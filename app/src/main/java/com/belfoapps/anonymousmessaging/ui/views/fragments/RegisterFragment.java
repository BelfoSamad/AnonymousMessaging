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

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";
    /**************************************** Declarations ****************************************/
    private AuthenticationPresenter mPresenter;
    private AuthenticationActivity mActivity;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.password_confirm)
    EditText password_confirm;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.register)
    public void register() {
        mPresenter.registerUser(email.getText().toString(),
                password.getText().toString(), password_confirm.getText().toString());
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
}
