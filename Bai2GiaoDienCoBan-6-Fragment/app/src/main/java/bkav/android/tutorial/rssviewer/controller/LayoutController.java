package bkav.android.tutorial.rssviewer.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import bkav.android.tutorial.rssviewer.NewAdapter;
import bkav.android.tutorial.rssviewer.NewGetter;
import bkav.android.tutorial.rssviewer.NewGetter.NewItem;
import bkav.android.tutorial.rssviewer.OnNewClickListener;
import bkav.android.tutorial.rssviewer.fragment.NewContentFragment;
import bkav.android.tutorial.rssviewer.fragment.NewsListFragment;

public abstract class LayoutController implements OnNewClickListener {
    public static final String LAST_ITEM_TITLE_EXTRA = "last_item_title";
    
    protected AppCompatActivity mActivity;
    protected NewsListFragment mNewsListFragment;
    
    public LayoutController(AppCompatActivity activity) {
        mActivity = activity;
    }
    
    protected Bundle newBundleFromNewItem(NewItem item) {
        Bundle args = new Bundle();
        args.putString(NewContentFragment.TITLE_EXTRA, item.title);
        args.putString(NewContentFragment.DESCRIPTION_EXTRA, item.description);
        args.putString(NewContentFragment.DATE_EXTRA, item.date);
        args.putString(NewContentFragment.CONTENT_ENCODED_EXTRA, item.contentEncoded);
        args.putString(NewContentFragment.IMAGE_URL_EXTRA, item.imageUrl);
        return args;
    }
    
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(LAST_ITEM_TITLE_EXTRA, mNewsListFragment.getCurrentItemTitle());
    }
    
    public abstract void onCreate(Bundle savedInstanceState, String currentItemTitle);
}
