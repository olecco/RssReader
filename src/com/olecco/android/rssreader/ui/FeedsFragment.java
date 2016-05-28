package com.olecco.android.rssreader.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.olecco.android.rssreader.R;
import com.olecco.android.rssreader.model.RssFeed;
import com.olecco.android.rssreader.store.StoreHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olecco on 17.02.14.
 */
public class FeedsFragment extends Fragment {

    private static final int ADD_ITEM_ID = 1;

    private FeedAdapter mFeedsAdapter;
    private ListView mFeedsList;
    private ProgressBar mFeedsProgress;
    private View mFeedsEmptyView;
    private List<RssFeed> mFeeds = new ArrayList<RssFeed>();
    private ProgressDialog mFeedsProgressDialog;
    private boolean mFeedsLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feeds, null);

        mFeedsList = (ListView) view.findViewById(R.id.feedsList);
        mFeedsEmptyView = view.findViewById(R.id.feedsEmpty);
        mFeedsList.setEmptyView(mFeedsEmptyView);
        mFeedsAdapter = new FeedAdapter();
        mFeedsList.setOnItemClickListener(mFeedsAdapter);
        mFeedsList.setOnItemLongClickListener(mFeedsAdapter);
        mFeedsList.setAdapter(mFeedsAdapter);
        mFeedsProgress = (ProgressBar) view.findViewById(R.id.feedsProgress);

        mFeedsProgressDialog = new ProgressDialog(getActivity());
        mFeedsProgressDialog.setIndeterminate(true);
        mFeedsProgressDialog.setCancelable(false);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.add(0, ADD_ITEM_ID, 0, R.string.add_feed)
                .setIcon(R.drawable.ic_action_new);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == ADD_ITEM_ID) {
            showInputDialog();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showInputDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle(R.string.add_feed);
        alert.setMessage(R.string.add_feed_message);

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        alert.setView(input);

        alert.setPositiveButton(R.string.feed_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String url = input.getText().toString().trim();
                if (url != null && !url.isEmpty()) {
                    RssFeed feed = new RssFeed();
                    feed.setTitle(url);
                    feed.setUrl(url);
                    if (!mFeeds.contains(feed)) {
                        addFeed(feed);
                    }
                    else {
                        Toast.makeText(getActivity(), getString(R.string.feed_exists), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), getString(R.string.feed_url_empty), Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton(R.string.feed_dialog_cancel, null);

        alert.show();
    }

    private void showDeleteDialog(final RssFeed feed) {
        final CharSequence[] items = { "Delete" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.delete_feed);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFeed(feed);
            }
        });

        builder.show();
    }

    private void showProgressDialog(String message) {
        mFeedsProgressDialog.setMessage(message);
        mFeedsProgressDialog.show();
    }

    private void hideProgressDialog() {
        mFeedsProgressDialog.dismiss();
    }

    private void showFeedsProgress() {
        mFeedsEmptyView.setVisibility(View.GONE);
        mFeedsList.setVisibility(View.GONE);
        mFeedsProgress.setVisibility(View.VISIBLE);
    }

    private void hideFeedsProgress() {
        mFeedsEmptyView.setVisibility(mFeeds.isEmpty() ? View.VISIBLE : View.GONE);
        mFeedsList.setVisibility(View.VISIBLE);
        mFeedsProgress.setVisibility(View.GONE);
    }

    public void loadFeeds() {
        if (!mFeedsLoaded) {
            new LoadFeedsTask().execute();
        }
    }

    public void addFeed(RssFeed feed) {
        new AddFeedTask().execute(feed);
    }

    public void deleteFeed(RssFeed feed) {
        new DeleteFeedTask().execute(feed);
    }

    private class AddFeedTask extends AsyncTask<RssFeed, Void, RssFeed> {

        @Override
        protected void onPreExecute() {
            showProgressDialog(getString(R.string.feed_adding_message));
        }

        @Override
        protected RssFeed doInBackground(RssFeed... params) {
            RssFeed feed = params[0];
            StoreHelper storeHelper = new StoreHelper(getActivity());
            storeHelper.storeFeed(feed);
            return feed;
        }

        @Override
        protected void onPostExecute(RssFeed result) {
            mFeeds.add(result);
            mFeedsAdapter.notifyDataSetChanged();
            hideProgressDialog();
        }
    }

    private class DeleteFeedTask extends AsyncTask<RssFeed, Void, RssFeed> {

        @Override
        protected void onPreExecute() {
            showProgressDialog(getString(R.string.feed_deleting_message));
        }

        @Override
        protected RssFeed doInBackground(RssFeed... params) {
            RssFeed feed = params[0];
            StoreHelper storeHelper = new StoreHelper(getActivity());
            storeHelper.deleteFeed(feed);
            return feed;
        }

        @Override
        protected void onPostExecute(RssFeed result) {
            mFeeds.remove(result);
            mFeedsAdapter.notifyDataSetChanged();
            hideProgressDialog();
        }
    }

    private class LoadFeedsTask extends AsyncTask<Void, Void, List<RssFeed>> {

        @Override
        protected void onPreExecute() {
            showFeedsProgress();
        }

        @Override
        protected List<RssFeed> doInBackground(Void... params) {
            StoreHelper storeHelper = new StoreHelper(getActivity());
            return storeHelper.getFeeds();
        }

        @Override
        protected void onPostExecute(List<RssFeed> feeds) {
            mFeedsLoaded = true;
            mFeeds = feeds;
            mFeedsAdapter.notifyDataSetChanged();
            hideFeedsProgress();
        }
    }

    private class FeedAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

        @Override
        public int getCount() {
            return mFeeds.size();
        }

        @Override
        public Object getItem(int position) {
            return mFeeds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeedsHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.feed_item, null);
                holder = new FeedsHolder();
                holder.feedTitleView = (TextView) convertView.findViewById(R.id.feedTitle);
                convertView.setTag(holder);
            }
            else {
                holder = (FeedsHolder) convertView.getTag();
            }

            RssFeed feed = (RssFeed) getItem(position);
            holder.feedTitleView.setText(feed.getTitle());

            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            RssFeed feed = (RssFeed) getItem(position);
            Intent intent = new Intent(getActivity(), NewsActivity.class);
            intent.putExtra(NewsActivity.RSS_URL_EXTRA, feed.getUrl());
            startActivity(intent);
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            showDeleteDialog((RssFeed) getItem(position));
            return true;
        }
    }

    private class FeedsHolder {
        public TextView feedTitleView;
    }

}
