package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by michaunp on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    private Context context;
    private TwitterClient client;
    //pass in the tweets array into the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }
    //for each row inflate layout and pass into ViewHolder class

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetview = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetview);

        client = TwitterApp.getRestClient();
        return viewHolder;
    }

    //bind valuesbased on position of element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //get data according to position
        final Tweet tweet = mTweets.get(position);


        if (tweet.favorited) {
            holder.ivFavorite.setBackgroundResource(R.drawable.ic_favorited);
        }
        else {
            holder.ivFavorite.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
        }

        if (tweet.retweeted) {
            holder.ivReweet.setBackgroundResource(R.drawable.ic_retweeted);
        }
        else {
            holder.ivReweet.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
        }
        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvScreenname.setText("@" + tweet.user.screenName);
        holder.tvBody.setText(tweet.body);
        holder.tvTimestamp.setText(TimeFormatter.getTimeDifference(tweet.createdAt));
        holder.count_retweet.setText(String.valueOf(tweet.retweet_count));
        holder.count_favorite.setText(String.valueOf(tweet.favorite_count));
        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
        holder.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ComposeActivity.class);
                i.putExtra("reply_id", tweet.uid);
                i.putExtra("screen_name", tweet.user.screenName);
                context.startActivity(i);
            }
        });
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("screen_name", tweet.user.screenName);
                i.putExtra("user_id", tweet.user.uid);
                context.startActivity(i);
            }
        });
        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweet.favorited) {
                    client.likeTweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            tweet.favorite_count = tweet.favorite_count + 1;
                            holder.ivFavorite.setBackgroundResource(R.drawable.ic_favorited);
                            holder.count_favorite.setText(String.valueOf(tweet.favorite_count));
                            tweet.favorited = true;
                            notifyItemChanged(position);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Log.d("debug_fail", responseString);
                        }
                    });
                }

                else {

                    client.unlikeTweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            tweet.favorite_count = tweet.favorite_count - 1;
                            holder.ivFavorite.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
                            holder.count_favorite.setText(String.valueOf(tweet.favorite_count));
                            tweet.favorited = false;
                            notifyItemChanged(position);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Log.d("debug_fail", responseString);
                        }
                    });
                }
            }
        });

        holder.ivReweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweet.retweeted) {
                    client.Retweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            tweet.retweet_count = tweet.retweet_count + 1;
                            holder.ivReweet.setBackgroundResource(R.drawable.ic_retweeted);
                            holder.count_retweet.setText(String.valueOf(tweet.retweet_count));
                            tweet.retweeted = true;
                            notifyItemChanged(position);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Log.d("debug_fail", responseString);
                        }
                    });
                }
                else {
                    client.unRetweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            tweet.retweet_count = tweet.retweet_count - 1;
                            holder.ivReweet.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
                            holder.count_retweet.setText(String.valueOf(tweet.retweet_count));
                            tweet.retweeted = false;
                            notifyItemChanged(position);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Log.d("debug_fail", responseString);
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

// Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
    //create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public ImageView ivReply;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimestamp;
        public TextView tvScreenname;
        public ImageView ivReweet;
        public TextView count_retweet;
        public ImageView ivFavorite;
        public TextView count_favorite;
        public Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            //perform viewbyid lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            tvScreenname = (TextView) itemView.findViewById(R.id.tvScreenname);
            ivReweet = (ImageView) itemView.findViewById(R.id.ivRetweet);
            count_retweet = (TextView) itemView.findViewById(R.id.count_retweet);
            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            count_favorite = (TextView) itemView.findViewById(R.id.count_favorite);
            ivReply = (ImageView) itemView.findViewById(R.id.ivReply);
            context = itemView.getContext();


        }
    }
}
