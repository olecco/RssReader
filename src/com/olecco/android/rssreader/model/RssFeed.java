package com.olecco.android.rssreader.model;

/**
 * Created by olecco on 17.02.14.
 */
public class RssFeed {

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getUrl() {
        return mLink;
    }

    public void setUrl(String mLink) {
        this.mLink = mLink;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RssFeed)) {
            return false;
        }
//        if (!mTitle.equals(((RssFeed) o).getTitle())) {
//            return false;
//        }
        if (!mLink.equals(((RssFeed) o).getUrl())) {
            return false;
        }
        return true;
    }

    private String mTitle;
    private String mLink;

}
