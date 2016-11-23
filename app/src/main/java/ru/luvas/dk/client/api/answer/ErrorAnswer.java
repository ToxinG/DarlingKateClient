package ru.luvas.dk.client.api.answer;

import android.support.annotation.NonNull;

/**
 * Created by RinesThaix on 23.11.16.
 */

public class ErrorAnswer extends Answer {

    @NonNull
    private final int id;

    @NonNull
    private final String text;

    public ErrorAnswer(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

}
