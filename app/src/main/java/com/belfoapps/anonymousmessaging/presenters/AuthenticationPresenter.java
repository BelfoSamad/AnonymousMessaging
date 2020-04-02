package com.belfoapps.anonymousmessaging.presenters;

import android.util.Log;
import android.util.Patterns;

import com.belfoapps.anonymousmessaging.contracts.AuthenticationContract;
import com.belfoapps.anonymousmessaging.ui.views.activities.AuthenticationActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationPresenter implements AuthenticationContract.Presenter {
    private static final String TAG = "AuthenticationPresenter";
    /***************************************** Declarations ***************************************/
    private AuthenticationActivity mView;
    private FirebaseAuth mAuth;

    /***************************************** Constructor ****************************************/
    public AuthenticationPresenter(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
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
        if (!validateEmail(email)) {
            mView.showErrorEmail();
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
    public void registerUser(String email, String password, String confirm_password) {
        if (!validateEmail(email)) {
            mView.showErrorEmail();
            Log.d(TAG, "registerUser: Email wrong");
            return;
        }

        if (!validatePassword(password, confirm_password)) {
            Log.d(TAG, "registerUser: Password Wrong");
            mView.showErrorPassword();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mView.goToMessages();
                    } else {
                        Log.d(TAG, "onComplete: Failed To Signup");
                    }
                });
    }

    @Override
    public boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean validatePassword(String password, String password_confirm) {
        return password.equals(password_confirm);
    }
}
