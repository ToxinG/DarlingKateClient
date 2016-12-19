package ru.luvas.dk.client.api.answer;

import android.support.annotation.NonNull;

import java.util.List;

import ru.luvas.dk.client.model.News;

/**
 * Created by RinesThaix on 19.12.16.
 */

public class NewsAnswer extends Answer {

    @NonNull
    private final List<News> newsList;

    public NewsAnswer(List<News> newsList) {
        this.newsList = newsList;
    }

    public List<News> getNewsList() {
        return this.newsList;
    }

}
