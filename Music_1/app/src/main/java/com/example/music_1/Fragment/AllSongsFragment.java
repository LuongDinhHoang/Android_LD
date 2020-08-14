package com.example.music_1.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.music_1.Adapter.SongAdapter;
import com.example.music_1.Model.Song;
import com.example.music_1.R;

import java.util.ArrayList;
import java.util.List;


public class AllSongsFragment extends Fragment {
    private RecyclerView mRecycle;
    private List<Song> mList;
    private SongAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_songs, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mList = new ArrayList<>();
        getSong();
        mRecycle = view.findViewById(R.id.Rcv_View);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecycle.setLayoutManager(manager);
        adapter = new SongAdapter(getActivity(), mList);
        mRecycle.setAdapter(adapter);
//        adapter.SongAdapter(new SongAdapter.IIClick() {
//            @Override
//            public void onItemClick(Song song) {
//                Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
//
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                MediaPlaybackFragment mediaPlaybackFragment = new MediaPlaybackFragment();
//                //getSupportActionBar().hide();  // hide action bar
//                fragmentTransaction.replace(R.id.ll_content, mediaPlaybackFragment);           // get fragment MediaPlayBackFragment vào activity main
//                fragmentTransaction.commit();
//            }
//        });
    }

    public void getSong() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = musicResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            if (songCursor != null && songCursor.moveToFirst()) {

                do {
                    int songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                    int songName = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                    long songTime = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    long currentId = songCursor.getLong(songID);
                    String currentName = songCursor.getString(songName);
                    Log.d("HoangLD", "getSong: "+songTime);
                    mList.add(new Song(currentId, currentName, songTime, null, null));
                } while (songCursor.moveToNext());
            }

        }
    }
}