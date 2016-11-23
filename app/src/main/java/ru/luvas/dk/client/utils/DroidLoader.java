package ru.luvas.dk.client.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import ru.luvas.dk.client.api.LoadResult;
import ru.luvas.dk.client.api.Params;
import ru.luvas.dk.client.api.Requester;
import ru.luvas.dk.client.api.answer.Answer;

/**
 * Created by RinesThaix on 23.11.16.
 */

public class DroidLoader extends AsyncTaskLoader<LoadResult<? extends Answer>> {

    private Params params;

    private LoadResult<? extends Answer> lastResult;

    public DroidLoader(Context context, Params params) {
        super(context);
        this.params = params;
    }

    public Params getParams() {
        return params;
    }

    @Override
    public LoadResult<? extends Answer> loadInBackground() {
        return lastResult = Requester.process(params);
    }

    @Override
    protected void onStartLoading() {
        if(lastResult == null || lastResult.getResultType() != LoadResult.ResultType.OK)
            forceLoad();
        else
            deliverResult(lastResult);
    }

}