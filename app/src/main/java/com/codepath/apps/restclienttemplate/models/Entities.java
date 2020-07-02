package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Entities {

    public String mediaUrl;

    public static Entities fromJson (JSONObject jsonObject) throws JSONException {
        Entities entity = new Entities();

        if (jsonObject.has("media")) {
            entity.mediaUrl = jsonObject.getJSONArray("media").getJSONObject(0).getString("media_url_https");

        }
        else {
            entity.mediaUrl = "";
        }
        return entity;

    }
}
