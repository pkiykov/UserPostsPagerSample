package com.pkiykov.userpostspagersample.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import rx.Observable;
import rx.subjects.PublishSubject;

public class InternetConnection {

    private BroadcastReceiver broadcastReceiver;
    private PublishSubject<Boolean> internetStatusHotObservable;
    private Context context;

    public InternetConnection(Context context) {
        this.context = context;
    }

    private boolean isInternetOn() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public Observable<Boolean> getInternetStatusHotObservable() {
        internetStatusHotObservable = PublishSubject.create();
        return internetStatusHotObservable.asObservable().serialize();
    }

    public void registerBroadCastReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                internetStatusHotObservable.onNext(isInternetOn());
            }
        };

        context.registerReceiver(broadcastReceiver, filter);
    }

    public void unRegisterBroadCastReceiver() {
        try {
            context.unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException ignored){

        }
    }
}

