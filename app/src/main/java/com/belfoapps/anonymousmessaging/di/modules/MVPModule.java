package com.belfoapps.anonymousmessaging.di.modules;

import android.app.Activity;
import android.content.Context;

import com.belfoapps.anonymousmessaging.di.annotations.ActivityContext;
import com.belfoapps.anonymousmessaging.presenters.AuthPresenter;
import com.belfoapps.anonymousmessaging.presenters.MessagesPresenter;
import com.belfoapps.anonymousmessaging.presenters.SendMessagePresenter;
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
    AuthPresenter providesAuthPresenter(FirebaseAuth authInstance, FirebaseFirestore mDb) {
        return new AuthPresenter(authInstance, mDb);
    }

    @Provides
    @Singleton
    MessagesPresenter providesMessagesPresenter(FirebaseAuth mAuth, FirebaseFirestore mDb, FirebaseStorage mStorage) {
        return new MessagesPresenter(mAuth, mDb, mStorage);
    }

    @Provides
    @Singleton
    SendMessagePresenter providesSendMessagePresenter(FirebaseFirestore mDb) {
        return new SendMessagePresenter(mDb);
    }

}
