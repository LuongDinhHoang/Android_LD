package com.example.music_1.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_1.Adapter.SongAdapter;
import com.example.music_1.Model.Song;
import com.example.music_1.R;

import java.util.ArrayList;
import java.util.List;

import Services.MediaPlaybackService;

import static android.content.Context.BIND_AUTO_CREATE;


public class AllSongsFragment extends Fragment implements View.OnClickListener{
    private RecyclerView mRecycle;
    private List<Song> mList;
    private SongAdapter mAdapter;
    private LinearLayout mllBottom;
    private int mPosition;
    private ImageView mImagePlay;
    private TextView mNameSongPlay,mSongArtistPlay,mSongID;
    private ImageView btn_play;
    private MediaPlaybackService mMediaPlaybackService;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            mHandler.postDelayed(this, 300);
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_songs, container, false);
        initView(view);
        setService();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setService() {
        Intent intent = new Intent(getActivity(), MediaPlaybackService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        mHandler.postDelayed(runnable, 300);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlaybackService.MusicBinder binder = (MediaPlaybackService.MusicBinder) service;
            mMediaPlaybackService = binder.getMusicService();
            mMediaPlaybackService.getMediaManager().setListSong(mList);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMediaPlaybackService = null;
        }
    };
    private void updateUI() {
        if (mMediaPlaybackService != null) {

//            tvDuration.setText(currentTime + "/" + totalTime);
//            tvSongName.setText(song.getName());
//            sbMusic.setMax(Integer.parseInt(song.getDuration()));
//            sbMusic.setProgress(currentPosition);
        }
    }

    ////////////////////////////////////
    private void initView(final View view) {
        mList = new ArrayList<>();
        getSong();
        mSongID=view.findViewById(R.id.Song_Id);
        btn_play=view.findViewById(R.id.Button_Play);
        mImagePlay = view.findViewById(R.id.ImagePlay);
        mNameSongPlay = view.findViewById(R.id.NamePlay);
        mSongArtistPlay=view.findViewById(R.id.ArtistPlay);
        mRecycle = view.findViewById(R.id.Rcv_View);
        mllBottom= view.findViewById(R.id.ll_bottom);
        mllBottom.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecycle.setLayoutManager(manager);

        mAdapter = new SongAdapter(getActivity(), mList);
        mRecycle.setAdapter(mAdapter);
        if(mMediaPlaybackService != null && mMediaPlaybackService.getMediaManager().isStatusPlaying())
        {
            mllBottom.setVisibility(view.VISIBLE);
            mNameSongPlay.setText(mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongName());
            mSongArtistPlay.setText(mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongArtist());
            final byte[] songArt = getAlbumArt(mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongImage());
            Glide.with(view.getContext()).asBitmap()
                    .load(songArt)
                    .error(R.drawable.cute)
                    .into(mImagePlay);
            btn_play.setImageResource(R.drawable.ic_pause_black_large);
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setPlay(false);
            }
            mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).setPlay(true);
            mAdapter.notifyDataSetChanged();


        }
        if(mMediaPlaybackService != null && !mMediaPlaybackService.getMediaManager().isStatusPlaying())
        {
            mllBottom.setVisibility(view.VISIBLE);
            mNameSongPlay.setText(mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongName());
            mSongArtistPlay.setText(mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongArtist());
            final byte[] songArt = getAlbumArt(mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongImage());
            Glide.with(view.getContext()).asBitmap()
                    .load(songArt)
                    .error(R.drawable.cute)
                    .into(mImagePlay);
            btn_play.setImageResource(R.drawable.ic_play_black);
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setPlay(false);
            }
            mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).setPlay(true);
            mAdapter.notifyDataSetChanged();

        }

        mAdapter.SongAdapter(new SongAdapter.IIClick() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void ItemClick(Song song, int pos) {
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setPlay(false);
                }
                mList.get(pos).setPlay(true);
                //service
                if(mMediaPlaybackService != null)
                {
                     //đưa list vào list service nếu service chạy
                        mMediaPlaybackService.getMediaManager().playSong(song.getSongImage());
                         btn_play.setImageResource(R.drawable.ic_pause_black_large);
                         mMediaPlaybackService.getMediaManager().setCurrentSong(pos);
                }
                mllBottom.setVisibility(view.VISIBLE);
                mNameSongPlay.setText(song.getSongName());                                  //Click item RecycleView
                mSongArtistPlay.setText(song.getSongArtist());
                byte[] songArt = getAlbumArt(mList.get(pos).getSongImage());
                Glide.with(view.getContext()).asBitmap()
                        .load(songArt)
                        .error(R.drawable.cute)
                        .into(mImagePlay);

                //////////////
                mPosition=pos;

                mAdapter.notifyDataSetChanged();
//                mAdapter.setPos(pos);


            }
        });


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


                do {
                    int songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                    int songName = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                    long songTime = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    int songAuthor = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                    int songArt = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                    long currentId = songCursor.getLong(songID);
                    String currentName = songCursor.getString(songName);
                    String currentAuthor = songCursor.getString(songAuthor);
                    String currentArt = songCursor.getString(songArt);

                    Log.d("HoangLD", "getSong: "+songTime);
                    mList.add(new Song(currentId, currentName, songTime,currentAuthor, currentArt,false));
                } while (songCursor.moveToNext());
            }

        }
    }
    @Override
    public  void onClick(View view)
    {
        switch (view.getId()){
            case R.id.ll_bottom :{
                Song song = mList.get(mPosition);
                Log.d("HoangLD", "onClick: ");
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();// hide action bar
                MediaPlaybackFragment mediaPlaybackFragment = MediaPlaybackFragment.newInstance(song.getSongName(),song.getSongArtist(),song.getSongImage());
                fragmentTransaction.replace(R.id.ll_out,mediaPlaybackFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.Button_Play:{
                if(mMediaPlaybackService.getMediaManager().getMediaPlayer().isPlaying())
                {
                    mMediaPlaybackService.getMediaManager().pauseSong();
                    btn_play.setImageResource(R.drawable.ic_play_black);
                }
                else
                {
                    mMediaPlaybackService.getMediaManager().resumeSong();
                    btn_play.setImageResource(R.drawable.ic_pause_black_large);

                }
            }
        }


    }
}