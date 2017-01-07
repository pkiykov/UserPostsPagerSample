package com.pkiykov.userpostspagersample.ui.components;


import com.pkiykov.userpostspagersample.ui.fragments.FragmentsScope;
import com.pkiykov.userpostspagersample.ui.fragments.PostsFragment;
import com.pkiykov.userpostspagersample.ui.modules.PostsFragmentModule;

import dagger.Subcomponent;


@FragmentsScope
@Subcomponent(
        modules = PostsFragmentModule.class
)

public interface PostsFragmentComponent {
    PostsFragment inject(PostsFragment postsFragment);
}

