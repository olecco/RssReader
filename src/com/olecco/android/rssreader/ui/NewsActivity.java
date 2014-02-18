package com.olecco.android.rssreader.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.olecco.android.rssreader.R;

/**
 * Created by olecco on 17.02.14.
 */
public class NewsActivity extends ActionBarActivity {

    public final static String RSS_URL_EXTRA = "RSS_URL_EXTRA";

    private NewsFragment mNewsFragment;
    private String mRssUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        mNewsFragment = (NewsFragment) getSupportFragmentManager().findFragmentById(R.id.newsFragment);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            mRssUrl = args.getString(RSS_URL_EXTRA);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mNewsFragment.loadNews(mRssUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
