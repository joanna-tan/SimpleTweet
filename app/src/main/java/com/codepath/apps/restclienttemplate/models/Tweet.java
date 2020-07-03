package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String relativeTimeAgo;
    public String mediaUrl;
    public long id;

    //build the tweet from the fields in the json object
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("full_text");
        tweet.createdAt = jsonObject.getString("created_at");
        //user needs to convert json object to a User.java object
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");
        //tweet.media = Entities.fromJson(jsonObject.getJSONObject("entities"));
        tweet.relativeTimeAgo = getRelativeTimeAgo(tweet.createdAt);

        if(jsonObject.has("extended_entities")){
            String type = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("type");
            if(type.equals("photo")){
                tweet.mediaUrl = jsonObject.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0).getString("media_url_https");
            }
        }

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return tweets;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
