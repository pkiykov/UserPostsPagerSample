package com.pkiykov.userpostspagersample;

import android.app.Application;
import android.content.Context;

import com.pkiykov.userpostspagersample.data.api.ApiModule;
import com.pkiykov.userpostspagersample.data.database.DbModule;
import com.pkiykov.userpostspagersample.utils.ComponentReflectionInjector;
import com.pkiykov.userpostspagersample.utils.Injector;

public class UserPostsApplication extends Application implements Injector {

    private AppComponent appComponent;
    private ComponentReflectionInjector<AppComponent> injector;

    public static UserPostsApplication get(Context context) {
        return (UserPostsApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initAppComponent();
    }
    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .dbModule(new DbModule())
                .build();
        injector = new ComponentReflectionInjector<>(AppComponent.class, appComponent);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void inject(Object target) {
        injector.inject(target);
    }

}
