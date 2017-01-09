package com.pkiykov.userpostspagersample.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.pkiykov.userpostspagersample.R;
import com.pkiykov.userpostspagersample.ui.fragments.PostsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null)
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, PostsFragment.getInstance())
                    .commit();
    }

    public void setToolBarTitle(String title) {
        setTitle(title);
    }

    public void push(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void onNetworkError() {
        Toast.makeText(this, R.string.connection_problem, Toast.LENGTH_LONG).show();
    }

}
