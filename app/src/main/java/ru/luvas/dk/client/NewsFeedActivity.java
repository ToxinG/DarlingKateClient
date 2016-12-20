package ru.luvas.dk.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.luvas.dk.client.model.News;
import ru.luvas.dk.client.utils.NewsRecyclerAdapter;
import ru.luvas.dk.client.utils.RecylcerDividersDecorator;

/**
 * Created by Антон on 10.12.2016.
 */

public class NewsFeedActivity extends AppCompatActivity {

    private static final String EXTRA_LIST_OF_NEWS = "list_of_news";

    private RecyclerView recyclerView;
    private TextView errorTextView;
    private ProgressBar progressBar;

    private List<News> newsList;

    private static final String TAG = "NewsFeedActivity";

    @Nullable
    private NewsRecyclerAdapter adapter;

    public static Intent createIntent(@NonNull Context context,
                                      @NonNull List<News> listOfNews) {
        final Intent intent = new Intent(context, NewsFeedActivity.class);
        listOfNews = new ArrayList<>();
        intent.putParcelableArrayListExtra(EXTRA_LIST_OF_NEWS, (ArrayList<News>) listOfNews);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        errorTextView = (TextView) findViewById(R.id.error_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(R.color.colorNewsFeedRecyclerDivider)));

        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        newsList = savedInstanceState.getParcelableArrayList(EXTRA_LIST_OF_NEWS);

        if (adapter == null) {
            adapter = new NewsRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setNewsList(newsList);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO ?

    }

    /*TODO
     * Error notification
     */
}