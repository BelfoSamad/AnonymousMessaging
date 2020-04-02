package com.belfoapps.anonymousmessaging.contracts;

import com.belfoapps.anonymousmessaging.base.BasePresenter;
import com.belfoapps.anonymousmessaging.base.BaseView;

public interface MessagesContract {

    interface Presenter extends BasePresenter<View> {

        String getUser();

    }

    interface View extends BaseView {

    }

}
