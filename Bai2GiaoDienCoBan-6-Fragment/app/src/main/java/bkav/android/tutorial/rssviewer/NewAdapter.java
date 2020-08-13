
package bkav.android.tutorial.rssviewer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import bkav.android.tutorial.rssviewer.NewGetter.NewItem;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {
    protected NewGetter mNewGetter;
    
    private OnNewClickListener mOnNewClickListener;
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }
    
    public NewAdapter(NewGetter newGetter) {
        mNewGetter = newGetter;
    }

    @Override
    public NewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(mNewGetter.getItemAt(position).title);
        holder.mTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewGetter.setCurrentItemIndex(position);
                NewItem item = mNewGetter.getCurrentItem();
                
                if (mOnNewClickListener != null) {
                    mOnNewClickListener.onNewClick(item);
                    notifyDataSetChanged();
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mNewGetter.getCount();
    }
    
    public void setOnNewClickListener(OnNewClickListener onNewClickListener) {
        mOnNewClickListener = onNewClickListener;
    }
    
    
}
