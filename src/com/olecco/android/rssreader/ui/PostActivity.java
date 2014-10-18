package com.olecco.android.rssreader.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.olecco.android.rssreader.IntentUtils;
import com.olecco.android.rssreader.R;
import com.olecco.android.rssreader.model.Post;

/**
 * Created by olecco on 17.02.14.
 */
public class PostActivity extends ActionBarActivity {

    public final static int OPEN_LINK_ITEM_ID = 1;
    public final static String POST_EXTRA = "POST_EXTRA";

    private TextView mPostTitleView;
    private TextView mPostDescriptionView;
    private TextView mPostDateView;

    private Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        mPostTitleView = (TextView) findViewById(R.id.postTitle);
        mPostDescriptionView = (TextView) findViewById(R.id.postDescription);
        mPostDateView = (TextView) findViewById(R.id.postDate);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            mPost = args.getParcelable(POST_EXTRA);
            showPostData(mPost);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0, OPEN_LINK_ITEM_ID, 0, R.string.open_link)
                .setIcon(R.drawable.ic_action_web_site);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == OPEN_LINK_ITEM_ID) {
            if (mPost != null) {
                IntentUtils.openURL(this, mPost.getLink());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
