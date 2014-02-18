package com.olecco.android.rssreader.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.olecco.android.rssreader.R;
import com.olecco.android.rssreader.model.Post;

/**
 * Created by olecco on 17.02.14.
 */
public class PostActivity extends ActionBarActivity {

    public final static String POST_EXTRA = "POST_EXTRA";

    private TextView mPostTitleView;
    private TextView mPostDescriptionView;
    private TextView mPostDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        mPostTitleView = (TextView) findViewById(R.id.postTitle);
        mPostDescriptionView = (TextView) findViewById(R.id.postDescription);
        mPostDateView = (TextView) findViewById(R.id.postDate);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            Post post = args.getParcelable(POST_EXTRA);
            showPostData(post);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showPostData(Post post) {
        if (post != null) {
            mPostDateView.setText(post.getPubDate());
            mPostTitleView.setText(post.getTitle());
            mPostDescriptionView.setText(Html.fromHtml(post.getDescription()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
