package com.belfoapps.anonymousmessaging.contracts;

import androidx.fragment.app.Fragment;

import com.belfoapps.anonymousmessaging.base.BasePresenter;
import com.belfoapps.anonymousmessaging.base.BaseView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface AuthenticationContract {

    interface Presenter extends BasePresenter<View> {

        void checkUserConnected();

        void configGoogleSignIn();

        void configFacebookLoginButton(LoginButton button);

        void singInUser(String username, String password);

        void signInWithGoogle(GoogleSignInAccount account);

        void signInWithFacebook(AccessToken accessToken);

        void registerUser(String username, String email, String password, String confirm_password);

        //void registerWithGoogle();

        void registerWithFacebook();

        boolean emailNotValid(String email);

        boolean validatePassword(String password, String password_confirm);

        GoogleSignInClient getGoogleClient();

        CallbackManager getCallbackManager();
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
