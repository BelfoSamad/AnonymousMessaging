package com.belfoapps.anonymousmessaging.ui.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.contracts.SendMessageContract;
import com.belfoapps.anonymousmessaging.di.components.DaggerMVPComponent;
import com.belfoapps.anonymousmessaging.di.components.MVPComponent;
import com.belfoapps.anonymousmessaging.di.modules.ApplicationModule;
import com.belfoapps.anonymousmessaging.di.modules.MVPModule;
import com.belfoapps.anonymousmessaging.presenters.SendMessagePresenter;
import com.google.android.material.textfield.TextInputLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.back)
    public void goBack() {
        startActivity(new Intent(SendMessageActivity.this, AuthActivity.class));
    }

    @OnClick(R.id.send)
    public void sendMessage() {
        mPresenter.sendMessage(getIntent().getData().getQueryParameter("uid"), message.getEditText().getText().toString());
        message.getEditText().setText("");
        Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
    }

    /**************************************** Essential Methods ***********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        Intent intent = getIntent();
        Uri data = intent.getData();

        message.setHintEnabled(false);

        mPresenter.checkUserExist(data.getQueryParameter("uid"));
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
    public void showUserNotFound() {
        noUser.setVisibility(View.VISIBLE);
        noNetwork.setVisibility(View.GONE);
        mContainer.setVisibility(View.GONE);
    }

    @Override
    public void showSendMessage(String username) {
        message_for.setText("Message For " + username);
        noUser.setVisibility(View.GONE);
        noNetwork.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoNetwork() {
        noUser.setVisibility(View.GONE);
        noNetwork.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.GONE);
    }
}
