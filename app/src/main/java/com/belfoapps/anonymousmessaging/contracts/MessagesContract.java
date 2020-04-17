package com.belfoapps.anonymousmessaging.contracts;

import android.net.Uri;

import com.belfoapps.anonymousmessaging.base.BasePresenter;
import com.belfoapps.anonymousmessaging.base.BaseView;
import com.belfoapps.anonymousmessaging.pojo.Message;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public interface MessagesContract {

    interface Presenter extends BasePresenter<View> {

        boolean isDarkModeEnabled();

        void setDrkModeEnabled(boolean isChecked);

        void setUserInfo();

        void copyUid();

        void updateProfilePicture(Uri image_uri);

        void initRecyclerView();

        void updateRecyclerView();

        void deleteMessage(int position);

        void likeMessage(Message message, boolean liked);

        void loadAd(AdView ad);

        void logout();

    }

    interface View extends BaseView {

        void initAdBanner();

        void setUserInfo(String username, String uid, Uri profile_picture_uri);

        void setProfilePicture(Uri image_uri);

        void initLogoutPopup();

        void initRecyclerView(ArrayList<Message> messages);

        void updateRecyclerView(ArrayList<Message> messages);

        void showNoNetwork();

        void showNoMessages();

        void showMessages();

        void showLogoutPopup();

        void hideLogoutPopup();

        void showLogoutLoading();

        void hideLogoutLoading();
    }

}
