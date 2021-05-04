package com.articles.app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.articles.app.Models.PostInfo;
import com.articles.app.R;
import com.articles.app.interfaces.Display_interface;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context context;
    ArrayList<PostInfo> list;

    Display_interface d;


    public CustomAdapter(Context context, ArrayList<PostInfo> list, Display_interface display_interface) {
        this.d = display_interface;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_adapter,parent,false);
        return  new CustomAdapter.MyViewHolder(v, d);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindArticles(list.get(position));

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Display_interface display_;

        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.card_title)
        TextView content_title;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.author_image)
        RoundedImageView author_profile;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.main_content) TextView content;
        @SuppressLint("NonConstantResourceId")
//        @BindView(R.id.date_published) TextView date_published;
        @BindView(R.id.mentions) TextView article_author;


//        TextView content, title, authorName, authorPhotoUrl;

        private Context mContext;

        public MyViewHolder(@NonNull View itemView, Display_interface display_interface) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            this.display_ = display_interface;


        }

        public void bindArticles(PostInfo postInfo) {
            content_title.setText(postInfo.getTitle());
            Picasso.get().load(postInfo.getAuthorPhotoUrl()).into(author_profile);
            content.setText(postInfo.getContent());
//            date_published.setText(articles.getCreationDate());
            article_author.setText(postInfo.getAuthorName());
        }

        @Override
        public void onClick(View v) {
            display_.onVersionItemClicked(getAdapterPosition());

        }
    }

}
