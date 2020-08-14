package com.example.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.ActivityMusic;
import com.example.music.R;
import com.example.music.model.Song;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{
    private Context mContext;
    private List<Song> mListSong;
    private IIClick mListener;

    public SongAdapter(Context mContext, List<Song> mListSong) {
        this.mContext = mContext;
        this.mListSong = mListSong;
    }

    public void SongAdapter(IIClick mListener) {
        this.mListener = mListener;
    }

    // private IItemClick mListener;
    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.itemmusic,parent,false);// khai báo vẽ view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, int position) {
           holder.binData(mListSong.get(position));
    }

    @Override
    public int getItemCount() {
        return mListSong.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mSongName;
        TextView mSongTime;
        TextView mSongID;

        public ViewHolder(@NonNull View itemView) {   // ánh xạ  dữ liệu
            super(itemView);
            mSongName = itemView.findViewById(R.id.tv_songName);
            mSongTime = itemView.findViewById(R.id.tv_songTime);
            mSongID = itemView.findViewById(R.id.tv_songID);

        }
        public void binData(final Song song) {
            mSongName.setText(song.getmSongName()+"");
            mSongID.setText(String.valueOf(song.getmSongID()));        //set dữ liệu cho từng item
            mSongTime.setText(song.getmSongTime());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(song);
                }
            });
        }
    }
    public interface IIClick{
        void onItemClick(Song song);
    }
}
