package com.example.music_1.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_1.Model.Song;
import com.example.music_1.R;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
private Context context;
private List<Song> mList;
private IIClick mClick;

    public SongAdapter(Context context, List<Song> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);// khai báo vẽ view
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mSongName,mSongTime,mSongID;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mSongName =itemView.findViewById(R.id.Name);
            mSongTime=itemView.findViewById(R.id.Time);
            mSongID=itemView.findViewById(R.id.Song_Id);

        }
        public void binData(final Song song ,int pos) {
            mSongName.setText(song.getSongName()+"");
            mSongID.setText(pos+1+"");        //set dữ liệu cho từng item
            String c = song.getSongTime()/60000 +":"+song.getSongTime()%60000;


            mSongTime.setText(c);

        }
    }
    public  interface  IIClick
    {
        void ItemClick(Song song);

    }
}
