package com.belfoapps.anonymousmessaging.ui.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.presenters.AuthenticationPresenter;
import com.belfoapps.anonymousmessaging.ui.views.activities.AuthActivity;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    /**************************************** Declarations ****************************************/
    private AuthenticationPresenter mPresenter;
    private AuthActivity mActivity;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.email)
    TextInputLayout email;
    @BindView(R.id.password)
    TextInputLayout password;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.login)
    public void login() {
        mPresenter.singInUser(email.getEditText().getText().toString(), password.getEditText().getText().toString());
    }

    /***************************************** Constructor ****************************************/
    public LoginFragment() {
        // Required empty public constructor
    }

    public LoginFragment(AuthenticationPresenter mPresenter, AuthActivity mActivity) {
        this.mPresenter = mPresenter;
        this.mActivity = mActivity;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    public void setEmptyEmailError() {
        email.setError("This Shouldn't Be Empty");
    }

    public void setInvalidEmailError() {
        email.setError("Email is Invalid");
    }

    public void setEmptyPasswordError() {
        password.setError("This Shouldn't Be Empty");
    }
}
