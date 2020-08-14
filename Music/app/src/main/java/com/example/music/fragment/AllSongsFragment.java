package com.example.music.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.widget.Toast;

import com.example.music.R;
import com.example.music.adapter.SongAdapter;
import com.example.music.model.Song;

import java.util.ArrayList;
import java.util.List;

public class AllSongsFragment extends Fragment {
    private RecyclerView rcv_Song;
    private List<Song> mListSong;
    private SongAdapter mSongAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_allsongfragment,container,false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        mListSong = new ArrayList<>();
        getSong();
        rcv_Song = view.findViewById(R.id.rcv_Song);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rcv_Song.setLayoutManager(manager);
        mSongAdapter = new SongAdapter(getActivity(), mListSong);
        rcv_Song.setAdapter(mSongAdapter);
        mSongAdapter.SongAdapter(new SongAdapter.IIClick() {
            @Override
            public void onItemClick(Song song) {
                Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                MediaPlaybackFragment mediaPlaybackFragment = new MediaPlaybackFragment();
                //getSupportActionBar().hide();  // hide action bar
                fragmentTransaction.replace(R.id.ll_content, mediaPlaybackFragment);           // get fragment MediaPlayBackFragment vào activity main
                fragmentTransaction.commit();
            }
        });
    }
    public void getSong() {
       ContentResolver musicResolver = getActivity().getContentResolver();
        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = musicResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songName = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songTime = songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);

            do {
                long currentId = songCursor.getLong(songID);
                String currentName = songCursor.getString(songName);
                String currentTime = songCursor.getString(songTime);
                mListSong.add(new Song(currentId, currentName, currentTime, null, null));
            } while (songCursor.moveToNext());
        }

        mListSong.add(new Song(1, "Hôm nay em cưới rồi", "3:25", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(2, "Mưa của ngày xưa", "4:15", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(3, "Ngỡ", "3:25", "Quang Hà", "httpasdasdasd"));
        mListSong.add(new Song(4, "Âm thầm bên em", "5:05", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(5, "Hãy trao cho anh", "3:25", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(6, "Ngày mai sẽ khác", "6:25", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(7, "Tiếng chuông chiều", "2:25", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(8, "Chúng ta không thuộc về nhau", "3:75", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(9, "Chỉ còn lại tình yêu", "3:58", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(10, "Noi tình yêu bắt đầu", "3:25", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(11, "Hôm nay em cưới rồi", "3:25", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(12, "Nơi tình yêu bắt đầu", "3:25", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(13, "Lặng yên", "3:25", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(14, "Hôm nay em cưới rồi", "3:25", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(15, "Hôm nay em cưới rồi", "3:25", "Hoài Lâm", "httpasdasdasd"));
        mListSong.add(new Song(16, "Hôm nay em cưới rồi", "3:25", "Hoài Lâm", "httpasdasdasd"));

    }
}
