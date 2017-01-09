package com.pkiykov.userpostspagersample.ui.presenters;

import android.os.Bundle;
import android.os.Environment;

import com.pkiykov.userpostspagersample.data.api.UserPostsService;
import com.pkiykov.userpostspagersample.data.database.DatabaseHelper;
import com.pkiykov.userpostspagersample.data.model.Post;
import com.pkiykov.userpostspagersample.ui.MainActivity;
import com.pkiykov.userpostspagersample.ui.fragments.PostsFragment;
import com.pkiykov.userpostspagersample.utils.InternetConnection;
import com.squareup.sqlbrite.BriteDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostsFragmentPresenter extends RxPresenter<PostsFragment> {


    private static final int REQUEST_POSTS = 1;

    private Subscription internetStatusSubscription;

    @Inject
    BriteDatabase db;
    @Inject
    UserPostsService userPostsService;
    @Inject
    InternetConnection internetConnection;
    @Inject
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        restartableReplay(REQUEST_POSTS,
                () -> userPostsService.getPosts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                (postsFragment1, posts) -> {
                    new Thread(() -> {
                        //postsFragment1.getActivity().deleteDatabase(databaseHelper.getDatabaseName());
                        databaseHelper.savePostsToDB(posts);
                    }).start();
                    postsFragment1.showPosts(posts);
                },
                (postsFragment, throwable) -> {
                    ((MainActivity) postsFragment.getActivity()).onNetworkError();
                    Observable<ArrayList<Post>> observable = db.createQuery(Post.POST_TABLE_NAME, Post.SELECT_ALL)
                            .mapToOne(cursor -> {
                                ArrayList<Post> postsArrayList = new ArrayList<>();
                                do {
                                    Post post = new Post();
                                    post.setUserId(cursor.getInt(0));
                                    post.setId(cursor.getInt(1));
                                    post.setTitle(cursor.getString(2));
                                    post.setBody(cursor.getString(3));
                                    postsArrayList.add(post);
                                } while (cursor.moveToNext());
                                return postsArrayList;
                            }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                    observable.subscribe(posts -> {
                        if (posts.size() > 0) {
                            postsFragment.showPosts(posts);
                        }
                        PostsFragmentPresenter.this.waitForInternetToComeBack();
                    });
                });
    }


    private void waitForInternetToComeBack() {
        if (internetStatusSubscription == null || internetStatusSubscription.isUnsubscribed()) {
            internetStatusSubscription = internetConnection.getInternetStatusHotObservable()
                    .filter(internetConnectionStatus -> internetConnectionStatus)
                    .subscribe(internetConnectionStatus -> {
                        request();
                        stopWaitForInternetToComeBack();
                    });
        }
        internetConnection.registerBroadCastReceiver();
    }

    private void stopWaitForInternetToComeBack() {
        if (internetStatusSubscription != null && !internetStatusSubscription.isUnsubscribed()) {
            internetStatusSubscription.unsubscribe();
            internetConnection.unRegisterBroadCastReceiver();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopWaitForInternetToComeBack();
    }

    public void request() {
        start(REQUEST_POSTS);
    }

    public void saveLogs() {
        new Thread(() -> {
            try {
                final File path = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
                Runtime.getRuntime().exec(
                        "logcat  -d -f " + path + File.separator
                                + "dbo_logcat"
                                + ".txt");
            } catch (IOException ignored) {
            }
        }).start();
    }

}
