package com.olecco.android.rssreader.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.olecco.android.rssreader.model.Post;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by olecco on 17.02.14.
 */
public class RssLoader {

    public interface LoaderCallBack {
        void onSuccess(List<Post> news);
        void onFailed(String errorMessage);
    }

    private static final int CALLBACK_CODE_LOADED = 0;
    private static final int CALLBACK_CODE_FAILURE = 1;

    private String mUrl;
    private WeakReference<LoaderCallBack> mLoaderCallBackRef;

    private Handler mCallBackHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CALLBACK_CODE_LOADED:
                    fireOnSuccess((List<Post>) msg.obj);
                    break;
                default:
                    fireOnFailed((String) msg.obj);
            }
        }
    };

    private Runnable mLoaderRunnable = new Runnable() {
        @Override
        public void run() {
            if (mUrl != null && !mUrl.isEmpty()) {
                try {
                    URL url = new URL(mUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    try {
                        //String resStr = readInputData(conn.getInputStream());
                        List<Post> result = RssParser.parse(conn.getInputStream());
                        //Log.d("rss", resStr);
                        sendCallbackMessage(CALLBACK_CODE_LOADED, result);
                    }
                    finally {
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    sendCallbackMessage(CALLBACK_CODE_FAILURE, e.getMessage());
                }
            }
            else {
                sendCallbackMessage(CALLBACK_CODE_FAILURE, "Invalid input params");
            }
        }
    };

    private String readInputData(InputStream stream) throws IOException {
        BufferedInputStream buffer = new BufferedInputStream(stream);

        StringBuilder builder = new StringBuilder();
        byte[] buf = new byte[512];
        while (buffer.read(buf) != -1)
            builder.append(new String(buf, "UTF-8"));

        buffer.close();

        return builder.toString();
    }

    private void sendCallbackMessage(int callbackCode, Object data) {
        Message msg = mCallBackHandler.obtainMessage(callbackCode, data);
        msg.sendToTarget();
    }


    public void loadNews(String url, LoaderCallBack callBack) {
        mUrl = url;
        if (callBack != null) {
            mLoaderCallBackRef = new WeakReference<LoaderCallBack>(callBack);
        }
        new Thread(mLoaderRunnable).start();
    }

    private LoaderCallBack getCallBack() {
        if (mLoaderCallBackRef != null) {
            return mLoaderCallBackRef.get();
        }
        return null;
    }

    private void fireOnSuccess(List<Post> news) {
        LoaderCallBack callBack = getCallBack();
        if (callBack != null) {
            callBack.onSuccess(news);
        }
    }

    private void fireOnFailed(String errorMessage) {
        LoaderCallBack callBack = getCallBack();
        if (callBack != null) {
            callBack.onFailed(errorMessage);
        }
    }
}
