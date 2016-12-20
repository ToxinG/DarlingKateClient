package ru.luvas.dk.client.api.answer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.luvas.dk.client.model.News;

/**
 * Created by RinesThaix on 19.12.16.
 */

public class AnswerResolver {

    public static Answer getAnswer(JSONObject answer) {
        try {
            String type = answer.getString("result_type");
            switch(type) {
                case "ERROR":
                    return new ErrorAnswer(answer.getInt("error_id"), answer.getString("error_text"));
                case "SPEAK":
                    if(answer.has("photo"))
                        return new SpeakAnswer(answer.getString("speech"), answer.getString("text"), answer.getString("photo"));
                    return new SpeakAnswer(answer.getString("speech"), answer.getString("text"));
                case "NOTIFY":
                    return new NotifyAnswer(answer.getString("speech"), answer.getString("text"));
                case "NEWS": {
                    List<News> newsList = new ArrayList<>();
                    JSONArray array = answer.getJSONArray("news");
                    for(int i = 0; i < array.length(); ++i) {
                        JSONObject json = array.getJSONObject(i);
                        String photoUrl = json.has("photo_url") ? json.getString("photo_url") : null;
                        newsList.add(new News(photoUrl, json.getString("news_url"), json.getString("title"), json.getString("full_text")));
                    }
                    return new NewsAnswer(newsList);
                }
                default:
                    return null;
            }
        }catch(Exception ex) {
            return null;
        }
    }

}
