package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class Tweet implements Parcelable {

    //list out attributes

    public String body;
    public long uid; //database for tweet
    public User user;
    public String createdAt;
    public int  retweet_count;
    public boolean retweeted;
    public int favorite_count;
    public boolean favorited;
    public int id;
    public Tweet() {}

    //deserialize the JSON data
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        //extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.retweet_count = jsonObject.getInt("retweet_count");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.favorite_count = jsonObject.getInt("favorite_count");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.id = jsonObject.getInt("id_str");
        return tweet;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.uid);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.createdAt);
        dest.writeInt(this.retweet_count);
        dest.writeByte((byte) (retweeted ? 1:0));
        dest.writeInt(this.favorite_count);
        dest.writeByte((byte) (favorited ? 1:0));
        dest.writeInt(this.id);
    }

    protected Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createdAt = in.readString();
        this.retweet_count = in.readInt();
        this.retweeted = in.readByte() != 0;
        this.favorite_count = in.readInt();
        this.favorited = in.readByte() != 0;
        this.id = in.readInt();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
