package com.example.music_1.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_1.Model.Song;
import com.example.music_1.R;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.example.music_1.Services.MediaPlaybackService;
import es.claucookie.miniequalizerlibrary.EqualizerView;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements Filterable {
private Context context;
private List<Song> mList;
private List<Song> mListFull;
private Context mContext;

    public IIClick getClick() {
        return mClick;
    }

    public void setClick(IIClick mClick) {
        this.mClick = mClick;
    }

    private IIClick mClick;
    private MediaPlaybackService mMediaPlaybackService;
    public void setMediaPlaybackService(MediaPlaybackService mMediaPlaybackService) {
        this.mMediaPlaybackService = mMediaPlaybackService;
    }
    public void SongAdapter(IIClick mClick) {
        this.mClick = mClick;
    }

    public SongAdapter(Context context, List<Song> mList) {
        this.context = context;
        this.mList = mList;
        mListFull = new LinkedList<>();
        mListFull.addAll(mList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
            view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);// khai báo vẽ view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binData(mList.get(position),position);

    }
    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            LinkedList<Song> filteredList = new LinkedList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(mListFull);
            } else {
                for (Song songName : mListFull) {
                    if (songName.getSongName().toLowerCase().contains(constraint.toString().toLowerCase().trim())) {
                        filteredList.addLast(songName);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mList.clear();
            mList.addAll((Collection<? extends Song>) results.values);
            notifyDataSetChanged();

        }
    };

    public void setList(List<Song> mList) {
        this.mList = mList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mSongName,mSongTime,mSongID;
        EqualizerView mImage;
        ImageView mImagePause;
        ImageView mPopup;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mSongName =itemView.findViewById(R.id.Name);
            mSongTime=itemView.findViewById(R.id.Time);
            mSongID=itemView.findViewById(R.id.Song_Id);
            mImagePause=itemView.findViewById(R.id.Image_Pause);
            mImage=itemView.findViewById(R.id.Image_Id);
            mPopup=itemView.findViewById(R.id.popup_button);
        }

        public TextView getSongTime() {
            return mSongTime;
        }

        public void binData(final Song song , final int pos) {
            final Song mCurrent = mList.get(pos);
            mSongName.setText(song.getSongName()+"");
            mSongID.setText(String.valueOf(song.getPos()+1));        //set dữ liệu cho từng item
            mSongTime.setText(song.getTimeDurationString(song.getSongTime()));
            mSongID.setVisibility(View.VISIBLE);
            mImage.setVisibility(View.INVISIBLE);
            mSongName.setTypeface(null, Typeface.NORMAL);

            if(song.isPlay())
            {
               if(mMediaPlaybackService.isPlay())
               {
                   mSongID.setVisibility(View.INVISIBLE);
                   mImage.setVisibility(View.VISIBLE);
                   mImage.animateBars();
                   mSongName.setTypeface(null, Typeface.BOLD);
               }
               else {
                   mSongID.setTypeface(null,Typeface.BOLD);
                   mSongName.setTypeface(null, Typeface.BOLD);
               }
            }
            else mImage.stopBars();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClick.ItemClick(song,pos);
                }
            });
            mPopup.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if(mClick != null)
                    {
                        mClick.onSongBtnClickListener(mPopup, view, song, pos);

                    }
                }
            });

        }

    }
    public  interface  IIClick
    {
        void ItemClick(Song song,int pos);
        void onSongBtnClickListener(ImageView btn, View v, Song song, int pos);

    }
}
