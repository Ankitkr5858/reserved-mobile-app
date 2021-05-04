package com.articles.app.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.articles.app.Models.Datum;
import com.articles.app.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleDetailsFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleDetailsFragment1 extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.content_title) TextView content_title;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.author_profile) RoundedImageView author_profile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.content_image) ImageView content_image;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.content) TextView content;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.date_published) TextView date_published;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.article_author) TextView article_author;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.author_email) TextView author_email;

    // TODO: Rename and change types of parameters
    private Datum mArticles;

    public ArticleDetailsFragment1() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ArticleDetailsFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleDetailsFragment1 newInstance(Datum articles) {
        ArticleDetailsFragment1 fragment = new ArticleDetailsFragment1();
        Bundle args = new Bundle();
        args.putParcelable("articles", Parcels.wrap(articles));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArticles = Parcels.unwrap(getArguments().getParcelable("articles"));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_article_details1, container, false);
        ButterKnife.bind(this, view);
        content_title.setText(mArticles.getTitle());
        Picasso.get().load(mArticles.getAuthorPhoto()).into(author_profile);
        Picasso.get().load(mArticles.getImage()).into(content_image);
        article_author.setText(mArticles.getAuthorName());
        content.setText(mArticles.getContent());
        date_published.setText(mArticles.getCreationDate());
        author_email.setText(getString(R.string.contact) + mArticles.getAuthorEmail());
        return view;
    }
}