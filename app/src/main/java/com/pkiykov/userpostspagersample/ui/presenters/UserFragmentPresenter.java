package com.pkiykov.userpostspagersample.ui.presenters;

import android.os.Bundle;

import com.pkiykov.userpostspagersample.data.api.UserPostsService;
import com.pkiykov.userpostspagersample.data.database.DatabaseHelper;
import com.pkiykov.userpostspagersample.data.model.User;
import com.pkiykov.userpostspagersample.ui.MainActivity;
import com.pkiykov.userpostspagersample.ui.fragments.UserFragment;
import com.pkiykov.userpostspagersample.utils.InternetConnection;

import javax.inject.Inject;

import icepick.Icepick;
import icepick.State;
import nucleus.presenter.RxPresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserFragmentPresenter extends RxPresenter<UserFragment> {

    private static final int GET_USER_REQUEST = 1;

    @State
    long userId;

    private User user;

    @Inject
    DatabaseHelper databaseHelper;

    private Subscription internetStatusSubscription;

    @Inject
    UserPostsService userPostsService;
    @Inject
    InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Icepick.restoreInstanceState(this, savedState);
        restartableLatestCache(GET_USER_REQUEST,
                () -> userPostsService.getUser(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                (userFragment, user) -> {
                    UserFragmentPresenter.this.user = user;
                    userFragment.showLoading(false);
                    userFragment.showUser(user);
                },
                (userFragment, throwable) -> {
                    ((MainActivity)(userFragment.getActivity())).onNetworkError();
                        waitForInternetToComeBack();
                        userFragment.activateButtons(false);
                        userFragment.showLoading(false);
                });
    }

    private void waitForInternetToComeBack() {
        if (internetStatusSubscription == null || internetStatusSubscription.isUnsubscribed()) {
            internetStatusSubscription = internetConnection.getInternetStatusHotObservable()
                    .filter(internetConnectionStatus -> internetConnectionStatus)
                    .subscribe(internetConnectionStatus -> {
                        request(userId);
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
    public void request(long userId) {
        this.userId = userId;
        start(GET_USER_REQUEST);
    }

    public void saveUser() {
        new Thread(() -> {
            databaseHelper.insertUserDataIntoDb(user);
        }).start();
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        Icepick.saveInstanceState(this, state);
    }
}
