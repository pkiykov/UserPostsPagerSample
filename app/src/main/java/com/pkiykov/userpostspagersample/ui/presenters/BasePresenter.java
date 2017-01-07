package com.pkiykov.userpostspagersample.ui.presenters;

import android.os.Bundle;

import icepick.Icepick;
import nucleus.presenter.RxPresenter;


public class BasePresenter<ViewType> extends RxPresenter<ViewType> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Icepick.restoreInstanceState(this, savedState);
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        Icepick.saveInstanceState(this, state);
    }
}
