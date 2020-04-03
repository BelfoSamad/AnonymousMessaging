package com.belfoapps.anonymousmessaging.contracts;

import com.belfoapps.anonymousmessaging.base.BasePresenter;
import com.belfoapps.anonymousmessaging.base.BaseView;
import com.belfoapps.anonymousmessaging.pojo.Message;

import java.util.ArrayList;

public interface MessagesContract {

    interface Presenter extends BasePresenter<View> {

        void setUserInfo();

        void copyUid();

        void updateProfilePicture();

        void initRecyclerView();

        void updateRecyclerView();

        void deleteMessage(Message message);

        void likeMessage(Message message, boolean liked);

        void logout();

    }

    interface View extends BaseView {

        void setUserInfo(String username, String uid);

        void initRecyclerView(ArrayList<Message> messages);

        void updateRecyclerView(ArrayList<Message> messages);

        void showNoNetwork();

        void showNoMessages();

        void showMessages();

    }

}
