package com.belfoapps.anonymousmessaging.di.modules;

import android.app.Activity;
import android.content.Context;

import com.belfoapps.anonymousmessaging.di.annotations.ActivityContext;
import com.belfoapps.anonymousmessaging.models.SharedPreferencesHelper;
import com.belfoapps.anonymousmessaging.presenters.AuthPresenter;
import com.belfoapps.anonymousmessaging.presenters.MessagesPresenter;
import com.belfoapps.anonymousmessaging.presenters.SendMessagePresenter;
import com.belfoapps.anonymousmessaging.utils.GDPR;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

//import com.belfoapps.recipepro.utils.GDPR;

@Module
public class MVPModule {

    private Activity mActivity;

    //Constructor
    public MVPModule(Activity mActivity) {
        this.mActivity = mActivity;
    }

    //Context
    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @Singleton
    AuthPresenter providesAuthPresenter(FirebaseAuth authInstance, FirebaseFirestore mDb, GDPR gdpr,
                                        SharedPreferencesHelper mSharedPrefs) {
        return new AuthPresenter(authInstance, mDb, gdpr, mSharedPrefs);
    }

    @Provides
    @Singleton
    MessagesPresenter providesMessagesPresenter(FirebaseAuth mAuth, FirebaseFirestore mDb, FirebaseStorage mStorage,
                                                SharedPreferencesHelper mSharedPrefs, GDPR gdpr) {
        return new MessagesPresenter(mAuth, mDb, mStorage, mSharedPrefs, gdpr);
    }

    @Provides
    @Singleton
    SendMessagePresenter providesSendMessagePresenter(FirebaseFirestore mDb, SharedPreferencesHelper mSharedPrefs, GDPR gdpr) {
        return new SendMessagePresenter(mDb, mSharedPrefs, gdpr);
    }

}
