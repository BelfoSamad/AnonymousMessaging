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
    @BindView(R.id.progress_send_message)
    ProgressBar progress;

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

        //Progress
        progress.setVisibility(View.GONE);

        //show UI
        noUser.setVisibility(View.VISIBLE);
        noNetwork.setVisibility(View.GONE);
        mContainer.setVisibility(View.GONE);
    }

    @Override
    public void showSendMessage(String username) {

        //Progress
        progress.setVisibility(View.GONE);

        //Set Edit Text
        message.getEditText().setSingleLine(false);
        message.getEditText().setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        message.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        message.getEditText().setLines(1);
        message.getEditText().setMaxLines(10);
        message.getEditText().setVerticalScrollBarEnabled(true);
        message.getEditText().setMovementMethod(ScrollingMovementMethod.getInstance());
        message.getEditText().setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        message_for.setText("Message For " + username);

        //show UI
        noUser.setVisibility(View.GONE);
        noNetwork.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoNetwork() {

        //Progress
        progress.setVisibility(View.GONE);

        //show UI
        noUser.setVisibility(View.GONE);
        noNetwork.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.GONE);
    }
}
