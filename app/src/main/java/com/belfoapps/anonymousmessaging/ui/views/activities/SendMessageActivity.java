package com.belfoapps.anonymousmessaging.ui.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.belfoapps.anonymousmessaging.R;
import com.belfoapps.anonymousmessaging.contracts.SendMessageContract;
import com.belfoapps.anonymousmessaging.di.components.DaggerMVPComponent;
import com.belfoapps.anonymousmessaging.di.components.MVPComponent;
import com.belfoapps.anonymousmessaging.di.modules.ApplicationModule;
import com.belfoapps.anonymousmessaging.di.modules.MVPModule;
import com.belfoapps.anonymousmessaging.presenters.SendMessagePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SendMessageActivity extends AppCompatActivity implements SendMessageContract.View {
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    @Inject
    SendMessagePresenter mPresenter;

    /**************************************** View Declarations ***********************************/
    /**************************************** Click Listeners *************************************/
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
}
