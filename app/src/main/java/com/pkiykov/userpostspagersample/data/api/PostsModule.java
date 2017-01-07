package com.pkiykov.userpostspagersample.data.api;

import com.pkiykov.userpostspagersample.data.PostsScope;
import com.pkiykov.userpostspagersample.data.model.Post;

import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class PostsModule {

    private List<Post> postsList;

    public PostsModule (List<Post> postsList) {
        this.postsList = postsList;
    }

    @Provides
    @PostsScope
    List<Post> providePostsList() {
        return postsList;
    }

}
