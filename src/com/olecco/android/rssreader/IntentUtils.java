package com.olecco.android.rssreader;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by olecco on 18.10.2014.
 */
public class IntentUtils {

    public static void openURL(Context context, String urlString) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(urlString));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {}
    }

}
