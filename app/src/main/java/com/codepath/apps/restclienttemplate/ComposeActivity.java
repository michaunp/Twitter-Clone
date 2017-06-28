package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    public EditText newTweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient();

        //find toolbar inside activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbnew_tweet);
        //sets toolbar to act as the actionbar
        setSupportActionBar(toolbar);
        //sets up toolbar title
        getSupportActionBar().setTitle(null);
        newTweet = (EditText) findViewById(R.id.et_newtweet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu, adds items to action bar if present
        getMenuInflater().inflate(R.menu.menu_newtweet, menu);
        return true;
    }

    public void postTweet(View v){
        String text = newTweet.getText().toString();
        //Toast.makeText(ComposeActivity.this, text, Toast.LENGTH_SHORT).show();
        client.sendTweet(text, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Tweet new_tweet= null;
                try {
                    new_tweet = Tweet.fromJSON(response);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("debug_success", new_tweet.body.toString());
                Log.d("debug_success", new_tweet.user.name.toString());

                Intent send_data = new Intent();
                send_data.putExtra("new_tweet", new_tweet);

                setResult(RESULT_OK, send_data);
                finish();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("debug_fail", responseString);

            }
        });
    }
}
