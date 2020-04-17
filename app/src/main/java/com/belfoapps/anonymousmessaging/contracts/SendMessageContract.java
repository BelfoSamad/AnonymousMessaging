package com.belfoapps.anonymousmessaging.contracts;

import com.belfoapps.anonymousmessaging.base.BasePresenter;
import com.belfoapps.anonymousmessaging.base.BaseView;
import com.google.android.gms.ads.AdView;

public interface SendMessageContract {

    interface Presenter extends BasePresenter<View> {

        boolean isDarkModeEnabled();

        void checkUserExist(String uid);

        void sendMessage(String uid, String message);

        void loadAd(AdView ad);
    }

    interface View extends BaseView {

        void initAdBanner();

        void showUserNotFound();

        void showSendMessage(String username);

        void showNoNetwork();
    }

}
