package ru.luvas.dk.client.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.ref.WeakReference;

import ru.luvas.dk.client.TheOnlyActivity;

/**
 * Created by RinesThaix on 23.11.16.
 */

public class Recognizer {

    public final static int PERMISSIONS_REQUEST_ID = 7661;

    protected AudioManager aManager;
    protected SpeechRecognizer sr;
    protected Intent srIntent;
    protected final Messenger messenger = new Messenger(new IncomingHandler(this));

    protected boolean listening;
    protected volatile boolean countDownOn;

    static final int FLAG_START_LISTENING = 1;
    static final int FLAG_STOP_LISTENING = 2;

    public Recognizer(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            askForPermissions(activity);
        }else
            preload(activity);
    }

    public void askForPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.RECORD_AUDIO},
                PERMISSIONS_REQUEST_ID);
    }

    public void preload(Activity activity) {
        aManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        sr = SpeechRecognizer.createSpeechRecognizer(activity);
        sr.setRecognitionListener(new SpeechRecognitionListener());
        srIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        srIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity.getPackageName());
    }

    public void startListening() {
        if(listening) {
            stopListening();
        }
        try {
            messenger.send(Message.obtain(null, FLAG_START_LISTENING));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        TheOnlyActivity.lastInstance.setListeningState();
    }

    public void stopListening() {
        try {
            messenger.send(Message.obtain(null, FLAG_STOP_LISTENING));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class IncomingHandler extends Handler {

        private WeakReference<Recognizer> recognizer;

        IncomingHandler(Recognizer target) {
            recognizer = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                final Recognizer target = recognizer.get();
                switch (msg.what) {
                    case FLAG_START_LISTENING:
                        if(!target.listening) {
                            TheOnlyActivity.lastInstance.muter.mute();
                            target.sr.startListening(target.srIntent);
                            target.listening = true;
                        }else
                            Log.d(TheOnlyActivity.TAG, "Already listening!");
                        break;
                    case FLAG_STOP_LISTENING:
                        target.sr.cancel();
                        target.listening = false;
                        break;
                }
            }catch(Exception ex) {
                ex.printStackTrace();
                Log.d(TheOnlyActivity.TAG, "There's an exception!");
            }
        }
    }

    // Count down timer for Jelly Bean work around
    private CountDownTimer countDown = new CountDownTimer(5000, 5000) {

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onFinish() {
            countDownOn = false;
            Message message = Message.obtain(null, FLAG_STOP_LISTENING);
            try {
                messenger.send(message);
                message = Message.obtain(null, FLAG_START_LISTENING);
                messenger.send(message);
            }catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    public void destroy() {
        if(countDownOn)
            countDown.cancel();
        if(sr != null)
            sr.destroy();
    }

    private class SpeechRecognitionListener implements RecognitionListener {

        @Override
        public void onBeginningOfSpeech() {
            if(countDownOn) {
                countDownOn = false;
                countDown.cancel();
            }
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TheOnlyActivity.TAG, "Ended listening");
            TheOnlyActivity.lastInstance.setWaitingState();
        }

        @Override
        public void onError(int error) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d(TheOnlyActivity.TAG, "Starting listening");
            countDownOn = true;
            countDown.start();
        }

        @Override
        public void onResults(Bundle results) {
            TheOnlyActivity.lastInstance.muter.unmute();
            String text = results.getStringArrayList("results_recognition").get(0);
            TheOnlyActivity.lastInstance.handleInput(text);
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

    }
}