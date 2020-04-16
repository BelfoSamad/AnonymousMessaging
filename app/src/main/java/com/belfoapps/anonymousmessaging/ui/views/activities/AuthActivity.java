package com.belfoapps.anonymousmessaging.ui.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.contracts.AuthenticationContract;
import com.belfoapps.anonymousmessaging.di.components.DaggerMVPComponent;
import com.belfoapps.anonymousmessaging.di.components.MVPComponent;
import com.belfoapps.anonymousmessaging.di.modules.ApplicationModule;
import com.belfoapps.anonymousmessaging.di.modules.MVPModule;
import com.belfoapps.anonymousmessaging.presenters.AuthPresenter;
import com.belfoapps.anonymousmessaging.ui.adapters.AuthPagerAdapter;
import com.belfoapps.anonymousmessaging.ui.views.fragments.LoginFragment;
import com.belfoapps.anonymousmessaging.ui.views.fragments.RegisterFragment;
import com.belfoapps.anonymousmessaging.utils.Config;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity implements AuthenticationContract.View {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    @Inject
    AuthPresenter mPresenter;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private AuthPagerAdapter mAdapter;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout mTab;
    @BindView(R.id.facebook_login_button)
    LoginButton facebook_login;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.google_login)
    public void loginGoogle() {
        Intent signInIntent = mPresenter.getGoogleClient().getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.facebook_login)
    public void loginFacebook() {
        facebook_login.performClick();
    }

    /**************************************** Essential Methods ***********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //setup Google Conf
        mPresenter.configGoogleSignIn();

        //Init Facebook Login
        mPresenter.configFacebookLoginButton(facebook_login);
    }

    @Override
    public MVPComponent getComponent() {
        if (mvpComponent == null) {
            mvpComponent = DaggerMVPComponent
                    .builder()
                    .applicationModule(new ApplicationModule(getApplication()))
                    .mVPModule(new MVPModule(this))
                    .build();
        }
        return mvpComponent;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.checkUserConnected();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                mPresenter.signInWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        } else mPresenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    /**************************************** Methods *********************************************/
    @Override
    public void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        loginFragment = new LoginFragment(mPresenter, this);
        registerFragment = new RegisterFragment(mPresenter, this);

        fragments.add(loginFragment);
        fragments.add(registerFragment);

        mAdapter = new AuthPagerAdapter(getSupportFragmentManager(), fragments, AuthActivity.this);
        mViewPager.setAdapter(mAdapter);

        initTabLayout();
    }

    @Override
    public void initTabLayout() {
        mTab.setupWithViewPager(mViewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < mTab.getTabCount(); i++) {
            TabLayout.Tab tab = mTab.getTabAt(i);
            assert tab != null;
            tab.setCustomView(mAdapter.getTabView(i));
        }
        enableTabAt(0);

        //TabLayout Listener
        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                enableTabAt(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void enableTabAt(int x) {
        TabLayout.Tab tab = mTab.getTabAt(x);

        for (int i = 0; i < Config.tabTitles.length; i++) {
            if (i == x) {
                assert tab != null;
                Objects.requireNonNull(tab.getCustomView()).findViewById(R.id.tab_dot).setVisibility(View.VISIBLE);

            } else {
                TabLayout.Tab tab1 = mTab.getTabAt(i);
                assert tab1 != null;
                Objects.requireNonNull(tab1.getCustomView()).findViewById(R.id.tab_dot).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showErrorEmail(String fragment, String type) {
        switch (fragment) {
            case "Login":
                switch (type) {
                    case "Empty":
                        loginFragment.setEmptyEmailError();
                        break;
                    case "Invalid":
                        loginFragment.setInvalidEmailError();
                        break;
                }
                break;
            case "Register":
                switch (type) {
                    case "Empty":
                        registerFragment.setEmptyEmailError();
                        break;
                    case "Invalid":
                        registerFragment.setInvalidEmailError();
                        break;
                }
                break;
        }
    }

    @Override
    public void showErrorPassword(String fragment, String type) {
        switch (fragment) {
            case "Login":
                loginFragment.setEmptyPasswordError();
                break;
            case "Register":
                switch (type) {
                    case "Empty":
                        registerFragment.setEmptyPasswordError();
                        break;
                    case "Not Match":
                        registerFragment.setNotMatchingPasswordError();
                        break;
                }
                break;
        }
    }

    @Override
    public void showLoading(boolean login) {
        if (login) loginFragment.showLoading();
        else registerFragment.showLoading();
    }

    @Override
    public void hideLoading(boolean login) {
        if (login) loginFragment.hideLoading();
        else registerFragment.hideLoading();
    }

    @Override
    public void goToMessages() {
        startActivity(new Intent(AuthActivity.this, MessagesActivity.class));
    }

    @Override
    public void showErrorUsername() {
        registerFragment.setEmptyUsernameError();
    }
}
