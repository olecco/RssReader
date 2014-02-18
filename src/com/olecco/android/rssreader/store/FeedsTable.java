package com.olecco.android.rssreader.store;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import com.olecco.android.rssreader.model.RssFeed;
import java.util.ArrayList;
import java.util.List;

public class FeedsTable {

    public final static String TABLE_NAME = "feeds";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_URL = "_URL";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_URL + " text"
            + ");";

    public static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    public static final String SELECT_BY_URL = "SELECT * FROM " + TABLE_NAME + " WHERE " +
            COLUMN_URL + " = ?";

//    public static final String DELETE_BY_URL = "DELETE FROM " + TABLE_NAME + " WHERE " +
//            COLUMN_URL + " = ?";

    public static void onCreate(SQLiteDatabase database, String[] defaultFeeds) {
        database.execSQL(DATABASE_CREATE);
        for (int i = 0; i < defaultFeeds.length; i++) {
            RssFeed feed = new RssFeed();
            feed.setTitle(defaultFeeds[i]);
            feed.setUrl(defaultFeeds[i]);
            ContentValues values = new ContentValues();
            insertSingleFeed(database, TABLE_NAME, values, feed);
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion, String[] defaultFeeds) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database, defaultFeeds);
    }

    public static RssFeed getFeedByURL(SQLiteDatabase db, String url) {
        Cursor cursor = db.rawQuery(SELECT_BY_URL, new String[] { url });
        List<RssFeed> feeds = feedsFromCursor(cursor);
        cursor.close();
        if (!feeds.isEmpty()) {
            return feeds.get(0);
        }
        return null;
    }

    public static RssFeed insertFeed(SQLiteDatabase db, ContentValues values, RssFeed feed){
        RssFeed existingFeed = getFeedByURL(db, feed.getUrl());
        if (existingFeed == null) {
            insertSingleFeed(db, TABLE_NAME, values, feed);
        }
        return existingFeed;
    }

    public static void insertSingleFeed(SQLiteDatabase db, String tableName, ContentValues values, RssFeed feed) {
        values.put(COLUMN_URL, feed.getUrl());
        db.insert(tableName, null, values);
    }

    public static void deleteFeed(SQLiteDatabase db, RssFeed feed) {
        db.delete(TABLE_NAME, COLUMN_URL + "='" + feed.getUrl() + "'", null);
    }

    public static List<RssFeed> feedsFromCursor(Cursor cursor) {
        List<RssFeed> feeds = new ArrayList<RssFeed>();
        if (cursor.moveToFirst()) {
            do {
                RssFeed feed = new RssFeed();
                feed.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
                feed.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
                feeds.add(feed);
            } while (cursor.moveToNext());
        }
        return feeds;
    }

    public static List<RssFeed> getFeeds(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery(SELECT_ALL, new String[] { });
        return feedsFromCursor(cursor);
    }

    public static void clearFeeds(SQLiteDatabase database) {
        database.delete(TABLE_NAME, null, null);
    }

}
