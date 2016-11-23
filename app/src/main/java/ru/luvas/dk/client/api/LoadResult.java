package ru.luvas.dk.client.api;

/**
 * Created by RinesThaix on 23.11.16.
 */

public class LoadResult<T> {

    private final ResultType result;

    private final T data;

    public LoadResult(ResultType result) {
        this(result, null);
    }

    public LoadResult(ResultType result, T data) {
        this.result = result;
        this.data = data;
    }

    public ResultType getResultType() {
        return result;
    }

    public T getData() {
        return data;
    }


    public enum ResultType {
        OK, FAILURE, NO_INTERNET;
    }
}
