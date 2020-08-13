
package bkav.android.tutorial.rssviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import bkav.android.tutorial.rssviewer.controller.LayoutController;
import bkav.android.tutorial.rssviewer.controller.OneColumnLayoutController;
import bkav.android.tutorial.rssviewer.controller.TwoColumnsLayoutController;

public class NewsListActivity extends AppCompatActivity {

    private LayoutController mLayoutController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);


        String currentItemTitle = null;
        if (savedInstanceState != null) {
            currentItemTitle = savedInstanceState.getString(LayoutController.LAST_ITEM_TITLE_EXTRA);
        }

        boolean isPortrait = getResources().getBoolean(R.bool.isPortrait);
        mLayoutController = isPortrait ? new OneColumnLayoutController(this)
                : new TwoColumnsLayoutController(this);

        mLayoutController.onCreate(savedInstanceState, currentItemTitle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mLayoutController.onSaveInstanceState(outState);
    }
}
