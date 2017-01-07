package com.pkiykov.userpostspagersample.data.api;

import android.app.Application;

import com.pkiykov.userpostspagersample.utils.InternetConnection;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {

    @Provides
    @Singleton
    InternetConnection provideInternetConnection(Application application){
        return new InternetConnection(application);
    }

}
