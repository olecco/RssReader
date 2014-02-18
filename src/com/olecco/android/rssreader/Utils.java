package com.olecco.android.rssreader;

import java.net.URL;

/**
 * Created by olecco on 18.02.14.
 */
public class Utils {

    public static String prepareUrl(String urlStr) {
        if (urlStr != null && !urlStr.isEmpty()) {
            if (!urlStr.startsWith("http://")) {
                urlStr = "http://" + urlStr;
            }
            return urlStr;
        }
        return "";
    }
}
