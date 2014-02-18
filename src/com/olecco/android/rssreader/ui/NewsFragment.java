package com.olecco.android.rssreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.olecco.android.rssreader.R;
import com.olecco.android.rssreader.model.Post;
import com.olecco.android.rssreader.network.RssLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olecco on 17.02.14.
 */
public class NewsFragment extends Fragment {

    private NewsAdapter mNewsAdapter;
    private ListView mNewsList;
    private ProgressBar mNewsProgress;
    private View mNewsEmptyView;
    private List<Post> mNews = new ArrayList<Post>();
    private boolean mNewsLoaded;

    private RssLoader.LoaderCallBack mLoaderCallBack = new RssLoader.LoaderCallBack() {
        @Override
        public void onSuccess(List<Post> news) {
            mNewsLoaded = true;
            hideNewsProgress();
            if (news != null) {
                mNews = news;
            }
            mNewsAdapter.notifyDataSetChanged();


        }

        @Override
        public void onFailed(String errorMessage) {
            mNewsLoaded = true;
            hideNewsProgress();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news, null);

        mNewsList = (ListView) view.findViewById(R.id.newsList);
        mNewsEmptyView = view.findViewById(R.id.newsEmpty);
        mNewsList.setEmptyView(mNewsEmptyView);
        mNewsAdapter = new NewsAdapter();
        mNewsList.setOnItemClickListener(mNewsAdapter);
        mNewsList.setAdapter(mNewsAdapter);

        mNewsProgress = (ProgressBar) view.findViewById(R.id.newsProgress);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void loadNews(String url) {
        if (!mNewsLoaded) {
            showNewsProgress();
            new RssLoader().loadNews(url, mLoaderCallBack);
        }
    }

    private void showNewsProgress() {
        mNewsEmptyView.setVisibility(View.GONE);
        mNewsList.setVisibility(View.GONE);
        mNewsProgress.setVisibility(View.VISIBLE);
    }

    private void hideNewsProgress() {
        mNewsEmptyView.setVisibility(View.VISIBLE);
        mNewsList.setVisibility(View.VISIBLE);
        mNewsProgress.setVisibility(View.GONE);
    }

    private class NewsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        @Override
        public int getCount() {
            return mNews.size();
        }

        @Override
        public Object getItem(int position) {
            return mNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewsHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.post_item, null);
                holder = new NewsHolder();
                holder.postTitleView = (TextView) convertView.findViewById(R.id.postTitle);
                holder.postDateView = (TextView) convertView.findViewById(R.id.postDate);
                convertView.setTag(holder);
            }
            else {
                holder = (NewsHolder) convertView.getTag();
            }

            Post post = (Post) getItem(position);
            holder.postTitleView.setText(post.getTitle());
            holder.postDateView.setText(post.getPubDate());

            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Post post = (Post) getItem(position);
            Intent intent = new Intent(getActivity(), PostActivity.class);
            intent.putExtra(PostActivity.POST_EXTRA, post);
            startActivity(intent);
        }
    }

    private class NewsHolder {
        public TextView postTitleView;
        public TextView postDateView;
    }
}
