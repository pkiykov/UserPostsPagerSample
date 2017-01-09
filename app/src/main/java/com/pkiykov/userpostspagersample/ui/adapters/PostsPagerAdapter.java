package com.pkiykov.userpostspagersample.ui.adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pkiykov.userpostspagersample.R;
import com.pkiykov.userpostspagersample.data.model.Post;
import com.pkiykov.userpostspagersample.ui.fragments.PostsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class PostsPagerAdapter extends PagerAdapter {

    private PostsFragment postsFragment;
    private List<Post> postArrayList = new ArrayList<>();

    @BindViews({R.id.post_1_id, R.id.post_2_id, R.id.post_3_id, R.id.post_4_id, R.id.post_5_id, R.id.post_6_id})
    List<TextView> postsIdList;
    @BindViews({R.id.title_1, R.id.title_2, R.id.title_3, R.id.title_4, R.id.title_5, R.id.title_6})
    List<TextView> titlesList;
    @BindViews({R.id.element_1, R.id.element_2, R.id.element_3, R.id.element_4, R.id.element_5, R.id.element_6})
    List<CardView> cardViewsList;

    public PostsPagerAdapter(PostsFragment postsFragment) {
        super();
        this.postsFragment = postsFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout linearLayout = (LinearLayout) postsFragment.getActivity().getLayoutInflater().inflate(R.layout.list_item, container, false);
        ButterKnife.bind(this, linearLayout);
        setUpElements(position);
        container.addView(linearLayout);
        return linearLayout;
    }

    private void setUpElements(int position) {
        int number = 0;
        for (int i = 0; i < postsIdList.size(); i++) {
            int elementsOnScreen = 6;
            int element = number + elementsOnScreen * position;
            if (element < postArrayList.size()) {
                titlesList.get(i).setText(postArrayList.get(element).getTitle());
                postsIdList.get(i).setText(String.valueOf(postArrayList.get(element).getId()));
                cardViewsList.get(i).setVisibility(View.VISIBLE);
                cardViewsList.get(i).setOnClickListener(v -> postsFragment.onItemClick(postArrayList.get(element).getUserId()));
                number++;
            } else {
                break;
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return (int) Math.ceil(postArrayList.size() / 6 + 1);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void updateList(List<Post> postArrayList) {
        this.postArrayList.clear();
        this.postArrayList.addAll(postArrayList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public int getListSize() {
        return postArrayList.size();
    }
}
