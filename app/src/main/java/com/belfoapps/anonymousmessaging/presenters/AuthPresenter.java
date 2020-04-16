package com.belfoapps.anonymousmessaging.presenters;

import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.contracts.AuthenticationContract;
import com.belfoapps.anonymousmessaging.ui.views.activities.AuthActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthPresenter implements AuthenticationContract.Presenter {
    private static final String TAG = "AuthenticationPresenter";
    /***************************************** Declarations ***************************************/
    private AuthActivity mView;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private CallbackManager mCallbackManager;

    /***************************************** Constructor ****************************************/
    public AuthPresenter(FirebaseAuth mAuth, FirebaseFirestore mDb) {
        this.mAuth = mAuth;
        this.mDb = mDb;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(AuthenticationContract.View view) {
        mView = (AuthActivity) view;
    }

    @Override
    public void dettach() {
        mView = null;
    }

    @Override
    public boolean isAttached() {
        return !(mView == null);
    }

    /***************************************** Methods ********************************************/
    @Override
    public void checkUserConnected() {
        if (mAuth.getCurrentUser() != null)
            mView.goToMessages();
        else mView.initViewPager();
    }

    @Override
    public void configGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mView.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(mView, gso);
    }

    @Override
    public void configFacebookLoginButton(LoginButton button) {
        mCallbackManager = CallbackManager.Factory.create();
        button.setReadPermissions("email", "public_profile");
        button.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                signInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(mView, "Couldn't Login With Facebook", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(mView, "Failed Login\n" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void singInUser(String email, String password) {
        if (email.isEmpty()) {
            mView.showErrorEmail("Login", "Empty");
            return;
        } else if (emailNotValid(email)) {
            mView.showErrorEmail("Login", "Invalid");
            return;
        }

        if (password.isEmpty()) {
            mView.showErrorPassword("Login", "Empty");
            return;
        }

        mView.showLoading(true);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    mView.hideLoading(true);
                    if (task.isSuccessful()) {
                        mView.goToMessages();
                    } else {
                        Toast.makeText(mView, "Failed Login\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void signInWithGoogle(GoogleSignInAccount account) {
        mView.showLoading(true);
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mView, task -> {
                    mView.hideLoading(true);
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Map<String, Object> data = new HashMap<>();
                            data.put("uid", user.getUid());
                            data.put("username", user.getDisplayName());
                            mDb.collection("users")
                                    .add(data)
                                    .addOnCompleteListener(task12 -> {
                                        if (task12.isSuccessful())
                                            Log.d(TAG, "onComplete: Adding Completed");
                                        else Log.d(TAG, "onComplete: Adding Failed");
                                    });
                        }
                        mView.goToMessages();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(mView, "Login Failed\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void signInWithFacebook(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        mView.showLoading(true);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mView, task -> {
                    mView.hideLoading(true);
                    if (task.isSuccessful()) {
                        if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Map<String, Object> data = new HashMap<>();
                            data.put("uid", user.getUid());
                            data.put("username", user.getDisplayName());
                            mDb.collection("users")
                                    .add(data)
                                    .addOnCompleteListener(task12 -> {
                                        if (task12.isSuccessful())
                                            Log.d(TAG, "onComplete: Adding Completed");
                                        else Log.d(TAG, "onComplete: Adding Failed");
                                    });
                        }
                        mView.goToMessages();
                    } else {
                        Toast.makeText(mView, "Login Failed\n" + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void registerUser(String username, String email, String password, String confirm_password) {
        if (email.isEmpty())
            mView.showErrorEmail("Register", "Empty");
        else if (emailNotValid(email)) {
            mView.showErrorEmail("Register", "Invalid");
            return;
        }

        if (password.isEmpty() || confirm_password.isEmpty()) {
            mView.showErrorPassword("Register", "Empty");
            return;
        } else if (!validatePassword(password, confirm_password)) {
            Log.d(TAG, "registerUser: Password Wrong");
            mView.showErrorPassword("Register", "Not Match");
            return;
        }

        if (username.isEmpty()) {
            mView.showErrorUsername();
            return;
        }

        mView.showLoading(true);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    mView.hideLoading(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username).build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                });

                        Map<String, Object> data = new HashMap<>();
                        data.put("uid", user.getUid());
                        data.put("username", username);
                        mDb.collection("users")
                                .add(data)
                                .addOnCompleteListener(task12 -> {
                                    if (task12.isSuccessful())
                                        Log.d(TAG, "onComplete: Adding Completed");
                                    else Log.d(TAG, "onComplete: Adding Failed");
                                });

                        mView.goToMessages();
                    } else {
                        Toast.makeText(mView, "Registration Failed\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean emailNotValid(String email) {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean validatePassword(String password, String password_confirm) {
        return password.equals(password_confirm);
    }

    @Override
    public GoogleSignInClient getGoogleClient() {
        return mGoogleSignInClient;
    }

    @Override
    public CallbackManager getCallbackManager() {
        return mCallbackManager;
    }
}
