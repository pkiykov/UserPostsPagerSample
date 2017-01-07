package com.pkiykov.userpostspagersample.ui.modules;

import com.pkiykov.userpostspagersample.ui.fragments.UserFragment;

import dagger.Module;

@Module
public class UserFragmentModule {

    private UserFragment userFragment;

    public UserFragmentModule(UserFragment userFragment) {
        this.userFragment = userFragment;
    }


}

