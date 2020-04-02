package com.belfoapps.anonymousmessaging.presenters;

import com.belfoapps.anonymousmessaging.contracts.SendMessageContract;
import com.belfoapps.anonymousmessaging.ui.views.activities.SendMessageActivity;

public class SendMessagePresenter implements SendMessageContract.Presenter{
    /***************************************** Declarations ***************************************/
    private SendMessageActivity mView;

    /***************************************** Constructor ****************************************/
    public SendMessagePresenter() {
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(SendMessageContract.View view) {
        mView = (SendMessageActivity) view;
    }

    @Override
    public void dettach() {
        mView = null;
    }

    @Override
    public boolean isAttached() {
        return !(mView == null);
    }

    /***************************************** Methods ********************************************/
}
