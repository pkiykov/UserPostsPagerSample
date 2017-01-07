package com.pkiykov.userpostspagersample.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pkiykov.userpostspagersample.R;
import com.pkiykov.userpostspagersample.UserPostsApplication;
import com.pkiykov.userpostspagersample.data.model.Post;
import com.pkiykov.userpostspagersample.ui.fragments.PostsFragment;
import com.pkiykov.userpostspagersample.ui.presenters.MainActivityPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(MainActivityPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<MainActivityPresenter> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpPresenter();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setToolBarTitle(getString(R.string.title));

        if (getFragmentManager().getBackStackEntryCount() == 0)
            showLoading(true);
        if (savedInstanceState == null)
            getPresenter().request();
    }

    public void setToolBarTitle(String title) {
        setTitle(title);
    }


    public void showLoading(boolean loading) {
        pbLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
    }


    private void setUpPresenter() {
        final PresenterFactory<MainActivityPresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(() -> {
            MainActivityPresenter presenter = superFactory.createPresenter();
            UserPostsApplication.get(this).getAppComponent().inject(presenter);
            return presenter;
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            finish();
            System.exit(1);
        }
        super.onBackPressed();
    }

    public void showPosts(List<Post> posts) {
        UserPostsApplication.get(this).createPostsComponent(posts);
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            startFragment(PostsFragment.getInstance());
        }
    }

    public void startFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    public void onNetworkError() {
        showLoading(false);
        Toast.makeText(this, R.string.connection_problem, Toast.LENGTH_LONG).show();
    }

    @Override
    public void finish() {
        super.finish();
        UserPostsApplication.get(this).releasePostsComponent();
    }

}
