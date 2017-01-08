package com.pkiykov.userpostspagersample.ui.modules;

import com.pkiykov.userpostspagersample.ui.adapters.PostsPagerAdapter;
import com.pkiykov.userpostspagersample.ui.fragments.FragmentsScope;
import com.pkiykov.userpostspagersample.ui.fragments.PostsFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class PostsFragmentModule {

    private PostsFragment postsFragment;

    public PostsFragmentModule(PostsFragment postsFragment) {
        this.postsFragment = postsFragment;
    }

    @Provides
    @FragmentsScope
    PostsPagerAdapter providePostsPagerAdapter(){
        return new PostsPagerAdapter(postsFragment);
    }

}
