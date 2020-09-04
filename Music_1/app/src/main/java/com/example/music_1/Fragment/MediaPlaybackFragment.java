package com.example.music_1.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_1.Model.Song;
import com.example.music_1.R;

import java.util.ArrayList;
import java.util.List;

import Services.MediaPlaybackService;
import Services.SongManager;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.music_1.Fragment.AllSongsFragment.getAlbumArt;


public class MediaPlaybackFragment extends Fragment implements View.OnClickListener , SongManager.SongManageListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private TextView mName, mArtist;
    ImageView mImage, mBackground;
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private MediaPlaybackService mMediaPlaybackService;
    private ImageView mPlayMedia,mButtonShuffle,mButtonRepeat;

    public void setVertical(boolean vertical) {
        isVertical = vertical;
    }
    private  boolean isVertical;


    public void setListMedia(List<Song> mListMedia) {
        this.mListMedia = mListMedia;
    }

    private List<Song> mListMedia = new ArrayList<>();
    private ImageButton mNextMedia, mBackMedia;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            mHandler.postDelayed(this, 300);
        }
    };


    // TODO: Rename and change types of parameters
    private String Name;
    private String Artist;
    private String Image;
    View view;

    public MediaPlaybackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_playback, container, false);
        initView();
        return view;
    }

    public void initView() {
        mName = view.findViewById(R.id.NameSong);
        mArtist = view.findViewById(R.id.ArtistSong);
        mImage = view.findViewById(R.id.ImagePlaying);
        mNextMedia = view.findViewById(R.id.btn_next_media);
        mBackMedia = view.findViewById(R.id.btn_pre_media);
        mPlayMedia = view.findViewById(R.id.PlayMedia);
        mBackground = view.findViewById(R.id.background_Image);
        mButtonShuffle=view.findViewById(R.id.button_Shuffle);
        mButtonRepeat=view.findViewById(R.id.button_Repeat);
        mButtonRepeat.setOnClickListener(this);
        mButtonShuffle.setOnClickListener(this);
        mPlayMedia.setOnClickListener(this);
        mBackMedia.setOnClickListener(this);
        mNextMedia.setOnClickListener(this);


//
//        if (Image != null) {
//            final byte[] songArt = getAlbumArt(Image);
//            Glide.with(view.getContext()).asBitmap()
//                    .load(songArt)
//                    .error(R.drawable.cute)
//                    .into(mImage);
//            Glide.with(view.getContext()).asBitmap()
//                    .load(songArt)
//                    .error(R.drawable.background)
//                    .into(mBackground);
//        }
//
//        mName.setText(Name);
//        mArtist.setText(Artist);
//
//        if(isVertical)
//        {
//            mBackground.setScaleType(ImageView.ScaleType.FIT_XY);
//        }
//        else {
//            mBackground.setScaleType(ImageView.ScaleType.FIT_XY);
//
//        }
        setData();
        //  mName.setText(mListMedia.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongName());


    }

    @Override
    public void onStart() {
        if(mServiceConnection == null) {

            setService();
        }
        super.onStart();
    }

    private void setService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MediaPlaybackService.MusicBinder binder = (MediaPlaybackService.MusicBinder) service;
                mMediaPlaybackService = binder.getMusicService();
                mListMedia= mMediaPlaybackService.getMediaManager().getmListSong();
                mMediaPlaybackService.getMediaManager().setListener(MediaPlaybackFragment.this);//get vao
                if(mMediaPlaybackService.getMediaManager().getShuffle())
                {
                    mButtonShuffle.setImageResource(R.drawable.ic_play_shuffle_orange);
                }else {
                    mButtonShuffle.setImageResource(R.drawable.ic_shuffle_white);
                }
                if(mMediaPlaybackService.getMediaManager().isRepeat)
                {
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_one_song_dark);
                }
                if (mMediaPlaybackService.getMediaManager().isRepeatAll)
                {
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_dark_selected);

                }

//                if(mMediaPlaybackService != null)
//                {
//                    if (mMediaPlaybackService.getMediaManager().getMediaPlayer().isPlaying()) {
//                        mPlayMedia.setImageResource(R.drawable.ic_pause_media);
//
//                    } else {
//                        mPlayMedia.setImageResource(R.drawable.ic_play_media);
//                    }
//                    //mIsConnect = true;
//                }

                setData();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIsConnect = false;
                mMediaPlaybackService = null;
            }
        };
        Intent intent = new Intent(getActivity(), MediaPlaybackService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        mHandler.postDelayed(runnable, 300);
    }

    private boolean mIsConnect;

    private ServiceConnection mServiceConnection;

    private void updateUI() {
        if (mMediaPlaybackService != null) {
//            tvDuration.setText(currentTime + "/" + totalTime);
//            tvSongName.setText(song.getName());
//            sbMusic.setMax(Integer.parseInt(song.getDuration()));
//            sbMusic.setProgress(currentPosition);
        }
    }

    ////////////////////////////////////
    public void setData() {
        if (mMediaPlaybackService != null) {
            if (mMediaPlaybackService.getMediaManager().getMediaPlayer().isPlaying()) {
                mPlayMedia.setImageResource(R.drawable.ic_pause_media);
            } else {
                mPlayMedia.setImageResource(R.drawable.ic_play_media);
            }
            //int mCurrent = mMediaPlaybackService.getMediaManager().getCurrentSong();
            Log.d("HoangLD", "setData: "+mName);
            mName.setText(mListMedia.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongName());
            mArtist.setText(mListMedia.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongArtist());
            final byte[] songArt = getAlbumArt(mListMedia.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongImage());
            Glide.with(view.getContext()).asBitmap()
                    .load(songArt)
                    .error(R.drawable.cute)
                    .into(mImage);
            Glide.with(view.getContext()).asBitmap()
                    .load(songArt)
                    .error(R.drawable.background)
                    .into(mBackground);
            if(isVertical)
            {
                mBackground.setScaleType(ImageView.ScaleType.CENTER);
            }
            else {
                mBackground.setScaleType(ImageView.ScaleType.FIT_XY);
            }



        }

    }

    // TODO: Rename and change types and number of parameters
    public static MediaPlaybackFragment newInstance(String NameRe, String ArtistRe, String ImageRe) {
        Log.d("HoangLD1", "getSong: " + ImageRe);

        MediaPlaybackFragment fragment = new MediaPlaybackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, NameRe);
        args.putString(ARG_PARAM2, ArtistRe);
        args.putString(ARG_PARAM3, ImageRe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Name = getArguments().getString(ARG_PARAM1);
            Artist = getArguments().getString(ARG_PARAM2);
            Image = getArguments().getString(ARG_PARAM3);
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.PlayMedia: {
                if (mMediaPlaybackService.getMediaManager().getMediaPlayer().isPlaying()) {
                    mMediaPlaybackService.getMediaManager().pauseSong();
                    mPlayMedia.setImageResource(R.drawable.ic_play_media);

                } else {
                    mMediaPlaybackService.getMediaManager().resumeSong();
                    mPlayMedia.setImageResource(R.drawable.ic_pause_media);
                }
                break;
            }
            case R.id.btn_next_media:
                Log.d("HoangLD", "onClick: next");
                mMediaPlaybackService.getMediaManager().nextSong();
                setData();
                if (mMediaPlaybackService.getMediaManager().getMediaPlayer().isPlaying()) {
                    mPlayMedia.setImageResource(R.drawable.ic_play_media);
                } else {
                    mPlayMedia.setImageResource(R.drawable.ic_pause_media);
                }
                break;


            case R.id.btn_pre_media:

                mMediaPlaybackService.getMediaManager().previousSong();
                setData();
                if (mMediaPlaybackService.getMediaManager().getMediaPlayer().isPlaying()) {
                    mPlayMedia.setImageResource(R.drawable.ic_play_media);
                } else {
                    mPlayMedia.setImageResource(R.drawable.ic_pause_media);
                }
                break;
            case R.id.button_Shuffle:
                if(mMediaPlaybackService.getMediaManager().getShuffle())
                {
                    mButtonShuffle.setImageResource(R.drawable.ic_shuffle_white);
                    mMediaPlaybackService.getMediaManager().setShuffle(false);
                }else {
                    mButtonShuffle.setImageResource(R.drawable.ic_play_shuffle_orange);
                    mMediaPlaybackService.getMediaManager().setShuffle(true);
                }

                break;
            case R.id.button_Repeat:
                if(mMediaPlaybackService.getMediaManager().isRepeatAll())
                {
                    mMediaPlaybackService.getMediaManager().setRepeat(false);
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_white);
                    mMediaPlaybackService.getMediaManager().setRepeatAll(false);
                }else if(mMediaPlaybackService.getMediaManager().isRepeat) {

                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_dark_selected);
                    mMediaPlaybackService.getMediaManager().setRepeatAll(true);
                    mMediaPlaybackService.getMediaManager().setRepeat(false);
                }
                else
                {
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_one_song_dark);
                    mMediaPlaybackService.getMediaManager().setRepeat(true);
                    mMediaPlaybackService.getMediaManager().setRepeatAll(false);
                }

                break;
        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void updateUiSongPlay(int pos) {
        setData();
        if (mMediaPlaybackService.getMediaManager().getMediaPlayer().isPlaying()) {
            mPlayMedia.setImageResource(R.drawable.ic_play_media);
        } else {
            mPlayMedia.setImageResource(R.drawable.ic_pause_media);
        }

    }
}