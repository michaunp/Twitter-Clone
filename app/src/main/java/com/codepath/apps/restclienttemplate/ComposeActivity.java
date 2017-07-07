package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    public EditText newTweet;
    public TextView char_count;
    String screenName;
    Long reply_id;
    Button post_tweet;
    Context context;
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
        //get access to post tweet button
        post_tweet = (Button) findViewById(R.id.bt_tweet);
        newTweet = (EditText) findViewById(R.id.et_newtweet);
        //sets up character counter
        char_count = (TextView) findViewById(R.id.tvTextCount);
        //get data from reply intent
        screenName = getIntent().getStringExtra("screen_name");
        reply_id = getIntent().getLongExtra("reply_id", 0);
        //checks if this user is replying and adds desired user's screen name if it's a reply
        if (reply_id != 0) {
            newTweet.setText("@" + screenName);
        }
        else {
            newTweet.setHint("What's happening?");
        }
        //moves cursor to current position after the text
        newTweet.setSelection(newTweet.getText().length());
        //gets live character count for text
        newTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                char_count.setText(String.valueOf(140 - s.length()));
                if ( (140 - s.length()) < 0) {
                    post_tweet.setBackgroundResource(R.color.faded_logo_blue);
                }
                else {
                    post_tweet.setBackgroundResource(R.color.logo_blue);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu, adds items to action bar if present
        getMenuInflater().inflate(R.menu.menu_newtweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miExit:
                // User chose the "Settings" item, show the app settings UI...
                setResult(RESULT_CANCELED);
                finish();

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void postTweet(View v){
        String text = newTweet.getText().toString();
        int num = Integer.valueOf(char_count.getText().toString());
        if (num >= 0) {
            client.sendTweet(reply_id, text, new JsonHttpResponseHandler() {
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
}
