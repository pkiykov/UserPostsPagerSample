package com.pkiykov.userpostspagersample;

import com.pkiykov.userpostspagersample.data.PostsComponent;
import com.pkiykov.userpostspagersample.data.api.ApiModule;
import com.pkiykov.userpostspagersample.data.api.PostsModule;
import com.pkiykov.userpostspagersample.data.api.UtilsModule;
import com.pkiykov.userpostspagersample.data.database.DbModule;
import com.pkiykov.userpostspagersample.ui.components.UserFragmentComponent;
import com.pkiykov.userpostspagersample.ui.modules.UserFragmentModule;
import com.pkiykov.userpostspagersample.ui.presenters.MainActivityPresenter;
import com.pkiykov.userpostspagersample.ui.presenters.PostsFragmentPresenter;
import com.pkiykov.userpostspagersample.ui.presenters.UserFragmentPresenter;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(
        modules = {
                AppModule.class,
                ApiModule.class,
                UtilsModule.class,
                DbModule.class
        }
)
public interface AppComponent {

    void inject(MainActivityPresenter mainActivityPresenter);

    PostsComponent plus(PostsModule postsModule);

    UserFragmentComponent plus(UserFragmentModule userFragmentModule);

    void inject(PostsFragmentPresenter postsFragmentPresenter);

    void inject(UserFragmentPresenter userFragmentPresenter);
}
