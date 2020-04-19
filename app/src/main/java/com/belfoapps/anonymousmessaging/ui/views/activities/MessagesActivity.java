package com.belfoapps.anonymousmessaging.ui.views.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.contracts.MessagesContract;
import com.belfoapps.anonymousmessaging.di.components.DaggerMVPComponent;
import com.belfoapps.anonymousmessaging.di.components.MVPComponent;
import com.belfoapps.anonymousmessaging.di.modules.ApplicationModule;
import com.belfoapps.anonymousmessaging.di.modules.MVPModule;
import com.belfoapps.anonymousmessaging.pojo.Message;
import com.belfoapps.anonymousmessaging.presenters.MessagesPresenter;
import com.belfoapps.anonymousmessaging.ui.adapters.MessagesAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class MessagesActivity extends AppCompatActivity implements MessagesContract.View {
    private static final String TAG = "MessagesActivity";
    private static final int COL_NUM = 1;
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    @Inject
    MessagesPresenter mPresenter;
    private MessagesAdapter mAdapter;
    private BottomSheetBehavior logout_popup_behavior;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.profile_picture)
    ImageView profile_picture;
    @BindView(R.id.uid)
    TextView uid;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.no_messages)
    ImageView noMessages;
    @BindView(R.id.no_network)
    ImageView noNetwork;

    @BindView(R.id.info_popup)
    CardView info_popup;
    @BindView(R.id.progress_logout)
    ProgressBar loading;
    @BindView(R.id.index)
    ImageView index;
    @BindView(R.id.logout)
    Button logout;

    @BindView(R.id.adView_messages)
    AdView ad;

    @BindView(R.id.dark_mode)
    ImageButton dark_mode_button;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.update_profile_picture)
    public void updateProfilePicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    @OnClick(R.id.copy_uid)
    public void copyUid() {
        mPresenter.copyUid();
    }

    @OnClick(R.id.logout)
    public void logout() {
        showLogoutLoading();
        mPresenter.logout();
        startActivity(new Intent(MessagesActivity.this, AuthActivity.class));
    }

    @OnClick(R.id.popup_logout_kicker)
    public void openLogoutPopup(View v) {
        if (v.getTag().equals(0)) {
            showLogoutPopup();
            v.setTag(1);
        } else {
            hideLogoutPopup();
            v.setTag(0);
        }
    }

    @OnClick(R.id.dark_mode)
    public void darkMode(View v) {
        if (v.getTag().equals(0)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            mPresenter.setDrkModeEnabled(true);
            v.setTag(1);
            restartApp();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            mPresenter.setDrkModeEnabled(false);
            v.setTag(0);
            restartApp();
        }
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
        setContentView(R.layout.activity_messages);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //set user infos
        mPresenter.setUserInfo();

        if (mPresenter.isDarkModeEnabled())
            dark_mode_button.setTag(1);
        else dark_mode_button.setTag(0);

        //init recyclerview
        mRefresh.setRefreshing(true);
        mPresenter.initRecyclerView();

        mRefresh.setOnRefreshListener(() -> mPresenter.updateRecyclerView());

        loading.bringToFront();

        initLogoutPopup();

        //Init Ad Banner
        initAdBanner();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri chosenImageUri = data.getData();
            mPresenter.updateProfilePicture(chosenImageUri);
        }
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
    public void setUserInfo(String username, String uid, Uri profile_picture_uri) {
        this.username.setText(username);
        this.uid.setText(uid);
        setProfilePicture(profile_picture_uri);
    }

    @Override
    public void setProfilePicture(Uri image_uri) {
        Log.d(TAG, "setProfilePicture: " + image_uri);
        if (image_uri != null)
            Glide.with(this)
                    .load(image_uri)
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.d(TAG, "onLoadFailed");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.d(TAG, "onResourceReady");
                            return false;
                        }
                    })
                    .into(profile_picture);
    }

    @Override
    public void initLogoutPopup() {
        logout_popup_behavior = BottomSheetBehavior.from(info_popup);

        logout_popup_behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0) {
                    index.setImageResource(R.drawable.index_popup);
                    info_popup.setCardBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    index.setImageResource(R.drawable.index);
                    info_popup.setCardBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });
    }

    @Override
    public void initRecyclerView(ArrayList<Message> messages) {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);

        ArrayList<Message> messages_copy = new ArrayList<>();
        for (Message message :
                messages) {
            messages_copy.add((Message) message.clone());
        }
        mAdapter = new MessagesAdapter(messages_copy, this, mPresenter);

        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.addItemDecoration(new MessagesItemDecoration());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateRecyclerView(ArrayList<Message> messages) {
        if (mAdapter != null) {
            //Deleting the List of the Categories
            mAdapter.clearAll();

            // Adding The New List of Categories
            mAdapter.addAll(messages);
        }
    }

    @Override
    public void showNoNetwork() {
        Log.d(TAG, "showNoNetwork: In");
        mRefresh.setRefreshing(false);

        noNetwork.setVisibility(View.VISIBLE);
        noMessages.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showNoMessages() {
        Log.d(TAG, "showNoMessages: In");
        mRefresh.setRefreshing(false);

        noMessages.setVisibility(View.VISIBLE);
        noNetwork.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);

    }

    @Override
    public void showMessages() {
        Log.d(TAG, "showMessages: In");
        mRefresh.setRefreshing(false);

        mRecyclerView.setVisibility(View.VISIBLE);
        noNetwork.setVisibility(View.GONE);
        noMessages.setVisibility(View.GONE);
    }

    @Override
    public void showLogoutPopup() {
        logout_popup_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void hideLogoutPopup() {
        logout_popup_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void showLogoutLoading() {
        Log.d(TAG, "showLogoutLoading");
        logout.setText("");
        loading.setVisibility(View.VISIBLE);
        loading.bringToFront();
    }

    @Override
    public void hideLogoutLoading() {
        logout.setText(getResources().getString(R.string.logout));
        loading.setVisibility(View.GONE);
    }

    private void restartApp() {
        finish();
        startActivity(new Intent(getApplicationContext(), AuthActivity.class));
    }
}
