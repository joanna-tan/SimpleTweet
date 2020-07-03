package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 20;

    Tweet tweet;

    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvTimestamp;
    ImageView ivMediaImage;
    ImageView ivReply;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_tweet);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        ivMediaImage = findViewById(R.id.ivMediaImage);
        ivReply = findViewById(R.id.ivReply);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        Glide.with(getApplicationContext()).load(tweet.user.profileImageUrl).into(ivProfileImage);

        if(tweet.mediaUrl != null){
            Glide.with(getApplicationContext())
                    .load(tweet.mediaUrl)
                    .into(ivMediaImage);
            ivMediaImage.setVisibility(View.VISIBLE);
        }
        else {
            ivMediaImage.setVisibility(View.GONE);
        }

        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screenName);
        tvTimestamp.setText(tweet.relativeTimeAgo);

        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ComposeActivity.class);
                intent.putExtra("replyTo", Parcels.wrap(tweet));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }
}
