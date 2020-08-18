package com.example.music_1.Fragment;

import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_1.R;

import static com.example.music_1.Fragment.AllSongsFragment.getAlbumArt;


public class MediaPlaybackFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private TextView mName,mArtist;
    ImageView mImage;
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";


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
        final byte[] songArt = getAlbumArt(Image);
        Glide.with(view.getContext()).asBitmap()
                .load(songArt)
                .error(R.drawable.ic_zing)
                .into(mImage);
        mName.setText(Name);
        mArtist.setText(Artist);
        return view;
    }
    public MediaPlaybackFragment() {
        // Required empty public constructor
    }


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