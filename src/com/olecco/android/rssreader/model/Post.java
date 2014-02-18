package com.olecco.android.rssreader.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by olecco on 17.02.14.
 */
public class Post implements Parcelable {

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String mLink) {
        this.mLink = mLink;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public void setPubDate(String mPubDate) {
        this.mPubDate = mPubDate;
    }

    private String mTitle;
    private String mLink;
    private String mDescription;
    private String mPubDate;

    public Post() { }

    public Post(Parcel parcel) {
        mTitle = parcel.readString();
        mLink = parcel.readString();
        mDescription = parcel.readString();
        mPubDate = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mLink);
        dest.writeString(mDescription);
        dest.writeString(mPubDate);
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
