package com.articles.app.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.articles.app.Fragments.PostsDetailsFragment;
import com.articles.app.Models.PostInfo;

import java.util.ArrayList;

public class PostsViewPager extends FragmentPagerAdapter {
    private ArrayList<PostInfo> mPostInfo;

    public PostsViewPager(@NonNull FragmentManager fm, int behavior, ArrayList<PostInfo> postInfo) {
        super(fm, behavior);
        mPostInfo = postInfo;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return PostsDetailsFragment.newInstance(mPostInfo.get(position));
    }

    @Override
    public int getCount() {
        return mPostInfo.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPostInfo.get(position).getTitle();
    }
}
