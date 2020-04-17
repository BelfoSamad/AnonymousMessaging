package com.belfoapps.anonymousmessaging.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.belfoapps.anonymousmessaging.di.annotations.ApplicationContext;
import com.belfoapps.anonymousmessaging.models.SharedPreferencesHelper;
import com.belfoapps.anonymousmessaging.utils.GDPR;
import com.google.ads.consent.ConsentForm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    //Declarations
    private final Application mApplication;
    private ConsentForm form;

    //Constructor
    public ApplicationModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    //Context
    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }


    /*
        Models
     */

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }


    @Provides
    @Singleton
    SharedPreferences provideSharedPrefs() {
        return mApplication.getSharedPreferences("BASICS", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    SharedPreferencesHelper provideSharedPrefsHelper(SharedPreferences sharedPreferences, Gson gson) {
        return new SharedPreferencesHelper(sharedPreferences, gson);
    }

    /*
        Utils
     */

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    FirebaseFirestore provideFirebaseFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    FirebaseStorage provideFirebaseStorageInstance() {
        return FirebaseStorage.getInstance();
    }

    @Provides
    @Singleton
    GDPR providesGDPR(SharedPreferencesHelper sharedPreferencesHelper){
        return new GDPR(sharedPreferencesHelper, form, mApplication);
    }
}
