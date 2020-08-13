
package bkav.android.tutorial.rssviewer;

import android.content.Context;
import android.support.v4.content.ContextCompat;

public class TwoColumnsNewAdapter extends NewAdapter {

    public TwoColumnsNewAdapter(NewGetter newGetter) {
        super(newGetter);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        int colorId = getColorId(holder.mTextView.getContext(),
                position == mNewGetter.getCurrentItemIndex());
        holder.mTextView.setBackgroundColor(colorId);
    }

    private int getColorId(Context context, boolean isSelected) {
        return ContextCompat.getColor(context, isSelected ? android.R.color.holo_blue_bright
                : android.R.color.white);
    }
}
