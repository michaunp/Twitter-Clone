package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

public class TimelineActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;
    TweetsPagerAdapter pagerAdapter;
    ViewPager vpPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //find toolbar inside activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //sets toolbar to act as the actionbar
        setSupportActionBar(toolbar);
        //sets up toolbar title
        getSupportActionBar().setTitle(null);
        pagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        //get the View pager
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        //set the adapter for the pager
        vpPager.setAdapter(pagerAdapter);
        //setup tablayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu, adds items to action bar if present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miProfile:
                // User chose the "Settings" item, show the app settings UI...
                Intent i = new Intent(this, ProfileActivity.class);
                startActivity(i);
                return true;

            case R.id.miCompose:
                // User chose the "Compose" action, start a new intent to launch compose tweet page
                launchComposeView();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    public void launchComposeView() {
        // first parameter is the context, second is the class of the activity to launch
        Intent compose = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(compose, REQUEST_CODE); // brings up the second activity
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet sent_tweet = (Tweet) data.getParcelableExtra("new_tweet");
            if(pagerAdapter.getItem(vpPager.getCurrentItem())instanceof HomeTimelineFragment) {
                ((HomeTimelineFragment) pagerAdapter.getItem(vpPager.getCurrentItem())).addTweet(sent_tweet);
            }
        }
    }


}
