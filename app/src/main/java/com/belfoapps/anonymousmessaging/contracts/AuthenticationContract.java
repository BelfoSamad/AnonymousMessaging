package com.belfoapps.anonymousmessaging.contracts;

import com.belfoapps.anonymousmessaging.base.BasePresenter;
import com.belfoapps.anonymousmessaging.base.BaseView;

public interface AuthenticationContract {

    interface Presenter extends BasePresenter<View> {

        void checkUserConnected();

        void singInUser(String username, String password);

        void registerUser(String email, String password, String confirm_password);

        boolean validateEmail(String email);

        boolean validatePassword(String password, String password_confirm);
    }

    interface View extends BaseView {

        void initViewPager();

        void initTabLayout();

        void enableTabAt(int x);

        void showErrorEmail();

        void showErrorPassword();

        void goToMessages();

    }

}
