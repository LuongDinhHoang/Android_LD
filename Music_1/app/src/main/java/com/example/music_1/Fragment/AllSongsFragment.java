package com.example.music_1.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.music_1.Adapter.SongAdapter;
import com.example.music_1.Model.Song;
import com.example.music_1.R;

import java.util.ArrayList;
import java.util.List;


public class AllSongsFragment extends Fragment {
    private RecyclerView mRecycle;
    private List<Song> mList;
    private SongAdapter mAdapter;
    private LinearLayout mllBottom;
    private int mPosition;
    private ImageView mImagePlay;
    private TextView mNameSongPlay,mSongArtistPlay;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_songs, container, false);
        initView(view);
        mllBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Song song = mList.get(mPosition);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MediaPlaybackFragment mediaPlaybackFragment = MediaPlaybackFragment.newInstance(song.getSongName(),song.getSongArtist());
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();  // hide action bar
                fragmentTransaction.replace(R.id.ll_out,mediaPlaybackFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        return view;
    }

    private void initView(final View view) {
        mList = new ArrayList<>();
        getSong();
        mImagePlay = view.findViewById(R.id.ImagePlay);
        mNameSongPlay = view.findViewById(R.id.NamePlay);
        mSongArtistPlay=view.findViewById(R.id.ArtistPlay);
        mRecycle = view.findViewById(R.id.Rcv_View);
        mllBottom= view.findViewById(R.id.ll_bottom);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecycle.setLayoutManager(manager);

        mAdapter = new SongAdapter(getActivity(), mList);
        mRecycle.setAdapter(mAdapter);
        mAdapter.SongAdapter(new SongAdapter.IIClick() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void ItemClick(Song song, int pos) {
                mllBottom.setVisibility(view.VISIBLE);
                mPosition=pos;
                mNameSongPlay.setText(song.getSongName());
                mSongArtistPlay.setText(song.getSongArtist());
                byte[] songArt = getAlbumArt(mList.get(pos).getSongImage());

                if(songArt != null)
                {
                    Glide.with(view.getContext()).asBitmap()
                            .load(songArt)
                            .into(mImagePlay);
                }
                else
                {
                    mImagePlay.setImageDrawable(getContext().getDrawable(R.drawable.ic_zing));
                }


            }
        });


//            @Override
////            public void onItemClick(Song song) {
////                Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
////
////                FragmentManager fragmentManager = getFragmentManager();
////                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////
////                MediaPlaybackFragment mediaPlaybackFragment = new MediaPlaybackFragment();
////                getSupportActionBar().hide();  // hide action bar
////                fragmentTransaction.replace(R.id.ll_out, mediaPlaybackFragment);           // get fragment MediaPlayBackFragment vào activity main
////                fragmentTransaction.commit();
////            }
////        });
    }
    public static byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri);
        byte[] albumArt = mediaMetadataRetriever.getEmbeddedPicture();  // chuyển đổi đường dẫn file media thành đường dẫn file Ảnh
        mediaMetadataRetriever.release();
        return albumArt;
    }

    public void getSong() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = musicResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            if (songCursor != null && songCursor.moveToFirst()) {
                int songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int songName = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                long songTime = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                int songAuthor = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int songArt = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                do {

                    long currentId = songCursor.getLong(songID);
                    String currentName = songCursor.getString(songName);
                    String currentAuthor = songCursor.getString(songAuthor);
                    String currentArt = songCursor.getString(songArt);

                    Log.d("HoangLD", "getSong: "+songTime);
                    mList.add(new Song(currentId, currentName, songTime,currentAuthor, currentArt));
                } while (songCursor.moveToNext());
            }

        }
    }
}