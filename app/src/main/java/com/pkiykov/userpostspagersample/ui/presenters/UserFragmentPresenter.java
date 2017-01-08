package com.pkiykov.userpostspagersample.ui.presenters;

import android.os.Bundle;

import com.pkiykov.userpostspagersample.data.api.UserPostsService;
import com.pkiykov.userpostspagersample.data.database.DatabaseHelper;
import com.pkiykov.userpostspagersample.data.model.User;
import com.pkiykov.userpostspagersample.ui.MainActivity;
import com.pkiykov.userpostspagersample.ui.fragments.UserFragment;
import com.pkiykov.userpostspagersample.utils.InternetConnection;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserFragmentPresenter extends BasePresenter<UserFragment> {

    private static final int GET_USER_REQUEST = 1;
    private long userId;
    private User user;

   /* @Inject
    BriteDatabase db;*/
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

        restartableReplay(GET_USER_REQUEST,
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
            databaseHelper.insertDataIntoDb(user);
        }).start();
    }

}
