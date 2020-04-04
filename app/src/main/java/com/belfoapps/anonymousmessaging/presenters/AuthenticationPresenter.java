package com.belfoapps.anonymousmessaging.presenters;

import android.util.Log;
import android.util.Patterns;

import com.belfoapps.anonymousmessaging.contracts.AuthenticationContract;
import com.belfoapps.anonymousmessaging.ui.views.activities.AuthenticationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationPresenter implements AuthenticationContract.Presenter {
    private static final String TAG = "AuthenticationPresenter";
    /***************************************** Declarations ***************************************/
    private AuthenticationActivity mView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    /***************************************** Constructor ****************************************/
    public AuthenticationPresenter(FirebaseAuth mAuth, FirebaseFirestore mDb) {
        this.mAuth = mAuth;
        this.mDb = mDb;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(AuthenticationContract.View view) {
        mView = (AuthenticationActivity) view;
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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mView.goToMessages();
                    } else {
                        Log.d(TAG, "onComplete: Failed To Signin");
                    }
                });
    }

    @Override
    public void signInWithGoogle() {

    }

    @Override
    public void signInWithFacebook() {

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

        if (username.isEmpty())
            mView.showErrorUsername();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
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
                        Log.d(TAG, "onComplete: Failed To Signup");
                    }
                });
    }

    @Override
    public void registerWithGoogle() {

    }

    @Override
    public void registerWithFacebook() {

    }

    @Override
    public boolean emailNotValid(String email) {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean validatePassword(String password, String password_confirm) {
        return password.equals(password_confirm);
    }
}
