package ru.luvas.dk.client;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.HashMap;
import java.util.Locale;

import ru.luvas.dk.client.api.LoadResult;
import ru.luvas.dk.client.api.Params;
import ru.luvas.dk.client.api.answer.Answer;
import ru.luvas.dk.client.api.answer.ErrorAnswer;
import ru.luvas.dk.client.api.answer.NewsAnswer;
import ru.luvas.dk.client.api.answer.NotifyAnswer;
import ru.luvas.dk.client.api.answer.SpeakAnswer;
import ru.luvas.dk.client.utils.DroidLoader;
import ru.luvas.dk.client.utils.Muter;
import ru.luvas.dk.client.utils.Recognizer;

public class TheOnlyActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<? extends Answer>>, TextToSpeech.OnInitListener {

    public final static String TAG = "DarlingKate";
    public static TheOnlyActivity lastInstance;

    private final Handler handler = new Handler();

    private TextView textView;
    private ProgressBar progressBar;
    private Button microView;
    private ImageView imageView;

    private TextToSpeech tts;
    public Muter muter;
    private Recognizer recognizer;

    private String recognizedText = "привет";
    private String message = "", photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastInstance = this;
        setContentView(R.layout.main);

        // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        // StrictMode.setThreadPolicy(policy);

        textView = (TextView) findViewById(R.id.text_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        microView = (Button) findViewById(R.id.micro_button);
        imageView = (ImageView) findViewById(R.id.image_view);
        microView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView.getVisibility() == View.VISIBLE) {
                    imageView.setVisibility(View.GONE);
                    Glide.clear(imageView);
                    imageView.setImageDrawable(null);
                }
                recognizer.startListening();
            }
        });

        muter = new Muter(this);
        recognizer = new Recognizer(this);

        tts = new TextToSpeech(this, this);

        setDisplayState("Нажми и скажи что-нибудь (;", null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            Locale locale = new Locale("ru");

            int result = tts.setLanguage(locale);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                setErrorState("Распознавание русского языка не поддерживается на Вашем устройстве.");
                return;
            }

            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {
                }

                @Override
                public void onDone(String s) {
                    if (s.equals(TAG)) {
                    }
                    //post(1000l, new Runnable() {
                    //   @Override
                    //  public void run() {
                    //     recognizer.startListening();
                    //}
                    //});
                }

                @Override
                public void onError(String s) {

                }
            });

        }

    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        recognizer.destroy();
        muter.unmute();
        super.onDestroy();
    }

    public void handleInput(String text) {
        recognizedText = text;
        if (recognizedText.equals("карта") || recognizedText.equals("карты") || recognizedText.equals("Покажи карту")) {
            Intent intent = new Intent(TheOnlyActivity.this, MapsActivity.class);
            setDisplayState("Нажми и скажи что-нибудь (;", null);
            startActivity(intent);
        } else
            getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", message);
        if (photo != null)
            outState.putString("photo", photo);
    }

    @Override
    public Loader<LoadResult<? extends Answer>> onCreateLoader(int id, Bundle args) {
        return new DroidLoader(this, new Params("token", 0, "message", recognizedText));
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<? extends Answer>> loader, LoadResult<? extends Answer> data) {
        switch (data.getResultType()) {
            case OK: {
                Answer answer = data.getData();
                if (answer instanceof ErrorAnswer) {
                    setErrorState(((ErrorAnswer) answer).getText());
                } else if (answer instanceof SpeakAnswer) {
                    SpeakAnswer sa = (SpeakAnswer) answer;
                    Toast.makeText(this, recognizedText, Toast.LENGTH_SHORT).show();
                    setDisplayState(sa.getMessage(), sa.getPhotoUrl());
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, TAG);
                    tts.speak(sa.getSpeech(), TextToSpeech.QUEUE_FLUSH, params);
                } else if (answer instanceof NotifyAnswer) {
                    NotifyAnswer na = (NotifyAnswer) answer;
                    Toast.makeText(this, na.getMessage(), Toast.LENGTH_SHORT).show();
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, TAG);
                    tts.speak(na.getSpeech(), TextToSpeech.QUEUE_FLUSH, params);
                } else if (answer instanceof NewsAnswer) {
                    NewsAnswer na = (NewsAnswer) answer;

                    Log.d(TAG, "Show " + na.getNewsList().size() + " news");

                    final Intent newsFeedIntent = NewsFeedActivity.createIntent(this, na.getNewsList());
                    setDisplayState("Нажми и скажи что-нибудь (;", null);
                    startActivity(newsFeedIntent);
                }
                break;
            }
            case NO_INTERNET:
                setErrorState("Что-то не так с Вашим интернет-соединением.");
                break;
            case FAILURE:
                setErrorState("Произошла непредвиденная ошибка. К сожалению, Вы ничего не можете поделать с этим );");
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<? extends Answer>> loader) {

    }

    public void setListeningState() {
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public void setWaitingState() {
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setDisplayState(String message, String url) {
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(message);
        if (url != null) {
            Glide.with(this)
                    .load(url)
                    .into(imageView);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void setErrorState(String message) {
        setDisplayState("Ошибка!", null);
        Toast.makeText(this, "Ошибка: " + message, Toast.LENGTH_LONG).show();
    }

    private void post(long delay, Runnable task) {
        handler.postDelayed(task, delay);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Recognizer.PERMISSIONS_REQUEST_ID:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    recognizer.preload(this);
                else
                    recognizer.askForPermissions(this);
        }
    }


}