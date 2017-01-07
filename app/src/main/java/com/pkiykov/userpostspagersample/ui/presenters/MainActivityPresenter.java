package com.pkiykov.userpostspagersample.ui.presenters;

import android.os.Bundle;

import com.pkiykov.userpostspagersample.data.api.UserPostsService;
import com.pkiykov.userpostspagersample.ui.MainActivity;
import com.pkiykov.userpostspagersample.utils.InternetConnection;

import javax.inject.Inject;

import icepick.State;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityPresenter extends BasePresenter<MainActivity> {

    private static final int REQUEST_POSTS = 1;
    private Subscription internetStatusSubscription;

    @State
    boolean isPostsLoadedAtLeastOnce;

    @Inject
    UserPostsService userPostsService;
    @Inject
    InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        restartableLatestCache(REQUEST_POSTS,
                () -> userPostsService.getPosts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                (activity, posts) -> {
                    isPostsLoadedAtLeastOnce = true;
                    activity.showLoading(false);
                    activity.showPosts(posts);
                },
                (mainActivity, throwable) -> {
                    mainActivity.onNetworkError();
                    if (!isPostsLoadedAtLeastOnce) {
                        waitForInternetToComeBack();
                    }
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
}
