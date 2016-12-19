package ru.luvas.dk.client.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ru.luvas.dk.client.R;
import ru.luvas.dk.client.model.News;


/**
 * Created by Антон on 17.12.2016.
 */

public class NewsRecyclerAdapter
        extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;

    private List<News> newsList = Collections.emptyList();

    public NewsRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return NewsViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final News news = newsList.get(position);
        holder.newsImageView.setImageURI(Uri.parse(news.newsImagePath));
        holder.newsTitleView.setText(news.newsTitle);
        holder.newsArticleView.setText(news.newsArticle);
        holder.newsArticleView.setVisibility(View.GONE);
        holder.newsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.findViewById(R.id.news_article).getVisibility() == View.GONE)
                    v.findViewById(R.id.news_article).setVisibility(View.VISIBLE);
                else
                    v.findViewById(R.id.news_article).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        final ImageView newsImageView;
        final TextView newsTitleView;
        final TextView newsArticleView;
        final RelativeLayout newsItem;

        private NewsViewHolder(View itemView) {
            super(itemView);
            newsImageView = (ImageView) itemView.findViewById(R.id.news_image);
            newsTitleView = (TextView) itemView.findViewById(R.id.news_title);
            newsArticleView = (TextView) itemView.findViewById(R.id.news_article);
            newsItem = (RelativeLayout) itemView.findViewById(R.id.news_item);
        }

        static NewsViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_news, parent, false);
            return new NewsViewHolder(view);
        }
    }
}
