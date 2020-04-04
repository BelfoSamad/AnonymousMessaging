package com.belfoapps.anonymousmessaging.ui.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

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
    @BindView(R.id.message_for)
    TextView message_for;
    @BindView(R.id.the_message)
    TextInputLayout message;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.back)
    public void goBack() {

    }

    @OnClick(R.id.send)
    public void sendMessage() {
        mPresenter.sendMessage(getIntent().getData().getQueryParameter("uid"), message.getEditText().getText().toString());
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

    }

    @Override
    public void showSendMessage(String username) {
        message_for.setText("Message For " + username);
    }

    @Override
    public void showNoNetwork() {

    }
}
