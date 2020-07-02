package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Entities;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;

    ImageView ivProfileImage;
//    TweetsAdapter.LinkifiedTextView tvBody;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvTimestamp;
    ImageView ivMediaImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_tweet);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        ivMediaImage = findViewById(R.id.ivMediaImage);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
//        User user = (User) tweet.user;
//        Entities media = (Entities) tweet.media;

        Glide.with(getApplicationContext()).load(tweet.user.profileImageUrl).into(ivProfileImage);

        /*if (!tweet.media.mediaUrl.equals("")) {
            Glide.with(getApplicationContext()).load(tweet.media.mediaUrl).placeholder(R.drawable.ic_launcher).into(ivMediaImage);
        }*/

        if(tweet.mediaUrl != null){
            Glide.with(getApplicationContext())
                    .load(tweet.mediaUrl)
                    .into(ivMediaImage);
            ivMediaImage.setVisibility(View.VISIBLE);
        }
        else {
            ivMediaImage.setVisibility(View.GONE);
        }


        //Log.i("tweeeeet", tweet.media.mediaUrl);

        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screenName);
        tvTimestamp.setText(tweet.relativeTimeAgo);

    }
}
