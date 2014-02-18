package com.olecco.android.rssreader.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.olecco.android.rssreader.R;
import com.olecco.android.rssreader.model.RssFeed;

import java.util.List;

public class StoreHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rssreader.db";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;

    public StoreHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] defaultFeeds = mContext.getResources().getStringArray(R.array.default_feeds);
        FeedsTable.onCreate(db, defaultFeeds);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] defaultFeeds = mContext.getResources().getStringArray(R.array.default_feeds);
        FeedsTable.onUpgrade(db, oldVersion, newVersion, defaultFeeds);
    }

    public List<RssFeed> getFeeds() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            return FeedsTable.getFeeds(db);
        }
        finally {
            db.close();
        }
    }

    public void storeFeed(RssFeed feed) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            FeedsTable.insertFeed(db, values, feed);
        }
        finally {
            db.close();
        }
    }

    public void deleteFeed(RssFeed feed) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            FeedsTable.deleteFeed(db, feed);
        }
        finally {
            db.close();
        }
    }

}
