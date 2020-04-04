package com.belfoapps.anonymousmessaging.ui.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.presenters.AuthenticationPresenter;
import com.belfoapps.anonymousmessaging.ui.views.activities.AuthenticationActivity;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";
    /**************************************** Declarations ****************************************/
    private AuthenticationPresenter mPresenter;
    private AuthenticationActivity mActivity;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.username)
    TextInputLayout username;
    @BindView(R.id.email)
    TextInputLayout email;
    @BindView(R.id.password)
    TextInputLayout password;
    @BindView(R.id.password_confirm)
    TextInputLayout password_confirm;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.register)
    public void register() {
        mPresenter.registerUser(username.getEditText().getText().toString(), email.getEditText().getText().toString(),
                password.getEditText().getText().toString(), password_confirm.getEditText().getText().toString());
    }

    @OnClick(R.id.google_register)
    public void registerGoogle() {
        mPresenter.registerWithGoogle();
    }

    @OnClick(R.id.facebook_register)
    public void registerFacebook() {
        mPresenter.registerWithFacebook();
    }

    /***************************************** Constructor ****************************************/
    public RegisterFragment() {
        // Required empty public constructor
    }

    public RegisterFragment(AuthenticationPresenter mPresenter, AuthenticationActivity mActivity) {
        this.mPresenter = mPresenter;
        this.mActivity = mActivity;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

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

    public void setNotMatchingPasswordError() {
        password_confirm.setError("Password should be matching");
    }

    public void setEmptyUsernameError() {
        username.setError("This Shouldn't Be Empty");
    }
}
