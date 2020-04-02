package com.belfoapps.anonymousmessaging.di.components;

import android.content.Context;

import com.belfoapps.anonymousmessaging.base.BaseApplication;
import com.belfoapps.anonymousmessaging.di.annotations.ApplicationContext;
import com.belfoapps.anonymousmessaging.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseApplication baseApplication);

    //Context
    @ApplicationContext
    Context getApplicationContext();
}
