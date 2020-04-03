package com.belfoapps.anonymousmessaging.contracts;

import com.belfoapps.anonymousmessaging.base.BasePresenter;
import com.belfoapps.anonymousmessaging.base.BaseView;

public interface AuthenticationContract {

    interface Presenter extends BasePresenter<View> {

        void checkUserConnected();

        void singInUser(String username, String password);

        void signInWithGoogle();

        void signInWithFacebook();

        void registerUser(String username, String email, String password, String confirm_password);

        void registerWithGoogle();

        void registerWithFacebook();

        boolean emailNotValid(String email);

        boolean validatePassword(String password, String password_confirm);
    }

    interface View extends BaseView {

        void initViewPager();

        void initTabLayout();

        void enableTabAt(int x);

        void showErrorEmail(String fragment, String type);

        void showErrorPassword(String fragment, String type);

        void goToMessages();

        void showErrorUsername();
    }

}
