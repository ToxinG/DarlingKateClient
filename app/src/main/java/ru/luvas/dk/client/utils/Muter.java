package ru.luvas.dk.client.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by RinesThaix on 23.11.16.
 */

public class Muter {

    private final AudioManager aManager;
    private int volume = -1;

    public Muter(Context context) {
        aManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void mute() {
        volume = aManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        aManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
    }

    public void unmute() {
        if(volume == -1)
            return;
        aManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        volume = -1;
    }

}
