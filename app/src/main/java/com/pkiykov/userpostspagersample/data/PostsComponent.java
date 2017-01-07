package com.pkiykov.userpostspagersample.data;

import com.pkiykov.userpostspagersample.data.api.ApiModule;
import com.pkiykov.userpostspagersample.data.api.PostsModule;
import com.pkiykov.userpostspagersample.ui.components.PostsFragmentComponent;
import com.pkiykov.userpostspagersample.ui.modules.PostsFragmentModule;
import com.pkiykov.userpostspagersample.ui.modules.UserFragmentModule;

import dagger.Subcomponent;

@PostsScope
@Subcomponent(
        modules = {
                PostsModule.class
        }
)
public interface PostsComponent {

    PostsFragmentComponent plus(PostsFragmentModule postsFragmentModule);

}
