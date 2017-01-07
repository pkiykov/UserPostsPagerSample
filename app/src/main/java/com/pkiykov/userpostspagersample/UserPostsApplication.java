package com.pkiykov.userpostspagersample;

import android.app.Application;
import android.content.Context;

import com.pkiykov.userpostspagersample.data.PostsComponent;
import com.pkiykov.userpostspagersample.data.api.ApiModule;
import com.pkiykov.userpostspagersample.data.api.PostsModule;
import com.pkiykov.userpostspagersample.data.api.UtilsModule;
import com.pkiykov.userpostspagersample.data.database.DbModule;
import com.pkiykov.userpostspagersample.data.model.Post;
import com.pkiykov.userpostspagersample.utils.ComponentReflectionInjector;
import com.pkiykov.userpostspagersample.utils.Injector;

import java.util.List;

public class UserPostsApplication extends Application implements Injector {

    private AppComponent appComponent;
    private ComponentReflectionInjector<AppComponent> injector;
    private PostsComponent postsComponent;

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
                .utilsModule(new UtilsModule())
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

    public PostsComponent createPostsComponent(List<Post> postsList) {
        postsComponent = appComponent.plus(new PostsModule(postsList));
        return postsComponent;
    }

    public void releasePostsComponent() {
        postsComponent = null;
    }

    public PostsComponent getPostsComponent() {
        return postsComponent;
    }
}
