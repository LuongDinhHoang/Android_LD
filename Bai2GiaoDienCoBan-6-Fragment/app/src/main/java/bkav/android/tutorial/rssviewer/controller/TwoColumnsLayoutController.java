
package bkav.android.tutorial.rssviewer.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import bkav.android.tutorial.rssviewer.NewGetter.NewItem;
import bkav.android.tutorial.rssviewer.R;
import bkav.android.tutorial.rssviewer.fragment.NewContentFragment;
import bkav.android.tutorial.rssviewer.fragment.TwoColumnsNewsListFragment;

public class TwoColumnsLayoutController extends LayoutController {
    private NewContentFragment mNewContentFragment;

    public TwoColumnsLayoutController(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, String currentItemTitle) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (mActivity.findViewById(R.id.fragment_container2) != null) {
            mNewContentFragment = new NewContentFragment();
            Bundle args = new Bundle();
            args.putString(LAST_ITEM_TITLE_EXTRA, currentItemTitle);
            mNewContentFragment.setArguments(args);
            
            // Create a new Fragment to be placed in the activity layout
            mNewsListFragment = new TwoColumnsNewsListFragment();
            mNewsListFragment.setOnNewClickListener(this);
            mNewsListFragment.setLoadCallback(mNewContentFragment);

            // Add the fragment to the 'fragment_container' FrameLayout
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.news_list_fragment_container, mNewsListFragment)
                    .replace(R.id.new_content_fragment_container, mNewContentFragment)
                    .commit();
        }
    }
    
    @Override
    public void onNewClick(NewItem item) {
        Bundle args = newBundleFromNewItem(item);
        mNewContentFragment.update(args);
    }
}
