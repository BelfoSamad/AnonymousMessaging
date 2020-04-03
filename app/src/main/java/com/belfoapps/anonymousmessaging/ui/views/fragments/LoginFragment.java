package com.belfoapps.anonymousmessaging.ui.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.presenters.AuthenticationPresenter;
import com.belfoapps.anonymousmessaging.ui.views.activities.AuthenticationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    /**************************************** Declarations ****************************************/
    private AuthenticationPresenter mPresenter;
    private AuthenticationActivity mActivity;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.login)
    public void login() {
        mPresenter.singInUser(email.getText().toString(), password.getText().toString());
    }

    @OnClick(R.id.google_login)
    public void loginGoogle(){
        mPresenter.signInWithGoogle();
    }

    @OnClick(R.id.facebook_login)
    public void loginFacebook(){
        mPresenter.signInWithFacebook();
    }

    /***************************************** Constructor ****************************************/
    public LoginFragment() {
        // Required empty public constructor
    }

    public LoginFragment(AuthenticationPresenter mPresenter, AuthenticationActivity mActivity) {
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
