package com.pkiykov.userpostspagersample.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.pkiykov.userpostspagersample.R;
import com.pkiykov.userpostspagersample.UserPostsApplication;
import com.pkiykov.userpostspagersample.data.model.Post;
import com.pkiykov.userpostspagersample.ui.MainActivity;
import com.pkiykov.userpostspagersample.ui.adapters.PostsPagerAdapter;
import com.pkiykov.userpostspagersample.ui.modules.PostsFragmentModule;
import com.pkiykov.userpostspagersample.ui.presenters.PostsFragmentPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(PostsFragmentPresenter.class)
public class PostsFragment extends BaseFragment<PostsFragmentPresenter> {

    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tabDots)
    TabLayout indicator;
    @BindView(R.id.save_logs_btn)
    FloatingActionButton saveBtn;

    @Inject
    PostsPagerAdapter postsPagerAdapter;

    @State
    Parcelable state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setUpComponent();
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    protected void setUpComponent() {
        UserPostsApplication.get(getActivity())
                .getAppComponent()
                .plus(new PostsFragmentModule(this))
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.posts_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager.setAdapter(postsPagerAdapter);
        indicator.setupWithViewPager(viewPager, true);
        if (savedInstanceState == null) {
            showAnimation();
            getPresenter().request();
        }
    }

    @OnClick(R.id.save_logs_btn)
    public void saveLogs() {
        getPresenter().saveLogs();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        if (viewPager != null) {
            super.onSaveInstanceState(bundle);
            state = viewPager.onSaveInstanceState();
            Icepick.saveInstanceState(this, bundle);
        }
    }

    public static PostsFragment getInstance() {
        return new PostsFragment();
    }

    public void onItemClick(long userId) {
        ((MainActivity) getActivity()).startFragment(UserFragment.getInstance(userId));
    }

    public void showAnimation() {

        Animation pulsation = AnimationUtils.loadAnimation(getActivity(), R.anim.pulse);
        pulsation.setRepeatCount(Animation.INFINITE);
        pulsation.setRepeatMode(Animation.RESTART);
        imageView.startAnimation(pulsation);

    }

    public void showPosts(List<Post> posts) {
        imageView.clearAnimation();
        saveBtn.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        postsPagerAdapter.updateList(posts);
        if (state != null) {
            viewPager.onRestoreInstanceState(state);
        }
    }

}
