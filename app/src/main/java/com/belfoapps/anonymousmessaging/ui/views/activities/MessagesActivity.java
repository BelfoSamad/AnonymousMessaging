package com.belfoapps.anonymousmessaging.ui.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.contracts.MessagesContract;
import com.belfoapps.anonymousmessaging.di.components.DaggerMVPComponent;
import com.belfoapps.anonymousmessaging.di.components.MVPComponent;
import com.belfoapps.anonymousmessaging.di.modules.ApplicationModule;
import com.belfoapps.anonymousmessaging.di.modules.MVPModule;
import com.belfoapps.anonymousmessaging.pojo.Message;
import com.belfoapps.anonymousmessaging.presenters.MessagesPresenter;
import com.belfoapps.anonymousmessaging.ui.MessagesItemDecoration;
import com.belfoapps.anonymousmessaging.ui.adapters.MessagesAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessagesActivity extends AppCompatActivity implements MessagesContract.View {
    private static final int COL_NUM = 1;
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    @Inject
    MessagesPresenter mPresenter;
    private MessagesAdapter mAdapter;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.uid)
    TextView uid;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.no_messages)
    ImageView noMessages;
    @BindView(R.id.no_network)
    ImageView noNetwork;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.update_profile_picture)
    public void updateProfilePicture() {
        mPresenter.updateProfilePicture();
    }

    @OnClick(R.id.copy_uid)
    public void copyUid() {
        mPresenter.copyUid();
    }

    @OnClick(R.id.logout)
    public void logout() {
        mPresenter.logout();
        startActivity(new Intent(MessagesActivity.this, AuthenticationActivity.class));
    }


    /**************************************** Essential Methods ***********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //set user infos
        mPresenter.setUserInfo();

        //init recyclerview
        mPresenter.initRecyclerView();
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
    public void setUserInfo(String username, String uid) {
        this.username.setText(username);
        this.uid.setText(uid);
    }

    @Override
    public void initRecyclerView(ArrayList<Message> messages) {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new MessagesAdapter(messages, this, mPresenter);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new MessagesItemDecoration());
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
        noNetwork.setVisibility(View.VISIBLE);
        noMessages.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showNoMessages() {
        noMessages.setVisibility(View.VISIBLE);
        noNetwork.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);

    }

    @Override
    public void showMessages() {
        mRecyclerView.setVisibility(View.VISIBLE);
        noNetwork.setVisibility(View.GONE);
        noMessages.setVisibility(View.GONE);

    }
}
