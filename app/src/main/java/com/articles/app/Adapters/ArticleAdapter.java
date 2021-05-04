package com.articles.app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.articles.app.ArticleDetails;
import com.articles.app.Models.Datum;
import com.articles.app.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Datum> mArticles;
    private Context mContext;

    public ArticleAdapter(Context context, List<Datum> articles) {
        mContext = context;
        mArticles = articles;
    }

    @NonNull
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_explore, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ViewHolder holder, int position) {
        holder.bindArticles(mArticles.get(position));
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.content_title) TextView content_title;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.author_profile)
        RoundedImageView author_profile;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.content) TextView content;
        @SuppressLint("NonConstantResourceId")
//        @BindView(R.id.date_published) TextView date_published;
        @BindView(R.id.article_author) TextView article_author;

        private Context mContext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bindArticles(Datum datum) {
            content_title.setText(datum.getTitle());
            Picasso.get().load(datum.getAuthorPhoto()).into(author_profile);
            content.setText(datum.getContent());
//            date_published.setText(articles.getCreationDate());
            article_author.setText(datum.getAuthorName());
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, ArticleDetails.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("articles", Parcels.wrap(mArticles));
            mContext.startActivity(intent);
        }
    }
}
