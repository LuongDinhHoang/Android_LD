package com.example.music_1.Fragment;

import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_1.MainActivity;
import com.example.music_1.Model.Song;
import com.example.music_1.R;

import java.util.ArrayList;
import java.util.List;

import Services.MediaPlaybackService;

import static com.example.music_1.Fragment.AllSongsFragment.getAlbumArt;


public class MediaPlaybackFragment extends Fragment implements View.OnClickListener, MediaPlaybackService.SongManageListener,MediaPlaybackService.notificationData
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private TextView mName, mArtist;
    ImageView mImage, mBackground;
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

//    public void setMediaPlaybackService(MediaPlaybackService mMediaPlaybackService) {
//        this.mMediaPlaybackService = mMediaPlaybackService;
//    }

    private MediaPlaybackService mMediaPlaybackService;
    private ImageView mPlayMedia, mButtonShuffle, mButtonRepeat, mButtonList;

    public void setVertical(boolean vertical) {
        isVertical = vertical;
    }
    private boolean isVertical;


    public void setListMedia(List<Song> mListMedia) {
        this.mListMedia = mListMedia;
    }

    private List<Song> mListMedia = new ArrayList<>();
    private ImageButton mNextMedia, mBackMedia;
    private TextView mEndTime,mStartTime;


    private SeekBar mMediaSeekBar;
    private UpdateSeekBarThread mUpdateSeekBarThread;


    // TODO: Rename and change types of parameters
    private String Name;
    private String Artist;
    private String Image;
    View view;

    public MediaPlaybackFragment() {
        // Required empty public constructor
    }
    private MediaPlaybackService getMusicService(){
        return getActivityMusic().getMediaPlaybackService();
    }
    private List<Song> getListSong(){
        return getActivityMusic().getList();
    }
    //get activity
    private MainActivity getActivityMusic() {
        if (getActivity() instanceof MainActivity) {
            return (MainActivity) getActivity();
        }
        return null;
    }
    public void setDataMedia(){
        mMediaPlaybackService= getMusicService();
        mListMedia=getListSong();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_playback, container, false);
//        if(isVertical)
//        {
//            mListMedia = mMediaPlaybackService.getMediaManager().getmListSong();
//
//        }
        if (mMediaPlaybackService != null)
        {
            mMediaPlaybackService.setListener(MediaPlaybackFragment.this);//get vao

        }
        initView();
        if (mMediaPlaybackService != null && mMediaPlaybackService.getCurrentSong()>=0) {

                if (mMediaPlaybackService.getShuffle()) {
                    mButtonShuffle.setImageResource(R.drawable.ic_play_shuffle_orange);
                } else {
                    mButtonShuffle.setImageResource(R.drawable.ic_shuffle_white);
                }
                if (mMediaPlaybackService.isRepeat) {
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_one_song_dark);
                }
                if (mMediaPlaybackService.isRepeatAll) {
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_dark_selected);
                }

        }
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
        mButtonShuffle = view.findViewById(R.id.button_Shuffle);
        mButtonRepeat = view.findViewById(R.id.button_Repeat);
        mButtonList = view.findViewById(R.id.btn_backList);
        mEndTime = view.findViewById(R.id.end_time);
        mStartTime=view.findViewById(R.id.start_time);
//        mMediaSeekBar.setMax((int) (mSongCurrentDuration));
//        mMediaSeekBar.setProgress((int) (mSongCurrentStreamPossition));
        mMediaSeekBar = view.findViewById(R.id.media_seek_bar);
        mButtonList.setOnClickListener(this);
        mButtonRepeat.setOnClickListener(this);
        mButtonShuffle.setOnClickListener(this);
        mPlayMedia.setOnClickListener(this);
        mBackMedia.setOnClickListener(this);
        mNextMedia.setOnClickListener(this);
        setDataMedia();
        if(mListenerMedia!=null)
        {
            mMediaPlaybackService.setListener(MediaPlaybackFragment.this);
        }
        Log.d("HoangLD", "initView:medisa "+isVertical);
        if (isVertical) {
            mBackground.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            mBackground.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mButtonList.setVisibility(View.INVISIBLE);
        }
        setData();
        mMediaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mMediaPlaybackService != null && b) {
                    mMediaPlaybackService.seekTo(i);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //  mName.setText(mListMedia.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongName());


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        mUpdateSeekBarThread.exit();
        super.onDestroy();
    }


    private boolean mIsConnect;

    private ServiceConnection mServiceConnection;

    private void updateUI() {
        if (mMediaPlaybackService != null) {
//
        }
    }

    public  String getTimeDurationString(long s )
    {

//        String mm = String.valueOf(mSongTime/60000);
        String seconds = String.valueOf((s/1000) % 60);
        String minutes = String.valueOf((s/1000) / 60);
        if (minutes.length() <= 1) minutes = "0" + minutes;
        if (seconds.length() <= 1) seconds = "0" + seconds;
        return minutes +":"+seconds;
    }
    ////////////////////////////////////
    public void setData() {
        if (mMediaPlaybackService != null) {
            if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
                mPlayMedia.setImageResource(R.drawable.ic_pause_media);
            } else {
                mPlayMedia.setImageResource(R.drawable.ic_play_media);
            }
            Log.d("HoangLD", "setData: " + mName);
            mName.setText(mListMedia.get(mMediaPlaybackService.getCurrentSong()).getSongName());
            mArtist.setText(mListMedia.get(mMediaPlaybackService.getCurrentSong()).getSongArtist());
            mEndTime.setText(getTimeDurationString(mListMedia.get(mMediaPlaybackService.getCurrentSong()).getSongTime()));
            final byte[] songArt = getAlbumArt(mListMedia.get(mMediaPlaybackService.getCurrentSong()).getSongImage());
            Glide.with(view.getContext()).asBitmap()
                    .load(songArt)
                    .error(R.drawable.cute)
                    .into(mImage);
            Glide.with(view.getContext()).asBitmap()
                    .load(songArt)
                    .error(R.drawable.background)
                    .into(mBackground);

            UpdateUIRunSeeBar();
        }

    }
    public  void UpdateUIRunSeeBar()
    {
        if (mMediaPlaybackService != null) {
            mUpdateSeekBarThread.updateSeekBar();
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

    public class UpdateSeekBarThread extends Thread {
        private Handler handler;

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            handler = new Handler();
            Looper.loop();
        }

        public void updateSeekBar() {
            if (mMediaPlaybackService != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
                            while (mMediaPlaybackService.getPlayer() != null) {
                                try {
                                    long current = -1;
                                    try {
                                        current = mMediaPlaybackService.getCurrentStreamPosition();
                                    } catch (IllegalStateException e) {
//                                    e.printStackTrace();
                                    }
                                    if (getActivity() != null) {
                                        final long finalCurrent = current;
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mMediaSeekBar.setMax((int) (mMediaPlaybackService.getDuration()));
                                                mMediaSeekBar.setProgress((int) (finalCurrent));
                                                mStartTime.setText(getTimeDurationString(finalCurrent));
                                            }
                                        });
                                    }
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }
        public void exit() {
            handler.getLooper().quit();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Name = getArguments().getString(ARG_PARAM1);
            Artist = getArguments().getString(ARG_PARAM2);
            Image = getArguments().getString(ARG_PARAM3);
        }
        mUpdateSeekBarThread = new UpdateSeekBarThread();
        mUpdateSeekBarThread.start();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.PlayMedia: {
                if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
                    mMediaPlaybackService.pauseSong();
                    mPlayMedia.setImageResource(R.drawable.ic_play_media);

                } else {
                    mMediaPlaybackService.resumeSong();
                    mPlayMedia.setImageResource(R.drawable.ic_pause_media);
                }
                int mCurrentNext =mMediaPlaybackService.getCurrentSong();
                Song song = mListMedia.get(mCurrentNext);
                mMediaPlaybackService.createChannel();
                mMediaPlaybackService.createNotification(getActivity(),song,mCurrentNext);
                break;
            }
            case R.id.btn_next_media:

                mMediaPlaybackService.nextSong();
                setData();
                if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
                    mPlayMedia.setImageResource(R.drawable.ic_play_media);
                } else {
                    mPlayMedia.setImageResource(R.drawable.ic_pause_media);
                }
                int mCurrentNext =mMediaPlaybackService.getCurrentSong();
                Song song = mListMedia.get(mCurrentNext);
                mMediaPlaybackService.createChannel();
                mMediaPlaybackService.createNotification(getActivity(),song,mCurrentNext+1);
                if(!isVertical)
                {
                    Log.d("HoangLD", "onClick: mlistner"+mListenerMedia);
                    if (mListenerMedia != null) {
                        Log.d("HoangLD", "onClick:next ");

                        mListenerMedia.updateUiSongPlayMedia();
                    }
                }

//                if (mMediaPlaybackService.getMediaManager().mListener != null) {
//                    Log.d("HoangLD", "nhay");
//                    mMediaPlaybackService.getMediaManager().mListener.updateUiSongPlay(mCurrentNext);
//                    mMediaPlaybackService.getMediaManager().setCurrentSong(mCurrentNext);
//                }
                break;

            case R.id.btn_pre_media:

                mMediaPlaybackService.previousSong();
                setData();
                if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
                    mPlayMedia.setImageResource(R.drawable.ic_play_media);
                } else {
                    mPlayMedia.setImageResource(R.drawable.ic_pause_media);
                }
                int mCurrentPre =mMediaPlaybackService.getCurrentSong();
                Song songPre = mListMedia.get(mCurrentPre);
                mMediaPlaybackService.createChannel();
                mMediaPlaybackService.createNotification(getActivity(),songPre,mCurrentPre-1);
                if(!isVertical)
                {
                    if (mListenerMedia != null) {
                        mListenerMedia.updateUiSongPlayMedia();
                    }
                }
                break;
            case R.id.button_Shuffle:
                if (mMediaPlaybackService.getShuffle()) {
                    mButtonShuffle.setImageResource(R.drawable.ic_shuffle_white);
                    mMediaPlaybackService.setShuffle(false);
                } else {
                    mButtonShuffle.setImageResource(R.drawable.ic_play_shuffle_orange);
                    mMediaPlaybackService.setShuffle(true);
                }

                break;
            case R.id.button_Repeat:
                if (mMediaPlaybackService.isRepeat()) {
                    mMediaPlaybackService.setRepeat(false);
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_white);
                    mMediaPlaybackService.setRepeatAll(false);
                } else if (mMediaPlaybackService.isRepeatAll) {
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_one_song_dark);
                    mMediaPlaybackService.setRepeat(true);
                    mMediaPlaybackService.setRepeatAll(false);
                } else {
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_dark_selected);
                    mMediaPlaybackService.setRepeatAll(true);
                    mMediaPlaybackService.setRepeat(false);
                }

                break;
            case R.id.btn_backList:
                getActivity().getSupportFragmentManager().popBackStack();
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();// hide action bar

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    ///interface updateUi tu chuyen bai

    @Override
    public void updateUiSongPlay(int pos) {
        Log.d("HoangLD", "updateUiSongPlay:media");
        setData();
        if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
            mPlayMedia.setImageResource(R.drawable.ic_play_media);
        } else {
            mPlayMedia.setImageResource(R.drawable.ic_pause_media);
        }
    }
    public interface SongManageListenerMedia {
        void updateUiSongPlayMedia();
    }
    private SongManageListenerMedia mListenerMedia;

    public void setListenerMedia(SongManageListenerMedia mListenerMedia) {
        this.mListenerMedia = mListenerMedia;
    }
    ////InterfaceNotification updateui notifiction click

    @Override
    public void updateData() {
        setData();
        if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
            mPlayMedia.setImageResource(R.drawable.ic_play_media);
        } else {
            mPlayMedia.setImageResource(R.drawable.ic_pause_media);
        }

    }

}