package com.belfoapps.anonymousmessaging.ui.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.presenters.AuthPresenter;
import com.belfoapps.anonymousmessaging.ui.views.activities.AuthActivity;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    /**************************************** Declarations ****************************************/
    private AuthPresenter mPresenter;
    private AuthActivity mActivity;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.email)
    TextInputLayout email;
    @BindView(R.id.password)
    TextInputLayout password;
    @BindView(R.id.progress_login)
    ProgressBar loading;
    @BindView(R.id.login)
    Button login;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.login)
    public void login(View v) {
        mPresenter.singInUser(email.getEditText().getText().toString(), password.getEditText().getText().toString());
    }

    /***************************************** Constructor ****************************************/
    public LoginFragment() {
        // Required empty public constructor
    }

    public LoginFragment(AuthPresenter mPresenter, AuthActivity mActivity) {
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
        email.setError(getResources().getString(R.string.email_empty));
    }

    public void setInvalidEmailError() {
        email.setError(getResources().getString(R.string.email_invalid));
    }

    public void setEmptyPasswordError() {
        password.setError(getResources().getString(R.string.empty));
    }

    public void showLoading() {
        login.setText("");
        loading.setVisibility(View.VISIBLE);
        loading.bringToFront();
    }

    public void hideLoading() {
        login.setText(getResources().getString(R.string.login));
        loading.setVisibility(View.GONE);
    }
}
