package com.example.music_1.Fragment;

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


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import Services.SongManager;

import static android.content.Context.BIND_AUTO_CREATE;


public class AllSongsFragment extends Fragment implements View.OnClickListener, SongManager.SongManageListener {
    private RecyclerView mRecycle;
    private List<Song> mList;
    private SongAdapter mAdapter;
    private LinearLayout mllBottom;
    private int mPosition;
    private ImageView mImagePlay;
    private TextView mNameSongPlay, mSongArtistPlay, mSongID;
    private ImageView mBtnPlay;
    private View view;
    private MediaPlaybackService mMediaPlaybackService;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            mHandler.postDelayed(this, 300);
        }
    };
    private boolean mIsBound;

    public void setCheck(boolean mCheck) {
        this.isVertical = mCheck;
    }

    private boolean isVertical;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_all_songs, container, false);
        initView();
        setService();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        if(mIsBound) {
            mMediaPlaybackService.getMediaManager().setListener(AllSongsFragment.this);//get vao
        }
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
            mMediaPlaybackService.getMediaManager().setListSong(mList);                     //đưa list vào list service nếu service chạy
            mMediaPlaybackService.getMedia().setListMedia(mList);
//            if(mIsBound){
            if (mMediaPlaybackService != null && mMediaPlaybackService.getMediaManager().getMediaPlayer().isPlaying()) {
                if (isVertical) {
                    mllBottom.setVisibility(View.VISIBLE);
                } else {
                    mllBottom.setVisibility(View.GONE);
                }
                if (!mMediaPlaybackService.getMediaManager().isStatusPlaying()) {
                    mBtnPlay.setImageResource(R.drawable.ic_play_black);
                } else {
                    mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);
                }
               UpdateUI();
                mAdapter.notifyDataSetChanged();

//                }
            }
            mMediaPlaybackService.getMediaManager().setListener(AllSongsFragment.this);//get vao
            mIsBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBound = false;
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
    private void initView() {
        mList = new ArrayList<>();
        getSong();
        mSongID = view.findViewById(R.id.Song_Id);
        mBtnPlay = view.findViewById(R.id.Button_Play);
        mImagePlay = view.findViewById(R.id.ImagePlay);
        mNameSongPlay = view.findViewById(R.id.NamePlay);
        mSongArtistPlay = view.findViewById(R.id.ArtistPlay);
        mRecycle = view.findViewById(R.id.Rcv_View);
        mllBottom = view.findViewById(R.id.ll_bottom);
        mllBottom.setOnClickListener(this);
        mBtnPlay.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecycle.setLayoutManager(manager);

        mAdapter = new SongAdapter(getActivity(), mList);
        mRecycle.setAdapter(mAdapter);
        //mMediaPlaybackService.getMediaManager().setListener(AllSongsFragment.this);//get vao
        if (mMediaPlaybackService != null) {
            if (isVertical) {
                mllBottom.setVisibility(View.VISIBLE);
            }

            UpdateUI();
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
                if (mMediaPlaybackService != null) {
                    mMediaPlaybackService.getMediaManager().playSong(song.getSongImage());
                    mMediaPlaybackService.getMediaManager().setCurrentSong(pos);//get position
                    mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);
                    //   mMediaPlaybackService.getMediaManager().setCurrentSong(pos);
                }
                if (isVertical) {
                    mllBottom.setVisibility(View.VISIBLE);
                    mNameSongPlay.setText(song.getSongName());                                  //Click item RecycleView
                    mSongArtistPlay.setText(song.getSongArtist());
                    byte[] songArt = getAlbumArt(mList.get(pos).getSongImage());
                    Glide.with(view.getContext()).asBitmap()
                            .load(songArt)
                            .error(R.drawable.cute)
                            .into(mImagePlay);
                    //////////////
                    mPosition = pos;
                } else {
                    mllBottom.setVisibility(View.GONE);
                    TextView mNameMedia = getActivity().findViewById(R.id.NameSong);
                    TextView mArtistMedia = getActivity().findViewById(R.id.ArtistSong);
                    ImageView mImageMedia = getActivity().findViewById(R.id.ImagePlaying);
                    ImageView mImageBackground = getActivity().findViewById(R.id.background_Image);
                    TextView mTimeEnd = getActivity().findViewById(R.id.end_time);



                    mArtistMedia.setText(song.getSongArtist());
                    mNameMedia.setText(song.getSongName());
                    mTimeEnd.setText(song.getTimeDurationString(song.getSongTime()));
                    byte[] songArt = getAlbumArt(mList.get(pos).getSongImage());
                    Glide.with(view.getContext()).asBitmap()
                            .load(songArt)
                            .error(R.drawable.cute)
                            .into(mImageBackground);
                    Glide.with(view.getContext()).asBitmap()
                            .load(songArt)
                            .error(R.drawable.cute)
                            .into(mImageMedia);

                }


                mAdapter.notifyDataSetChanged();
//                mAdapter.setPos(pos);


            }
        });


    }
    public  void UpdateUI()
    {
        mNameSongPlay.setText(mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongName());
        mSongArtistPlay.setText(mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongArtist());
        final byte[] songArt = getAlbumArt(mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongImage());
        Glide.with(view.getContext()).asBitmap()
                .load(songArt)
                .error(R.drawable.cute)
                .into(mImagePlay);
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setPlay(false);
        }
        if (!mMediaPlaybackService.getMediaManager().isStatusPlaying()) {
            mBtnPlay.setImageResource(R.drawable.ic_play_black);
        } else {
            mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);
        }
        mList.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).setPlay(true);
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


                    mList.add(new Song(currentId, currentName, songTime, currentAuthor, currentArt, false));
                } while (songCursor.moveToNext());
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_bottom: {
                Song song = mList.get(mPosition);
                Log.d("HoangLD", "onClick: ");
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();// hide action bar
                MediaPlaybackFragment mediaPlaybackFragment = MediaPlaybackFragment.newInstance(song.getSongName(), song.getSongArtist(), song.getSongImage());
                fragmentTransaction.replace(R.id.ll_out, mediaPlaybackFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.Button_Play: {
                if (mMediaPlaybackService.getMediaManager().getMediaPlayer().isPlaying()) {
                    mMediaPlaybackService.getMediaManager().pauseSong();
                    mBtnPlay.setImageResource(R.drawable.ic_play_black);
                } else {
                    mMediaPlaybackService.getMediaManager().resumeSong();
                    mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);

                }
            }
        }

    }

    @Override
    public void updateUiSongPlay(int pos) {
        Log.d("HoangLDpp", "updateUiSongPlay: "+pos);
        mPosition = pos;
        UpdateUI();
        mAdapter.notifyDataSetChanged();

    }
}