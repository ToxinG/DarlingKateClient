package ru.luvas.dk.client.api.answer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by RinesThaix on 19.12.16.
 */

public class SpeakAnswer extends Answer {

    @NonNull
    private final String speech;

    @NonNull
    private final String message;

    @Nullable
    private final String photoUrl;

    public SpeakAnswer(String speech, String message) {
        this(speech, message, null);
    }

    public SpeakAnswer(String speech, String message, String photoUrl) {
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
