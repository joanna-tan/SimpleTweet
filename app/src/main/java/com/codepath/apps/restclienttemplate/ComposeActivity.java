package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = "ComposeActivity";
    EditText etCompose;
    Button btnTweet;
    TextView tvCharsRemaining;
    boolean isReply;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = findViewById(R.id.etCompose);
        final Resources res = getResources();

        final Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("replyTo"));
        if (tweet != null) {
            etCompose.setText(String.format(res.getString(R.string.reply_to), String.format("%s ", tweet.user.screenName)));
            isReply = true;
        }

        client = TwitterApp.getRestClient(this);

        btnTweet = findViewById(R.id.btnTweet);
        tvCharsRemaining = findViewById(R.id.tvCharsRemaining);


        tvCharsRemaining.setText(String.format(res.getString(R.string.chars_remaining), MAX_TWEET_LENGTH));

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int charsRem = MAX_TWEET_LENGTH - s.toString().length();
                tvCharsRemaining.setText(String.format(res.getString(R.string.chars_remaining), charsRem));
            }
        });

        // add click listener to tweet button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();

                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(ComposeActivity.this, "Published tweet!", Toast.LENGTH_SHORT).show();

                if (isReply) { //call reply_tweet
                    assert tweet != null;
                    client.replyTweet(tweet.id, tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.e(TAG, "onSuccess to reply to tweet");
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                Log.i(TAG, "Published tweet says: " + tweet.body);
                                //Intent intent = new Intent();

                                Intent intent = new Intent(getApplicationContext(), TimelineActivity.class);

                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                //set result code and bundle data for response
                                setResult(RESULT_OK, intent);
                                // closes the activity , pass data to parent
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "onFailure to reply to tweet", throwable);

                        }
                    });

                }

                else {

                // Make an API call to Twitter to publish the tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.e(TAG, "onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says: " + tweet.body);

                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            //set result code and bundle data for response
                            setResult(RESULT_OK, intent);
                            // closes the activity , pass data to parent
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);
                    }
                }); }

            }
        });

    }
}