package com.pkiykov.userpostspagersample.ui.presenters;

import android.os.Bundle;
import android.os.Environment;

import com.pkiykov.userpostspagersample.data.api.UserPostsService;
import com.pkiykov.userpostspagersample.ui.MainActivity;
import com.pkiykov.userpostspagersample.ui.fragments.PostsFragment;
import com.pkiykov.userpostspagersample.utils.InternetConnection;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostsFragmentPresenter extends BasePresenter<PostsFragment> {


    private static final int REQUEST_POSTS = 1;
    private Subscription internetStatusSubscription;

    @Inject
    UserPostsService userPostsService;
    @Inject
    InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        restartableReplay(REQUEST_POSTS,
                () -> userPostsService.getPosts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                (postsFragment1, posts) -> {
                    postsFragment1.showPosts(posts);
                },
                (postsFragment, throwable) -> {
                    ((MainActivity) postsFragment.getActivity()).onNetworkError();
                    postsFragment.stopAnimation();
                    waitForInternetToComeBack();
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
