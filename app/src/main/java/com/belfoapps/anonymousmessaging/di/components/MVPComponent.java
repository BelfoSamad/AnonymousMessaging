package com.belfoapps.anonymousmessaging.di.components;

import android.content.Context;

import com.belfoapps.anonymousmessaging.di.annotations.ActivityContext;
import com.belfoapps.anonymousmessaging.di.modules.ApplicationModule;
import com.belfoapps.anonymousmessaging.di.modules.MVPModule;
import com.belfoapps.anonymousmessaging.ui.views.activities.AuthActivity;
import com.belfoapps.anonymousmessaging.ui.views.activities.MessagesActivity;
import com.belfoapps.anonymousmessaging.ui.views.activities.SendMessageActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, MVPModule.class})
public interface MVPComponent {

    //Inject in Activities
    void inject(AuthActivity authActivity);

    void inject(MessagesActivity messagesActivity);

    void inject(SendMessageActivity sendMessagesActivity);


    //Context
    @ActivityContext
    Context getActivityContext();
}
