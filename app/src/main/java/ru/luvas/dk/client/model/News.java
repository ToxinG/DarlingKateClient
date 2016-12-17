package ru.luvas.dk.client.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Антон on 17.12.2016.
 */

public class News {

    public final @Nullable String newsImagePath; //кстати у этого изображения есть название?

    public final @NonNull String newsTitle;

    public final @NonNull String newsArticle;

    public News(String newsImagePath,
                String newsTitle,
                String newsArticle) {
        //TODO:
        this.newsImagePath = newsImagePath;
        this.newsTitle = newsTitle;
        this.newsArticle = newsArticle;
    }
}
