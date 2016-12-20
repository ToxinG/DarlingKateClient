package ru.luvas.dk.client.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Антон on 17.12.2016.
 */

public class News implements Parcelable {

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

    protected News(Parcel in) {
        newsImagePath = in.readString();
        newsUrl = in.readString();
        newsTitle = in.readString();
        newsArticle = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(newsImagePath);
        dest.writeString(newsUrl);
        dest.writeString(newsTitle);
        dest.writeString(newsArticle);
    }
}