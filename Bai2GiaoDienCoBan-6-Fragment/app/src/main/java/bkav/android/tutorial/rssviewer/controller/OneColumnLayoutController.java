
package bkav.android.tutorial.rssviewer.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import bkav.android.tutorial.rssviewer.NewAdapter;
import bkav.android.tutorial.rssviewer.NewGetter;
import bkav.android.tutorial.rssviewer.NewGetter.NewItem;
import bkav.android.tutorial.rssviewer.R;
import bkav.android.tutorial.rssviewer.fragment.NewContentFragment;
import bkav.android.tutorial.rssviewer.fragment.NewsListFragment;

public class OneColumnLayoutController extends LayoutController {
    public OneColumnLayoutController(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, String currentItemTitle) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (mActivity.findViewById(R.id.fragment_container) != null) {
            // Create a new Fragment to be placed in the activity layout
            mNewsListFragment = new NewsListFragment();
            mNewsListFragment.setOnNewClickListener(this);

            // Add the fragment to the 'fragment_container' FrameLayout
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mNewsListFragment).commit();
        }
    }

    @Override
    public void onNewClick(NewItem item) {
        // Create fragment and give it an argument specifying the article it should show
        NewContentFragment newFragment = new NewContentFragment();
        Bundle args = newBundleFromNewItem(item);
        newFragment.setArguments(args);

        FragmentTransaction transaction = mActivity.getSupportFragmentManager()
                .beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
