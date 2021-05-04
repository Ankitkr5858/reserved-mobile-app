package com.articles.app.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.articles.app.Fragments.ArticleDetailsFragment1;
import com.articles.app.Models.Datum;

import java.util.List;

public class ArticlesViewPager extends FragmentPagerAdapter {

    private List<Datum> mArticles;

    public ArticlesViewPager(@NonNull FragmentManager fm, int behavior, List<Datum> articles) {
        super(fm, behavior);
        mArticles = articles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ArticleDetailsFragment1.newInstance(mArticles.get(position));
    }

    @Override
    public int getCount() {
        return mArticles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mArticles.get(position).getTitle();
    }
}

