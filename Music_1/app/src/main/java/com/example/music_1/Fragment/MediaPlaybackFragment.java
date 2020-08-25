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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_1.R;

import Services.MediaPlaybackService;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.music_1.Fragment.AllSongsFragment.getAlbumArt;


public class MediaPlaybackFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private TextView mName,mArtist;
    ImageView mImage,mBackground;
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private MediaPlaybackService mediaPlaybackService;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_playback, container, false);
        mName = view.findViewById(R.id.NameSong);
        mArtist = view.findViewById(R.id.ArtistSong);
        mImage=view.findViewById(R.id.ImagePlaying);
        mBackground=view.findViewById(R.id.background_Image);
        final byte[] songArt = getAlbumArt(Image);
        Glide.with(view.getContext()).asBitmap()
                .load(songArt)
                .error(R.drawable.cute)
                .into(mImage);
        Glide.with(view.getContext()).asBitmap()
                .load(songArt)
                .error(R.drawable.background)
                .into(mBackground);
        mBackground.setScaleType(ImageView.ScaleType.FIT_XY);
        mName.setText(Name);
        mArtist.setText(Artist);
        return view;
    }
    public MediaPlaybackFragment() {
        // Required empty public constructor
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
            mediaPlaybackService = binder.getMusicService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mediaPlaybackService = null;
        }
    };
    private void updateUI() {
        if (mediaPlaybackService != null) {

//            tvDuration.setText(currentTime + "/" + totalTime);
//            tvSongName.setText(song.getName());
//            sbMusic.setMax(Integer.parseInt(song.getDuration()));
//            sbMusic.setProgress(currentPosition);
        }
    }
    ////////////////////////////////////

    // TODO: Rename and change types and number of parameters
    public static MediaPlaybackFragment newInstance(String NameRe, String ArtistRe, String ImageRe) {
        Log.d("HoangLD1", "getSong: "+ImageRe);

        MediaPlaybackFragment fragment = new MediaPlaybackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, NameRe);
        args.putString(ARG_PARAM2, ArtistRe);
        args.putString(ARG_PARAM3,ImageRe);
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

}