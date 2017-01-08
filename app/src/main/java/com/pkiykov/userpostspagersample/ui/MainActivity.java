package com.pkiykov.userpostspagersample.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.pkiykov.userpostspagersample.R;
import com.pkiykov.userpostspagersample.ui.fragments.PostsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setToolBarTitle(getString(R.string.title));

        if (savedInstanceState == null)
            startFragment(PostsFragment.getInstance());
    }

    public void setToolBarTitle(String title) {
        setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            finish();
            System.exit(1);
        }
        super.onBackPressed();
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
        Toast.makeText(this, R.string.connection_problem, Toast.LENGTH_LONG).show();
    }

}
