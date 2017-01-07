package com.pkiykov.userpostspagersample.ui.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.florent37.viewanimator.ViewAnimator;
import com.pkiykov.userpostspagersample.R;
import com.pkiykov.userpostspagersample.UserPostsApplication;
import com.pkiykov.userpostspagersample.ui.MainActivity;
import com.pkiykov.userpostspagersample.ui.adapters.PostsPagerAdapter;
import com.pkiykov.userpostspagersample.ui.modules.PostsFragmentModule;
import com.pkiykov.userpostspagersample.ui.presenters.PostsFragmentPresenter;

import java.io.File;
import java.io.IOException;

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
                .getPostsComponent()
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
        if (state != null) {
            viewPager.onRestoreInstanceState(state);
            saveBtn.setVisibility(View.VISIBLE);
        } else {
            ViewAnimator
                    .animate(imageView, viewPager)
                    .scale(0, 1)
                    .start()
                    .onStop(() -> {
                        if (saveBtn != null)
                            saveBtn.setVisibility(View.VISIBLE);
                    });
        }
    }

    @OnClick (R.id.save_logs_btn)
    public void saveLogs(){
        getPresenter().saveLogs();
        try {
            final File path = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
            Runtime.getRuntime().exec(
                    "logcat  -d -f " + path + File.separator
                            + "dbo_logcat"
                            + ".txt");
            Log.d("MyTag", "path :"+path.getAbsolutePath());
        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

}
