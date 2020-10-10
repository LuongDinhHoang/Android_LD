package com.example.music_1.Fragment;

import android.content.ContentValues;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.music_1.Adapter.SongAdapter;
import com.example.music_1.MainActivity;
import com.example.music_1.Model.Song;
import com.example.music_1.MusicDB;
import com.example.music_1.MusicProvider;
import com.example.music_1.R;

import java.util.ArrayList;
import java.util.List;

import com.example.music_1.Services.MediaPlaybackService;

import static com.example.music_1.Fragment.AllSongsFragment.getAlbumArt;


public class MediaPlaybackFragment extends Fragment implements View.OnClickListener,MediaPlaybackService.SongManageListenerMedia,MediaPlaybackService.notificationUpdateMedia
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private TextView mName, mArtist;
    ImageView mImage, mBackground;
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private MediaPlaybackService mMediaPlaybackService;
    private ImageView mPlayMedia, mButtonShuffle, mButtonRepeat, mButtonList,mButtonLike, mButtonDisLike;
    private boolean isVertical,isFavorite=false;
    public void setVertical(boolean vertical) {
        isVertical = vertical;
    }
    private SongAdapter mAdapter;
    public void setListMedia(List<Song> mListMedia) {
        this.mListMedia = mListMedia;
    }
    private List<Song> mSongListFavorite = new ArrayList<>();
    private List<Song> mListMedia = new ArrayList<>();
    private ImageButton mNextMedia, mBackMedia;
    private TextView mEndTime,mStartTime;
    private SeekBar mMediaSeekBar;
    private int mSongCurrentId = -1;
    private UpdateSeekBarThread mUpdateSeekBarThread;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferencesCurrent,sharedPreferencesStream;
    SharedPreferences.Editor editorCurrent;

    // TODO: Rename and change types of parameters
    private String Name;
    private String Artist;
    private String Image;
    View view;

    public MediaPlaybackFragment() {
        // Required empty public constructor
    }
    private SongAdapter getSongAdapter(){
        return getActivityMusic().getAdapter();
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
//        mListMedia=getListSong();
//        mAdapter=getSongAdapter();
        mListMedia=getListSong();
        mSongListFavorite = Song.getSongFavorite(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media_playback, container, false);

        initView();
        if (mMediaPlaybackService != null && mMediaPlaybackService.getCurrentSong()>=0) {
            int shuffle = mMediaPlaybackService.isShuffle();
            if (shuffle != MediaPlaybackService.SHUFFLE) {
                mButtonShuffle.setImageResource(R.drawable.ic_shuffle_white);
            } else {
                mButtonShuffle.setImageResource(R.drawable.ic_play_shuffle_orange);
            }

            int repeat = mMediaPlaybackService.isRepeat();
            if (repeat == MediaPlaybackService.REPEAT) {
                mButtonRepeat.setImageResource(R.drawable.ic_repeat_one_song_dark);
            } else if (repeat == MediaPlaybackService.REPEAT_ALL) {
                mButtonRepeat.setImageResource(R.drawable.ic_repeat_dark_selected);
            } else {
                mButtonRepeat.setImageResource(R.drawable.ic_repeat_white);
            }
        }
        checkSongFavorite();

        return view;
    }
    public void checkSongFavorite() {
        if (mMediaPlaybackService != null) {
            long idPlay = mMediaPlaybackService.getCurrentSongId();
            for (int i = 0; i < mSongListFavorite.size(); i++) {
                if (mSongListFavorite.get(i).getSongID() == idPlay) {
                    isFavorite = true;
                }
//                else isFavorite=false;
            }
        }
        if(isFavorite)
        {
            mButtonLike.setImageResource(R.drawable.ic_thumbs_up_selected);
            mButtonDisLike.setImageResource(R.drawable.ic_thumbs_down_default);
        }
        else
        {
            mButtonLike.setImageResource(R.drawable.ic_thumbs_up_default);
            mButtonDisLike.setImageResource(R.drawable.ic_thumbs_down_selected);
        }

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
        mButtonLike=view.findViewById(R.id.media_like);
        mButtonDisLike =view.findViewById(R.id.media_dislike);
//        mMediaSeekBar.setMax((int) (mSongCurrentDuration));
//        mMediaSeekBar.setProgress((int) (mSongCurrentStreamPossition));
        mMediaSeekBar = view.findViewById(R.id.media_seek_bar);
        mButtonLike.setOnClickListener(this);
        mButtonDisLike.setOnClickListener(this);
        mButtonList.setOnClickListener(this);
        mButtonRepeat.setOnClickListener(this);
        mButtonShuffle.setOnClickListener(this);
        mPlayMedia.setOnClickListener(this);
        mBackMedia.setOnClickListener(this);
        mNextMedia.setOnClickListener(this);
        setDataMedia();

         //mMediaPlaybackService.setListener(MediaPlaybackFragment.this);//set interface tu chuyen bai

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
            if (mMediaPlaybackService.isPlay()) {
                mPlayMedia.setImageResource(R.drawable.ic_pause_media);
            } else {
                mPlayMedia.setImageResource(R.drawable.ic_play_media);
            }
            if(mMediaPlaybackService.getCurrentSong()<0)
            {
                int current = sharedPreferencesCurrent.getInt("DATA_CURRENT", -1);
                int position=sharedPreferencesCurrent.getInt("DATA_CURRENT_STREAM_POSITION",0);
                Log.d("HoangLD", "setData: " + mName);
                mName.setText(mMediaPlaybackService.getListSong().get(mMediaPlaybackService.getCurrentSong()).getSongName());
                mArtist.setText(mMediaPlaybackService.getListSong().get(mMediaPlaybackService.getCurrentSong()).getSongArtist());
                mEndTime.setText(getTimeDurationString(mMediaPlaybackService.getListSong().get(mMediaPlaybackService.getCurrentSong()).getSongTime()));
                final byte[] songArt = getAlbumArt(mMediaPlaybackService.getListSong().get(mMediaPlaybackService.getCurrentSong()).getSongImage());
                Glide.with(view.getContext()).asBitmap()
                        .load(songArt)
                        .error(R.drawable.cute)
                        .into(mImage);
                Glide.with(view.getContext()).asBitmap()
                        .load(songArt)
                        .error(R.drawable.background)
                        .into(mBackground);

                mMediaSeekBar.setMax((int) mListMedia.get(current).getSongTime());
                mMediaSeekBar.setProgress(position);

            }else {

                long id = mMediaPlaybackService.getCurrentSongId();
                int current = 0;
                for (int i = 0; i < mListMedia.size(); i++) {
                    if (mListMedia.get(i).getSongID() == id) {
                        current = i;
                    }

                }
                Log.d("HoangLD", "setData: " + mName);
                mName.setText(mListMedia.get(current).getSongName());
                mArtist.setText(mListMedia.get(current).getSongArtist());
                mEndTime.setText(getTimeDurationString(mListMedia.get(current).getSongTime()));
                final byte[] songArt = getAlbumArt(mListMedia.get(current).getSongImage());
                Glide.with(view.getContext()).asBitmap()
                        .load(songArt)
                        .error(R.drawable.cute)
                        .into(mImage);
                Glide.with(view.getContext()).asBitmap()
                        .load(songArt)
                        .error(R.drawable.background)
                        .into(mBackground);
                int position=sharedPreferencesCurrent.getInt("DATA_CURRENT_STREAM_POSITION",0);
                mMediaSeekBar.setMax((int) mListMedia.get(mMediaPlaybackService.getCurrentSong()).getSongTime());
                mMediaSeekBar.setProgress(position);
                mStartTime.setText(getTimeDurationString(position));
                if(!mMediaPlaybackService.isFirst()) UpdateUIRunSeeBar();
            }
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

    @Override
    public void updateUiMediaSong(int pos) {
        setData();
        if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
            mPlayMedia.setImageResource(R.drawable.ic_play_media);
        } else {
            mPlayMedia.setImageResource(R.drawable.ic_pause_media);
        }
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
                        if (mMediaPlaybackService.getCurrentSong()>=0) {
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
        sharedPreferences = getActivity().getSharedPreferences("DATA_PLAY_MEDIA", getActivity().MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferencesCurrent = getActivity().getSharedPreferences("DATA_CURRENT_PLAY", getActivity().MODE_PRIVATE);
        editorCurrent = sharedPreferences.edit();

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
                    if (!mMediaPlaybackService.isResumeRe()) {
                        mMediaPlaybackService.resumeSong();
                    } else {
                        int position = sharedPreferencesCurrent.getInt("DATA_CURRENT_STREAM_POSITION", 0);
                        mMediaPlaybackService.playSong(mListMedia.get(mMediaPlaybackService.getCurrentSong()).getSongImage());
                        mMediaPlaybackService.seekTo(position);
                        UpdateUIRunSeeBar();
                    }
                    mPlayMedia.setImageResource(R.drawable.ic_pause_media);
                }
                if (!isVertical) {
                    mPlayPauseMedia.updateBase();
                }
                int mCurrentNext = mMediaPlaybackService.getCurrentSong();
                Song song = mListMedia.get(mCurrentNext);
                mMediaPlaybackService.startForegroundService(song, mCurrentNext);
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
                if (!isVertical) {
                    Log.d("HoangLD", "onClick: mlistner" + mListenerMedia);
                    if (mListenerMedia != null) {
                        Log.d("HoangLD", "onClick:next ");
                        mListenerMedia.updateUiSongPlayMedia();
                    }
                }
                isFavorite=false;
                checkSongFavorite();

                break;

            case R.id.btn_pre_media:
                mMediaPlaybackService.previousSong();
                setData();
                if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
                    mPlayMedia.setImageResource(R.drawable.ic_play_media);
                } else {
                    mPlayMedia.setImageResource(R.drawable.ic_pause_media);
                }

                if (!isVertical) {
                    if (mListenerMedia != null) {
                        mListenerMedia.updateUiSongPlayMedia();
                    }
                }
                isFavorite=false;
                checkSongFavorite();
                break;
            case R.id.button_Shuffle:
                int shuffle = mMediaPlaybackService.isShuffle();
                if (shuffle == MediaPlaybackService.SHUFFLE) {
                    mMediaPlaybackService.setShuffle(MediaPlaybackService.NORMAL);
                    mButtonShuffle.setImageResource(R.drawable.ic_shuffle_white);
                    editor.remove("DATA_SHUFFLE");
                    editor.putInt("DATA_SHUFFLE", MediaPlaybackService.NORMAL);
                    editor.commit();
                } else {
                    mMediaPlaybackService.setShuffle(MediaPlaybackService.SHUFFLE);
                    mButtonShuffle.setImageResource(R.drawable.ic_play_shuffle_orange);
                    editor.remove("DATA_SHUFFLE");
                    editor.putInt("DATA_SHUFFLE", MediaPlaybackService.SHUFFLE);
                    editor.commit();

                }


                break;
            case R.id.button_Repeat:
                int repeat = mMediaPlaybackService.isRepeat();
                if (repeat == MediaPlaybackService.REPEAT) {
                    mMediaPlaybackService.setRepeat(MediaPlaybackService.NORMAL);
                    editor.remove("DATA_REPEAT");
                    editor.putInt("DATA_REPEAT", MediaPlaybackService.NORMAL);
                    editor.commit();
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_white);
                } else if (repeat == MediaPlaybackService.REPEAT_ALL) {
                    mMediaPlaybackService.setRepeat(MediaPlaybackService.REPEAT);
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_one_song_dark);
                    editor.remove("DATA_REPEAT");
                    editor.putInt("DATA_REPEAT", MediaPlaybackService.REPEAT);
                    editor.commit();
                } else {
                    mMediaPlaybackService.setRepeat(MediaPlaybackService.REPEAT_ALL);
                    mButtonRepeat.setImageResource(R.drawable.ic_repeat_dark_selected);
                    editor.remove("DATA_REPEAT");
                    editor.putInt("DATA_REPEAT", MediaPlaybackService.REPEAT_ALL);
                    editor.commit();
                }
                ///////////////////////


                break;
            case R.id.btn_backList:
                getActivity().getSupportFragmentManager().popBackStack();
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();// hide action bar\
                break;

            case R.id.media_like:
                mButtonLike.setImageResource(R.drawable.ic_thumbs_up_selected);
                mButtonDisLike.setImageResource(R.drawable.ic_thumbs_down_default);
                isFavorite = true;
                Song song = mMediaPlaybackService.getListSong().get(mMediaPlaybackService.getCurrentSong());
                ContentValues values = new ContentValues();
                values.put(MusicDB.ID, song.getSongID());
                values.put(MusicDB.ID_PROVIDER, song.getSongIDProvider());
                values.put(MusicDB.TITLE, song.getSongName());
                values.put(MusicDB.ARTIST, song.getSongArtist());
                values.put(MusicDB.DATA, song.getSongImage());
                values.put(MusicDB.DURATION, song.getSongTime());
                values.put(MusicDB.IS_FAVORITE, 2);
                getContext().getContentResolver().insert(MusicProvider.CONTENT_URI, values);
                Toast.makeText(getActivity().getApplicationContext(), "Add Favorite", Toast.LENGTH_SHORT).show();
                break;

            case R.id.media_dislike:
                isFavorite = false;
                mButtonDisLike.setImageResource(R.drawable.ic_thumbs_down_selected);
                mButtonLike.setImageResource(R.drawable.ic_thumbs_up_default);
                Song song1 = mMediaPlaybackService.getListSong().get(mMediaPlaybackService.getCurrentSong());
                final int id = (int) song1.getSongID();
                final Uri uri = Uri.parse(MusicProvider.CONTENT_URI + "/" + id);
                final Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
                ContentValues values1 = new ContentValues();
                values1.put(MusicDB.IS_FAVORITE, 0);
                if (cursor != null) {
                    getContext().getContentResolver().delete(MusicProvider.CONTENT_URI, MusicDB.ID + "=" + id, null);

                }
                Toast.makeText(getActivity().getApplicationContext(), "Remove Favorite", Toast.LENGTH_SHORT).show();
                break;

        }
    }

//////////// menu Search
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    ///interface updateUi tu chuyen bai

//    @Override
//    public void updateUiSongPlay(int pos) {
//        Log.d("HoangLD", "updateUiSongPlay:media");
//        setData();
//
//    }
    public interface SongManageListenerMedia {
        void updateUiSongPlayMedia();
    }
    private SongManageListenerMedia mListenerMedia;

    public void setListenerMedia(SongManageListenerMedia mListenerMedia) {
        this.mListenerMedia = mListenerMedia;
    }
    ////InterfaceNotification updateui notifiction click
    @Override
    public void updateDataMedia() {
        setData();
    }
//interface ngang
    public interface PlayPauseMedia{
        void updateBase();
}

    public void setPlayPauseMedia(PlayPauseMedia mPlayPauseMedia) {
        this.mPlayPauseMedia = mPlayPauseMedia;
    }

    private PlayPauseMedia mPlayPauseMedia;

    ////////////update ngang click
    public void updateMediaWhenClickItem(int pos) {
        isFavorite=false;
        checkSongFavorite();
    }


}
