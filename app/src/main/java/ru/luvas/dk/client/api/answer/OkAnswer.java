package ru.luvas.dk.client.api.answer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by RinesThaix on 23.11.16.
 */

public class OkAnswer extends Answer {

    @NonNull
    private final String speech;

    @NonNull
    private final String message;

    @Nullable
    private final String photoUrl;

    public OkAnswer(String speech, String message) {
        this(speech, message, null);
    }

    public OkAnswer(String speech, String message, String photoUrl) {
        this.speech = speech;
        this.message = message;
        this.photoUrl = photoUrl;
    }

    public String getSpeech() {
        return speech;
    }

    public String getMessage() {
        return message;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
