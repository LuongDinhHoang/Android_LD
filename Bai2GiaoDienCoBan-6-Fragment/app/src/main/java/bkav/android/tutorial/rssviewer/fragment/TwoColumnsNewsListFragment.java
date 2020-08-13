package bkav.android.tutorial.rssviewer.fragment;

import bkav.android.tutorial.rssviewer.NewAdapter;
import bkav.android.tutorial.rssviewer.NewGetter;
import bkav.android.tutorial.rssviewer.TwoColumnsNewAdapter;

public class TwoColumnsNewsListFragment extends NewsListFragment {

    @Override
    public NewAdapter getNewAdapter(NewGetter newGetter) {
        return new TwoColumnsNewAdapter(newGetter);
    }
}
