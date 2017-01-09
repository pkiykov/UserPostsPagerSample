package com.pkiykov.userpostspagersample.data.model;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pkiykov.userpostspagersample.PostsModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userId",
        "id",
        "title",
        "body"
})
public class Post implements PostsModel{

    public static final String POST_TABLE_NAME = "posts";

    @JsonProperty("userId")
    private long userId;
    @JsonProperty("id")
    private long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("body")
    private String body;

    @JsonProperty("userId")
    public long getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    @JsonProperty("body")
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public long userId() {
        return userId;
    }

    @Override
    public long id() {
        return id;
    }

    @Nullable
    @Override
    public String title() {
        return title;
    }

    @Nullable
    @Override
    public String body() {
        return body;
    }

   /* public Post(long userId, long id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

     public static final Factory FACTORY = new Factory<>(Post::new);*/
}