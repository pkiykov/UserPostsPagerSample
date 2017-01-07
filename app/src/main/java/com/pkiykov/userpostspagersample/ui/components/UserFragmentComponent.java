package com.pkiykov.userpostspagersample.ui.components;


import com.pkiykov.userpostspagersample.ui.fragments.FragmentsScope;
import com.pkiykov.userpostspagersample.ui.fragments.UserFragment;
import com.pkiykov.userpostspagersample.ui.modules.UserFragmentModule;

import dagger.Subcomponent;

    @FragmentsScope
    @Subcomponent(
            modules = UserFragmentModule.class
    )
    public interface UserFragmentComponent {
        UserFragment inject(UserFragment offersFragment);
    }
