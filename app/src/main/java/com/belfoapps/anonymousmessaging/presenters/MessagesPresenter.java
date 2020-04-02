package com.belfoapps.anonymousmessaging.presenters;

import com.belfoapps.anonymousmessaging.contracts.MessagesContract;
import com.belfoapps.anonymousmessaging.ui.views.activities.MessagesActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MessagesPresenter implements MessagesContract.Presenter{
    /***************************************** Declarations ***************************************/
    private MessagesActivity mView;
    private FirebaseAuth mAuth;

    /***************************************** Constructor ****************************************/
    public MessagesPresenter(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(MessagesContract.View view) {
        mView = (MessagesActivity) view;
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
    @Override
    public String getUser() {
        return mAuth.getCurrentUser().getUid();
    }
}
