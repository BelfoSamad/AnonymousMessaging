package com.belfoapps.anonymousmessaging.contracts;

import com.belfoapps.anonymousmessaging.base.BasePresenter;
import com.belfoapps.anonymousmessaging.base.BaseView;

public interface SendMessageContract {

    interface Presenter extends BasePresenter<View> {

        void checkUserExist(String uid);

        void sendMessage(String uid, String message);

    }

    interface View extends BaseView {

        void showUserNotFound();

        void showSendMessage(String username);

        void showNoNetwork();
    }

}
