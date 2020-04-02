package com.belfoapps.anonymousmessaging.base;

import androidx.multidex.MultiDexApplication;
import com.belfoapps.anonymousmessaging.di.components.ApplicationComponent;
import com.belfoapps.anonymousmessaging.di.components.DaggerApplicationComponent;
import com.belfoapps.anonymousmessaging.di.modules.ApplicationModule;


public class BaseApplication extends MultiDexApplication {
    ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize Dagger For Application
        appComponent = getComponent();

        //Inject the Component Here
        appComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        if (appComponent == null) {
            appComponent = DaggerApplicationComponent
                    .builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return appComponent;
    }
}
