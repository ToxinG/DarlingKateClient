package ru.luvas.dk.client.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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

    private final String TAG = "NewsRecyclerAdapter";

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


    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            conn.disconnect();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final News news = newsList.get(position);
        if (news.newsImagePath != null) {
           // holder.newsImageView.setImageBitmap(getImageBitmap(news.newsImagePath));
            Glide.with(context)
                    .load(news.newsImagePath)
                    //.transform(new CircleTransform(context))
                    .into(holder.newsImageView);
        } else {
            // make sure Glide doesn't load anything into this view until told otherwise
            Glide.clear(holder.newsImageView);
            holder.newsImageView.setImageDrawable(null);
        }


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
