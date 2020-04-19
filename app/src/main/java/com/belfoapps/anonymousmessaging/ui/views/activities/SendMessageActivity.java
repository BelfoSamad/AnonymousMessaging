package com.belfoapps.anonymousmessaging.ui.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.contracts.SendMessageContract;
import com.belfoapps.anonymousmessaging.di.components.DaggerMVPComponent;
import com.belfoapps.anonymousmessaging.di.components.MVPComponent;
import com.belfoapps.anonymousmessaging.di.modules.ApplicationModule;
import com.belfoapps.anonymousmessaging.di.modules.MVPModule;
import com.belfoapps.anonymousmessaging.presenters.SendMessagePresenter;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class SendMessageActivity extends AppCompatActivity implements SendMessageContract.View {
    private static final String TAG = "SendMessageActivity";
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    @Inject
    SendMessagePresenter mPresenter;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.message_container)
    RelativeLayout mContainer;
    @BindView(R.id.message_for)
    TextView message_for;
    @BindView(R.id.the_message)
    TextInputLayout message;
    @BindView(R.id.no_user)
    ImageView noUser;
    @BindView(R.id.no_network)
    ImageView noNetwork;
    @BindView(R.id.progress_send_message)
    ProgressBar progress;

    @BindView(R.id.adView_send_message)
    AdView ad;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.send)
    public void sendMessage() {
        mPresenter.sendMessage(getIntent().getData().getQueryParameter("uid"), message.getEditText().getText().toString());
        message.getEditText().setText("");
        Toast.makeText(this, getResources().getString(R.string.message_sent), Toast.LENGTH_SHORT).show();
    }

    /**************************************** Essential Methods ***********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Enable/Disable Dark Mode
        if (mPresenter.isDarkModeEnabled())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        //Set Dark Mode
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppDarkTheme);
        else setTheme(R.style.AppLightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        Intent intent = getIntent();
        Uri data = intent.getData();

        mPresenter.checkUserExist(data.getQueryParameter("uid"));

        //Init Ad Banner
        initAdBanner();

        //Load Interstitial Ad
        mPresenter.loadInterstitialAd();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ad.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ad.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ad.resume();
    }

    @Override
    public void onBackPressed() {
        mPresenter.showInterstitialAd();
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

    /**************************************** Methods *********************************************/
    @Override
    public void initAdBanner() {
        if (getResources().getBoolean(R.bool.AD_BANNER_Enabled)) {
            mPresenter.loadAd(ad);
        } else {
            ad.setVisibility(GONE);
        }
    }

    @Override
    public void showUserNotFound() {

        //Progress
        progress.setVisibility(GONE);

        //show UI
        noUser.setVisibility(View.VISIBLE);
        noNetwork.setVisibility(GONE);
        mContainer.setVisibility(GONE);
    }

    @Override
    public void showSendMessage(String username) {

        //Progress
        progress.setVisibility(GONE);

        //Set Edit Text
        message.getEditText().setSingleLine(false);
        message.getEditText().setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        message.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        message.getEditText().setLines(1);
        message.getEditText().setMaxLines(10);
        message.getEditText().setVerticalScrollBarEnabled(true);
        message.getEditText().setMovementMethod(ScrollingMovementMethod.getInstance());
        message.getEditText().setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        message_for.setText(getResources().getString(R.string.message_for) + username);

        //show UI
        noUser.setVisibility(GONE);
        noNetwork.setVisibility(GONE);
        mContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoNetwork() {

        //Progress
        progress.setVisibility(GONE);

        //show UI
        noUser.setVisibility(GONE);
        noNetwork.setVisibility(View.VISIBLE);
        mContainer.setVisibility(GONE);
    }
}
