package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }
    // Pass in the context and list of tweets
    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        //wrap the view in the ViewHolder class defined below
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);

        //Bind the tweet with ViewHolder holder (passed in)
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }


    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    // Define a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        LinkifyTextView tvBody;
        TextView tvScreenName;
        TextView tvTimestamp;
        ImageView ivTweetMedia;
        ImageView ivReply;

        RelativeLayout content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivReply = itemView.findViewById(R.id.ivReply);

            ivTweetMedia = itemView.findViewById(R.id.ivTweetMedia);

            content = itemView.findViewById(R.id.content);
        }

        public void bind(final Tweet tweet) {
            tvBody.setText(tweet.body);

            tvScreenName.setText(tweet.user.screenName);
            tvTimestamp.setText(tweet.relativeTimeAgo);
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);

            if (tweet.mediaUrl != null) {
                Glide.with(context)
                        .load(tweet.mediaUrl)
                        .into(ivTweetMedia);
                ivTweetMedia.setVisibility(View.VISIBLE);
            } else {
                ivTweetMedia.setVisibility(View.GONE);
            }

            ivReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ComposeActivity.class);
                    i.putExtra("replyTo", Parcels.wrap(tweet));
                    context.startActivity(i);
                }
            });

            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, TweetDetailsActivity.class);
                    i.putExtra("tweet", Parcels.wrap(tweet));
                    context.startActivity(i);
                }
            });

        }

    }

}