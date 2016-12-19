package ru.luvas.dk.client.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Антон on 17.12.2016.
 */

public class News {

    public final @Nullable String newsImagePath;

    public final @NonNull String newsUrl;

    public final @NonNull String newsTitle;

    public final @NonNull String newsArticle;

    public News(String newsImagePath,
                String newsUrl,
                String newsTitle,
                String newsArticle) {
        this.newsImagePath = newsImagePath;
        this.newsUrl = newsUrl;
        this.newsTitle = newsTitle;
        this.newsArticle = newsArticle;
    }
}
