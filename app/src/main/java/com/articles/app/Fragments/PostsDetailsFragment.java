package com.articles.app.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.articles.app.Models.PostInfo;
import com.articles.app.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.content_title)
    TextView content_title;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.author_profile)
    RoundedImageView author_profile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.content_image)
    ImageView content_image;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.content) TextView content;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.date_published) TextView date_published;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.article_author) TextView article_author;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.author_email) TextView author_email;

    // TODO: Rename and change types of parameters
    private PostInfo mPostInfo;

    public PostsDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment PostsDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostsDetailsFragment newInstance(PostInfo postInfo) {
        PostsDetailsFragment fragment = new PostsDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("articles", Parcels.wrap(postInfo));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mPostInfo = Parcels.unwrap(getArguments().getParcelable("articles"));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_posts_details, container, false);
        ButterKnife.bind(this, view);
        content_title.setText(mPostInfo.getTitle());
        Picasso.get().load(mPostInfo.getAuthorPhotoUrl()).into(author_profile);
        Picasso.get().load(mPostInfo.getContentImageURL()).into(content_image);
        article_author.setText(mPostInfo.getAuthorName());
        content.setText(mPostInfo.getContent());
//        date_published.setText(mPostInfo.getCreationDate());
        author_email.setText(getString(R.string.contact) + mPostInfo.getAuthorEmail());
        return view;
    }
}