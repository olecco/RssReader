package com.olecco.android.rssreader.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.olecco.android.rssreader.R;

public class MainActivity extends ActionBarActivity {

    private FeedsFragment mFeedsFragment;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getSupportActionBar().setTitle(R.string.app_name);

        mFeedsFragment = (FeedsFragment) getSupportFragmentManager().findFragmentById(R.id.feedsFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFeedsFragment.loadFeeds();
    }
}
