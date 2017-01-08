package com.pkiykov.userpostspagersample.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkiykov.userpostspagersample.R;
import com.pkiykov.userpostspagersample.UserPostsApplication;
import com.pkiykov.userpostspagersample.data.model.User;
import com.pkiykov.userpostspagersample.ui.MainActivity;
import com.pkiykov.userpostspagersample.ui.modules.UserFragmentModule;
import com.pkiykov.userpostspagersample.ui.presenters.UserFragmentPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(UserFragmentPresenter.class)
public class UserFragment extends BaseFragment<UserFragmentPresenter> {

    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;
    @BindView(R.id.save_user_btn)
    Button saveUserBtn;
    @BindViews({R.id.post_number, R.id.user_name, R.id.nickname, R.id.tvCall, R.id.tvMail, R.id.tvWeb, R.id.tvLocation})
    List<TextView> tvList;
    @BindViews({R.id.mail, R.id.web, R.id.phone, R.id.city})
    List<RelativeLayout> buttonsList;

    public static final String SELECTED_USER_ID = "selected_user_id";

    public static UserFragment getInstance(long uesrId) {
        UserFragment userFragment = new UserFragment();
        Bundle args = new Bundle();
        args.putLong(SELECTED_USER_ID, uesrId);
        userFragment.setArguments(args);
        return userFragment;
    }

    @OnClick(R.id.save_user_btn)
    public void saveUser() {
        getPresenter().saveUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setUpComponent();
        super.onCreate(savedInstanceState);
    }

    private void setUpComponent() {
        UserPostsApplication.get(getActivity())
                .getAppComponent()
                .plus(new UserFragmentModule(this))
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long userId = getArguments().getLong(SELECTED_USER_ID);
        String title = getString(R.string.user_contact) + userId;
        ((MainActivity) (getActivity())).setToolBarTitle(title);
        if (savedInstanceState == null) {
            showLoading(true);
            getPresenter().request(userId);
        }
    }


    private void enableButtons(){
        buttonsList.get(0).setEnabled(true);
        buttonsList.get(1).setEnabled(true);
        buttonsList.get(2).setEnabled(true);
        buttonsList.get(3).setEnabled(true);
        saveUserBtn.setEnabled(true);
    }

    public void showUser(User user) {
        enableButtons();
        tvList.get(0).setText(String.valueOf(user.getId()));
        tvList.get(1).setText(user.getName());
        tvList.get(2).setText(user.getUsername());
        tvList.get(4).setText(user.getEmail());
        buttonsList.get(0).setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("mailto:" + user.getEmail()))));
        tvList.get(5).setText(user.getWebsite());
        buttonsList.get(1).setOnClickListener(v -> {
            String url = user.getWebsite();
            if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://" + url;
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
        });
        tvList.get(3).setText(user.getPhone());
        buttonsList.get(2).setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + user.getPhone()))));
        tvList.get(6).setText(user.getAddress().getCity());
        buttonsList.get(3).setOnClickListener(v -> {
            String uri = "http://maps.google.com/maps?q=loc:"
                    + user.getAddress().getGeo().getLat() + ","
                    + user.getAddress().getGeo().getLng() + " ("
                    + user.getAddress().getSuite() + ")";
            getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
        });
    }

    public void showLoading(boolean loading) {
        pbLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

}
