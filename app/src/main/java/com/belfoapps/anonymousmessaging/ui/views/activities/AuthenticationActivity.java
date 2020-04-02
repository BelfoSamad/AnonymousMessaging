package com.belfoapps.anonymousmessaging.ui.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.contracts.AuthenticationContract;
import com.belfoapps.anonymousmessaging.di.components.DaggerMVPComponent;
import com.belfoapps.anonymousmessaging.di.components.MVPComponent;
import com.belfoapps.anonymousmessaging.di.modules.ApplicationModule;
import com.belfoapps.anonymousmessaging.di.modules.MVPModule;
import com.belfoapps.anonymousmessaging.presenters.AuthenticationPresenter;
import com.belfoapps.anonymousmessaging.ui.AuthPagerAdapter;
import com.belfoapps.anonymousmessaging.ui.views.fragments.LoginFragment;
import com.belfoapps.anonymousmessaging.ui.views.fragments.RegisterFragment;
import com.belfoapps.anonymousmessaging.utils.Config;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthenticationActivity extends AppCompatActivity implements AuthenticationContract.View {
    private static final String TAG = "LoginActivity";
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    @Inject
    AuthenticationPresenter mPresenter;
    private AuthPagerAdapter mAdapter;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout mTab;

    /**************************************** Click Listeners *************************************/

    /**************************************** Essential Methods ***********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Attach View To Presenter
        mPresenter.attach(this);
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

    /**************************************** Methods *********************************************/
    @Override
    public void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        LoginFragment loginFragment = new LoginFragment(mPresenter, this);
        RegisterFragment registerFragment = new RegisterFragment(mPresenter, this);

        fragments.add(loginFragment);
        fragments.add(registerFragment);

        mAdapter = new AuthPagerAdapter(getSupportFragmentManager(), fragments, AuthenticationActivity.this);
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
    public void showErrorEmail() {
    }

    @Override
    public void showErrorPassword() {

    }

    @Override
    public void goToMessages() {
        //TODO: Send User
        startActivity(new Intent(AuthenticationActivity.this, MessagesActivity.class));
    }
}
