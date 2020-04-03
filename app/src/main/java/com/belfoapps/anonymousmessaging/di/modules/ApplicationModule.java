package com.belfoapps.anonymousmessaging.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.belfoapps.anonymousmessaging.di.annotations.ApplicationContext;
import com.belfoapps.anonymousmessaging.models.SharedPreferencesHelper;
import com.belfoapps.anonymousmessaging.utils.Config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

//import com.belfoapps.recipepro.utils.GDPR;
//import com.google.ads.consent.ConsentForm;

@Module
public class ApplicationModule {

    //Declarations
    private final Application mApplication;
    //private ConsentForm form;

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
    Config providesConfig() {
        SharedPreferences preferences = mApplication.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        return new Config(preferences);
    }

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

    /*
    @Provides
    @Singleton
    GDPR providesGDPR(SharedPreferencesHelper sharedPreferencesHelper, Config config){
        return new GDPR(sharedPreferencesHelper, form, mApplication, config);
    }
     */
}
