package ru.luvas.dk.client.api.answer;

import android.support.annotation.NonNull;

/**
 * Created by RinesThaix on 19.12.16.
 */

public class NotifyAnswer extends Answer {

    @NonNull
    private final String speech;

    @NonNull
    private final String message;

    public NotifyAnswer(String speech, String message) {
        this.speech = speech;
        this.message = message;
    }

    public String getSpeech() {
        return speech;
    }

    public String getMessage() {
        return message;
    }

}
