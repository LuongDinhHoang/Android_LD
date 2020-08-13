
package bkav.android.tutorial.rssviewer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import bkav.android.tutorial.rssviewer.ImageLoader;
import bkav.android.tutorial.rssviewer.NewGetter;
import bkav.android.tutorial.rssviewer.NewGetter.NewItem;
import bkav.android.tutorial.rssviewer.R;
import bkav.android.tutorial.rssviewer.controller.LayoutController;
import bkav.android.tutorial.rssviewer.fragment.NewsListFragment.LoadCallback;

public class NewContentFragment extends Fragment implements LoadCallback {
    public static final String TITLE_EXTRA = "title";
    public static final String DESCRIPTION_EXTRA = "description";
    public static final String DATE_EXTRA = "date";
    public static final String CONTENT_ENCODED_EXTRA = "content_encoded";
    public static final String IMAGE_URL_EXTRA = "image_url";

    private TextView mTitle;
    private TextView mDescription;
    private TextView mContentEncoded;
    private TextView mDate;
    private ImageView mImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_content_layout, container, false);

        mTitle = (TextView) view.findViewById(R.id.title);
        mTitle.setVisibility(View.GONE);

        mDescription = (TextView) view.findViewById(R.id.image_description);
        mContentEncoded = (TextView) view.findViewById(R.id.content_encoded);
        mDate = (TextView) view.findViewById(R.id.date);
        mImage = (ImageView) view.findViewById(R.id.image);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        update(args);
    }

    public void update(Bundle args) {
        if (args != null) {
            updateUi(args.getString(TITLE_EXTRA),
                    args.getString(DESCRIPTION_EXTRA),
                    args.getString(CONTENT_ENCODED_EXTRA),
                    args.getString(DATE_EXTRA),
                    args.getString(IMAGE_URL_EXTRA));
        }
    }

    @Override
    public void onLoadFinish(NewGetter newGetter) {
        // Bkav QuangLH: khi NewsListFragment load xong danh sach list
        // va neu NewContentFragment ton tai (truong hop giao dien may
        // tinh bang xoay ngang) thi tien hanh load giao dien luon.
        Bundle args = getArguments();

        String lastItemTitle = args != null ? args
                .getString(LayoutController.LAST_ITEM_TITLE_EXTRA) : null;
        if (!TextUtils.isEmpty(lastItemTitle)) {
            newGetter.setCurrentItemByTitle(lastItemTitle);
        }

        NewItem item = newGetter.getCurrentItem();
        updateUi(item.title, item.description, item.contentEncoded, item.date, item.imageUrl);
    }

    private void updateUi(String title, String description, String contentEncoded, String date,
            String imageUrl) {
        getActivity().setTitle(title);
        mDescription.setText(description);
        mContentEncoded.setText(contentEncoded);
        mDate.setText(date);

        mImage.setImageBitmap(null);
        new ImageLoader(imageUrl, mImage).execute();
    }
}
