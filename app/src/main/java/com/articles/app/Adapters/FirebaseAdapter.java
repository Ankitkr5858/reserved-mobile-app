package com.articles.app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.articles.app.Models.PostInfo;
import com.articles.app.R;
import com.articles.app.ContentListDetails;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseAdapter extends RecyclerView.Adapter<FirebaseAdapter.MyViewHolder> {

    private Context mContext;

    private ArrayList<PostInfo> mList;


    public FirebaseAdapter(Context context, ArrayList<PostInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public FirebaseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindArticles(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.firebase_content_title) TextView content_title;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.author_profile)
        RoundedImageView author_profile;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.content) TextView content;
        @SuppressLint("NonConstantResourceId")
//        @BindView(R.id.date_published) TextView date_published;
        @BindView(R.id.article_author) TextView article_author;


//        TextView content, title, authorName, authorPhotoUrl;

        private Context mContext;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);

        }

        public void bindArticles(PostInfo postInfo) {
            content_title.setText(postInfo.getTitle());
            Picasso.get().load(postInfo.getAuthorPhotoUrl()).into(author_profile);
            content.setText(postInfo.getContent());
//            date_published.setText(articles.getCreationDate());
            article_author.setText(postInfo.getAuthorName());
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, ContentListDetails.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("articles", Parcels.wrap(mList));
            mContext.startActivity(intent);
        }
    }

}
