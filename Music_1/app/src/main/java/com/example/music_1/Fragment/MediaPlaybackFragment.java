package com.example.music_1.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import android.app.Fragment;

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


public class MediaPlaybackFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private TextView mName, mArtist;
    ImageView mImage, mBackground;
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private MediaPlaybackService mMediaPlaybackService;
    private ImageView mPlayMedia;


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
        mPlayMedia.setOnClickListener(this);
        mBackMedia.setOnClickListener(this);
        mNextMedia.setOnClickListener(this);
        if (Image != null) {
            final byte[] songArt = getAlbumArt(Image);
            Glide.with(view.getContext()).asBitmap()
                    .load(songArt)
                    .error(R.drawable.cute)
                    .into(mImage);
            Glide.with(view.getContext()).asBitmap()
                    .load(songArt)
                    .error(R.drawable.background)
                    .into(mBackground);
        }

        mBackground.setScaleType(ImageView.ScaleType.FIT_XY);
        mName.setText(Name);
        mArtist.setText(Artist);


    }

    @Override
    public void onStart() {
        setService();
        super.onStart();
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
            mListMedia= mMediaPlaybackService.getMediaManager().getmListSong();
            setData();

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
    public void setData() {
        if (mMediaPlaybackService != null) {


            if (mMediaPlaybackService.getMediaManager().getMediaPlayer().isPlaying()) {
                mPlayMedia.setImageResource(R.drawable.ic_pause_media);

            } else {
                mPlayMedia.setImageResource(R.drawable.ic_play_media);
            }
            //int mCurrent = mMediaPlaybackService.getMediaManager().getCurrentSong();
            mName.setText(mListMedia.get(mMediaPlaybackService.getMediaManager().getCurrentSong()).getSongName());
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
                break;


            case R.id.btn_pre_media:

                mMediaPlaybackService.getMediaManager().previousSong();
                break;
        }


    }
}