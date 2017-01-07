package com.pkiykov.userpostspagersample.data.api;


import com.pkiykov.userpostspagersample.data.model.Post;
import com.pkiykov.userpostspagersample.data.model.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface UserPostsService {

    @GET("users/{id}")
    Observable<User> getUser(@Path("id") long id);

    @GET("posts")
    Observable<List<Post>> getPosts();

}
