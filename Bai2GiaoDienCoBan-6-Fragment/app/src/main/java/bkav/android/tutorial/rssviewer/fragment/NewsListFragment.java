
package bkav.android.tutorial.rssviewer.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import bkav.android.tutorial.rssviewer.NewAdapter;
import bkav.android.tutorial.rssviewer.NewGetter;
import bkav.android.tutorial.rssviewer.OnNewClickListener;
import bkav.android.tutorial.rssviewer.R;

public class NewsListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private NewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnNewClickListener mOnNewClickListener;
    private NewGetter mNewGetter;
    private String mCurrentItemTitle;

    private LoadCallback mLoadCallback;

    public void setLoadCallback(LoadCallback callback) {
        mLoadCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater
                .inflate(R.layout.news_list_layout, container, false);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        return mRecyclerView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mNewGetter = new NewGetter();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (!TextUtils.isEmpty(mCurrentItemTitle)) {
                    mNewGetter.setCurrentItemByTitle(mCurrentItemTitle);
                }

                // specify an adapter (see also next example)
                mAdapter = getNewAdapter(mNewGetter);
                mAdapter.setOnNewClickListener(mOnNewClickListener);

                mRecyclerView.setAdapter(mAdapter);

                if (mLoadCallback != null) {
                    mLoadCallback.onLoadFinish(mNewGetter);
                }
            }
        }.execute();
    }

    public void setOnNewClickListener(OnNewClickListener onNewClickListener) {
        mOnNewClickListener = onNewClickListener;
        if (mAdapter != null) {
            mAdapter.setOnNewClickListener(onNewClickListener);
        }
    }

    public String getCurrentItemTitle() {
        return mNewGetter.getCurrentItem().title;
    }

    public interface LoadCallback {
        public void onLoadFinish(NewGetter newGetter);
    }
    
    public NewAdapter getNewAdapter(NewGetter newGetter) {
        return new NewAdapter(newGetter);
    }
}
