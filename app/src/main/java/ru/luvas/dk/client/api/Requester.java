package ru.luvas.dk.client.api;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import ru.luvas.dk.client.api.answer.Answer;
import ru.luvas.dk.client.api.answer.ErrorAnswer;
import ru.luvas.dk.client.api.answer.OkAnswer;

/**
 * Created by RinesThaix on 23.11.16.
 */

public class Requester {

    private final static String BASE_URL = "https://kate.kostya.sexy/";

    private static InputStream execute(String link) throws IOException {
        String[] spl = link.split("\\?");
        String urlClean = spl[0], params = spl[1];
        URL url = new URL(urlClean);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        byte[] out = params.getBytes("UTF-8");
        int length = out.length;
        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.connect();
        OutputStream os = http.getOutputStream();
        os.write(out);
        os.close();
        return http.getInputStream();
    }

    private static String process(String url) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(execute(url), "UTF-8"));
        String line = reader.readLine();
        reader.close();
        return line;
    }

    /**
     * Возвращает LoadResult ответа по заданным параметрам.
     * При этом ResultType будет OK в случае, если мы смогли получить и распарсить любой JSON, как ответ серверного API
     * (в том числе и в случае, когда серверный API вернул ошибку: в данном случае ResultType будет OK,
     * но вместо OkAnswer будет получен ErrorAnswer).
     * @param params параметры запроса к серверному API.
     * @return результат обращения к серверному апи.
     */
    public static LoadResult<? extends Answer> process(Params params) {
        try {
            JSONObject json = new JSONObject(process(BASE_URL + params.toString()));
            if(json.has("error"))
                return new LoadResult<>(LoadResult.ResultType.OK, new ErrorAnswer(json.getInt("id"), json.getString("text")));
            OkAnswer answer;
            String speech = json.getString("speech"), msg = json.getString("message");
            if(json.has("photo"))
                answer = new OkAnswer(speech, msg, json.getString("photo"));
            else
                answer = new OkAnswer(speech, msg);
            return new LoadResult<>(LoadResult.ResultType.OK, answer);
        }catch(Exception ex) {
            ex.printStackTrace();
            return new LoadResult<>(ex instanceof UnknownHostException ?
                    LoadResult.ResultType.NO_INTERNET :LoadResult.ResultType.FAILURE);
        }
    }

}
