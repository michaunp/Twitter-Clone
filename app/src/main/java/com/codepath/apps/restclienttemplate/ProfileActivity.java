package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    String screenName;
    Long user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //find toolbar inside activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        //sets toolbar to act as the actionbar
        setSupportActionBar(toolbar);
        screenName = getIntent().getStringExtra("screen_name");
        user_id = getIntent().getLongExtra("user_id", 0);
        Log.i("user_headline_vals", String.valueOf(user_id));
        Log.i("user_headline_vals", String.valueOf(screenName));
        //create the user fragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
        //display user timeline fragment in container (dynamically)
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //make change
        ft.replace(R.id.flContainer, userTimelineFragment);
        //commit transaction
        ft.commit();



        client = TwitterApp.getRestClient();
        if (screenName == null || user_id == null) {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //deserialize the User object
                    try {
                        User user = User.fromJSON(response);
                        //set title of action bar based on user info
                        getSupportActionBar().setTitle(user.screenName);
                        //populate user headline
                        PopulateUserHeadline(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            client.getNewUserInfo(user_id, screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //deserialize the User object
                    try {
                        User user = User.fromJSON(response);
                        //set title of action bar based on user info
                        getSupportActionBar().setTitle(user.screenName);
                        //populate user headline
                        PopulateUserHeadline(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public void PopulateUserHeadline(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        //bind views to data
        tvName.setText(user.name);
        tvTagline.setText(user.tagLine);
        tvFollowers.setText(user.followersCount + "Followers");
        tvFollowing.setText(user.followingCount + "Following");

        //load profile image
        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu, adds items to action bar if present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }
}
