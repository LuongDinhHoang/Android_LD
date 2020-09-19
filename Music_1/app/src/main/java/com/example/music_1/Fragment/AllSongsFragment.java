package com.example.music_1.Fragment;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_1.Adapter.SongAdapter;
import com.example.music_1.MainActivity;
import com.example.music_1.Model.Song;
import com.example.music_1.R;

import java.util.List;

import Services.MediaPlaybackService;


public class AllSongsFragment extends Fragment implements View.OnClickListener, MediaPlaybackService.SongManageListener,MediaPlaybackFragment.SongManageListenerMedia,
        SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener,MediaPlaybackService.notificationUpdateAllSong {


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

//    public void setMediaPlaybackService(MediaPlaybackService mMediaPlaybackService) {
//        this.mMediaPlaybackService = mMediaPlaybackService;
//    }
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
    private SongAdapter getSongAdapter(){
        return getActivityMusic().getAdapter();
    }

    public void setData(){
        mMediaPlaybackService= getMusicService();
        mList=getListSong();
        mAdapter=getSongAdapter();
    }


    public void setCheck(boolean mCheck) {
        this.isVertical = mCheck;
    }

    private boolean isVertical;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_songs, container, false);
        setData();
        initView();

//        onConnectService();
        Log.d("HoangLD", "onCreateView: ");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        if (mMediaPlaybackService != null) {
            Log.d("HoangLD", "onResume: ");
            mMediaPlaybackService.setListener(AllSongsFragment.this);//get vao
               // mllBottom.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    private void initView() {

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
//        mAdapter = new SongAdapter(getActivity(), mList);
        mRecycle.setAdapter(mAdapter);
        mAdapter.setMediaPlaybackService(mMediaPlaybackService);//set data on adapter



        onConnectService();

        //mMediaPlaybackService.getMediaManager().setListener(AllSongsFragment.this);//get vao
        if (mMediaPlaybackService != null && mMediaPlaybackService.getMediaPlayer().isPlaying()) {

                UpdateUI();
        }
        mAdapter.setClick(
                new SongAdapter.IIClick() {
                    @Override
                    public void ItemClick(Song song, int pos) {
                            //pos=song.getPos();
                            for (int i = 0; i < mList.size(); i++) {
                                mList.get(i).setPlay(false);
                            }
                            mList.get(pos).setPlay(true);
                            //service
                            if (mMediaPlaybackService != null) {
                                mMediaPlaybackService.playSong(song.getSongImage());
                                mMediaPlaybackService.setCurrentSong(pos);//get position
                                mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);
                                //   mMediaPlaybackService.getMediaManager().setCurrentSong(pos);
                            }
                            if (isVertical) {
                                if(mMediaPlaybackService.getCurrentSong()>=0)
                                {
                                    mllBottom.setVisibility(View.VISIBLE);

                                }
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
                                ImageView mImagePlaying = getActivityMusic().findViewById(R.id.PlayMedia);
                                TextView mTimeEnd = getActivity().findViewById(R.id.end_time);
                                mArtistMedia.setText(song.getSongArtist());
                                mNameMedia.setText(song.getSongName());
                                if(mMediaPlaybackService.isPlay())
                                {
                                    mImagePlaying.setImageResource(R.drawable.ic_pause_media);

                                }
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

                            mMediaPlaybackService.createChannel();
                            mMediaPlaybackService.createNotification(getActivity(),song,pos);
//                mAdapter.setPos(pos);


                        }


                }
        );
//        mAdapter.SongAdapter(new SongAdapter.IIClick() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//
//        });


    }
    public  void  onConnectService()
    {
        if (mMediaPlaybackService !=null)
        {
            if (mMediaPlaybackService != null && mMediaPlaybackService.getCurrentSong()>=0) {
                if (isVertical) {
                    mllBottom.setVisibility(View.VISIBLE);
                } else {
                    mllBottom.setVisibility(View.GONE);
                }
                if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
                    mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);
                } else {
                    mBtnPlay.setImageResource(R.drawable.ic_play_black);
                }
                UpdateUI();
            }
                mAdapter.notifyDataSetChanged();

            }

    }

    public void UpdateUI() {
        if (mMediaPlaybackService != null)
        {
            mNameSongPlay.setText(mList.get(mMediaPlaybackService.getCurrentSong()).getSongName());
            mSongArtistPlay.setText(mList.get(mMediaPlaybackService.getCurrentSong()).getSongArtist());
            final byte[] songArt = getAlbumArt(mList.get(mMediaPlaybackService.getCurrentSong()).getSongImage());
            Glide.with(view.getContext()).asBitmap()
                    .load(songArt)
                    .error(R.drawable.cute)
                    .into(mImagePlay);
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setPlay(false);
            }
            if (!mMediaPlaybackService.isPlay()) {
                mBtnPlay.setImageResource(R.drawable.ic_play_black);
            } else {
                mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);
            }
            mList.get(mMediaPlaybackService.getCurrentSong()).setPlay(true);
        }
        mAdapter.notifyDataSetChanged();

    }
    public void  UpdateUICreate()
    {
        if(mMediaPlaybackService!= null && mMediaPlaybackService.getCurrentSong() >=0)
        {

        }
    }

    public static byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(uri);
        byte[] albumArt = mediaMetadataRetriever.getEmbeddedPicture();  // chuyển đổi đường dẫn file media thành đường dẫn file Ảnh
        mediaMetadataRetriever.release();
        return albumArt;
    }

//    public void getSong() {
//        ContentResolver musicResolver = getActivity().getContentResolver();
//        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Cursor songCursor = musicResolver.query(songUri, null, null, null, null);
//
//        if (songCursor != null && songCursor.moveToFirst()) {
//            if (songCursor != null && songCursor.moveToFirst()) {
//                do {
//                    int songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
//                    int songName = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
//                    long songTime = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
//                    int songAuthor = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//                    int songArt = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
//                    long currentId = songCursor.getLong(songID);
//                    String currentName = songCursor.getString(songName);
//                    String currentAuthor = songCursor.getString(songAuthor);
//                    String currentArt = songCursor.getString(songArt);
//
//
//                    mList.add(new Song(currentId, currentName, songTime, currentAuthor, currentArt, false));
//                } while (songCursor.moveToNext());
//            }
//        }
//    }

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
                mediaPlaybackFragment.setVertical(true);
                mMediaPlaybackService.setNotificationDataMedia(mediaPlaybackFragment);
                mMediaPlaybackService.mListenerMe(mediaPlaybackFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.Button_Play: {
                if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
                    mMediaPlaybackService.pauseSong();
                    mBtnPlay.setImageResource(R.drawable.ic_play_black);
                } else {
                    mMediaPlaybackService.resumeSong();
                    mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);
                }
                mAdapter.notifyDataSetChanged();
                int pos = mMediaPlaybackService.getCurrentSong();
                Song song = getListSong().get(pos);
                mMediaPlaybackService.createChannel();
                mMediaPlaybackService.createNotification(getActivity(),song,pos);
            }
        }
    }
    @Override
    public void updateUiSongPlay(int pos) {//interface tu dong update het bai
        //mPosition = pos;
        Log.d("HoangLD", "het bai ");
      UpdateUI();
        if (!mMediaPlaybackService.getMediaPlayer().isPlaying()) {
            mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);
        } else {
            mBtnPlay.setImageResource(R.drawable.ic_play_black);
        }
    }
    @Override
    public void updateUiSongPlayMedia() {//interface nhan su kien mediafragment
        Log.d("HoangLD", "mediab next ");
      UpdateUI();
    }

    @Override
    public void updateData() {//interface notification
        Log.d("HoangLD", "notifi next ");
        UpdateUI();
//        if (mMediaPlaybackService.getMediaPlayer().isPlaying()) {
//            mBtnPlay.setImageResource(R.drawable.ic_play_black);
//        } else {
//            mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);
//        }
    }
////////////menu search
@Override
public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu,menu);
    MenuItem searchItem = menu.findItem(R.id.search);
    SearchView searchView = (SearchView) searchItem.getActionView();
    searchView.setOnQueryTextListener(this);
}
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mAdapter.getFilter().filter(s);
        mAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
    }

}