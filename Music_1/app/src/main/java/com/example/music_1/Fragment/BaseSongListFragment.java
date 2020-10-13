package com.example.music_1.Fragment;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;

import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.music_1.Adapter.SongAdapter;
import com.example.music_1.MainActivity;
import com.example.music_1.Model.Song;
import com.example.music_1.R;

import java.util.ArrayList;
import java.util.List;

import com.example.music_1.Services.MediaPlaybackService;

public abstract class BaseSongListFragment extends Fragment implements View.OnClickListener, MediaPlaybackService.SongManageListener,MediaPlaybackFragment.SongManageListenerMedia,
        SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener,MediaPlaybackService.notificationUpdateAllSong,MediaPlaybackFragment.PlayPauseMedia  {


    protected RecyclerView mRecycle;

    public List<Song> getList() {
        return mList;
    }

    public void setList(List<Song> mList) {
        this.mList = mList;
    }

    protected List<Song> mList,mListFull;
    protected SongAdapter mAdapter;
    private LinearLayout mllBottom;
    private int mPosition;
    private ImageView mImagePlay;
    private TextView mNameSongPlay, mSongArtistPlay, mSongID;
    private ImageView mBtnPlay;
    private View view;
    protected PopupMenu mPopup;
    protected MediaPlaybackService mMediaPlaybackService;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private  boolean isFavorite;

    private MediaPlaybackService getMusicService(){
        return getActivityMusic().getMediaPlaybackService();
    }
    private  List<Song> getListSong(){
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
    public void setListView()

    {
        if(mMediaPlaybackService!=null){
            mListFull= mList=getList();
            if(!isFavorite)
            {
                Log.d("Hoangokokokok","allsong"+mList.size());
                mList=getList();
                mAdapter = new SongAdapter(getContext(), mList);
                mMediaPlaybackService.setListSong(mList);
            }else {

                mList=Song.getSongFavorite(getContext());
                mAdapter = new SongAdapter(getContext(), mList);
                mMediaPlaybackService.setListSong(mList);
                Log.d("Hoangokokokok","favorite "+mList.size());

            }
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_songs, container, false);
        setData();
        //absSetFavorite();
        setListView();

        initView();
        if(mMediaPlaybackService != null)
        {
            if(mMediaPlaybackService.getCurrentSong()<0)
            {
                //int current = sharedPreferences.getInt("DATA_CURRENT", -1);
                long id =sharedPreferences.getLong("DATA_CURRENT_ID",-1);
                Log.d("songid", "onCreateView: "+mMediaPlaybackService.getCurrentSongId());
                int current = -1;
                Log.d("ClickPlay", "" + id);
                for (int i = 0; i < mListFull.size(); i++) {
                    if (mListFull.get(i).getSongID() == id) {
                        mMediaPlaybackService.setCurrentSong(i);
                        mMediaPlaybackService.setCurrentSongId((int) id);
                        current = i;
                    }
                }
                if (current > -1) {
                    if(isVertical){
                        mllBottom.setVisibility(View.VISIBLE);
                        mMediaPlaybackService.setCurrentSong(current);
                    }
                    mNameSongPlay.setText(mList.get(current).getSongName());                         //Click item RecycleView
                    mSongArtistPlay.setText(mList.get(current).getSongArtist());
                    byte[] songArt = getAlbumArt(mList.get(current).getSongImage());
                    Glide.with(getContext()).asBitmap()
                            .error(R.drawable.cute)
                            .load(songArt)
                            .into(mImagePlay);
                    mBtnPlay.setImageResource(R.drawable.ic_play_black);
                    setItemWhenPause(current);
                }
            }

        }

//        onConnectService();
        Log.d("HoangLD", "onCreateView: ");
        return view;
    }
    public  void setItemWhenPause(int pos){
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setPlay(false);
        }
        mList.get(pos).setPlay(true);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HoangLDssss", "onCreate: ");
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences("DATA_CURRENT_PLAY", getActivity().MODE_PRIVATE);//tao file luu tru
        editor = sharedPreferences.edit();
    }

    public void saveData() {
        editor.remove("DATA_CURRENT");
        editor.putInt("DATA_CURRENT", mMediaPlaybackService.getCurrentSong());
        editor.remove("DATA_CURRENT_ID");
        editor.putLong("DATA_CURRENT_ID", mMediaPlaybackService.getCurrentSongId());
        editor.remove("DATA_CURRENT_STREAM_POSITION");
        editor.putInt("DATA_CURRENT_STREAM_POSITION", mMediaPlaybackService.getCurrentStreamPosition());
        editor.commit();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        if (mMediaPlaybackService != null) {
            Log.d("HoangLD", "onResume: ");
            //mMediaPlaybackService.setListener(AllSongsFragment.class);//get vao
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
        //updateAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecycle.setLayoutManager(manager);
        mRecycle.setAdapter(mAdapter);
        mAdapter.setMediaPlaybackService(mMediaPlaybackService);//set data on adapter
        onConnectService();

        if (mMediaPlaybackService != null && mMediaPlaybackService.getMediaPlayer().isPlaying()) {

            UpdateUI();
        }
        mAdapter.setClick(
                new SongAdapter.IIClick() {
                    @Override
                    public void ItemClick(Song song, int pos) {
                        //pos=song.getPos();
                        //service
                            mMediaPlaybackService.setCurrentSong(pos);//get position
                            mMediaPlaybackService.setCurrentSongId((int) song.getSongID());
                            mMediaPlaybackService.playSong(song.getSongImage());
                            mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);
                            for (int i = 0; i < mList.size(); i++) {
                                mList.get(i).setPlay(false);
                                if(mList.get(i).getSongID()==song.getSongID())
                                {
                                    mList.get(i).setPlay(true);
                                }
                            }
                            //   mMediaPlaybackService.getMediaManager().setCurrentSong(pos);
                        if (isVertical) {
                            if(mMediaPlaybackService.getCurrentSong()>=0)
                            {
                                mllBottom.setVisibility(View.VISIBLE);

                            }
                            mNameSongPlay.setText(song.getSongName());                                  //Click item RecycleView
                            mSongArtistPlay.setText(song.getSongArtist());
                            byte[] songArt = getAlbumArt(song.getSongImage());
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
                            if(songArt != null)
                            {
                                Glide.with(view.getContext()).asBitmap()
                                        .load(songArt)
                                        .error(R.drawable.cute)
                                        .into(mImageBackground);
                                Glide.with(view.getContext()).asBitmap()
                                        .load(songArt)
                                        .error(R.drawable.cute)
                                        .into(mImageMedia);
                            }else {
                                Glide.with(view.getContext()).asBitmap()
                                        .load(R.drawable.cute)
                                        .into(mImageBackground);
                                Glide.with(view.getContext()).asBitmap()
                                        .load(R.drawable.cute)
                                        .into(mImageMedia);
                            }

                            iUpdateMediaWhenAllSongClickItem.UpdateMediaWhenAllSongClickItem(pos);


                        }
                        Log.d("HoangLD", "ItemClick: "+mList.get(pos).getSongImage());

                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public  void onSongBtnClickListener(ImageView btn, View v, Song song, int pos) {
                        updatePopup(v,song,pos);

                    }

                }
        );
    }
    ///////interface ngang click
    //interface ngang
    public interface IUpdateMediaWhenAllSongClickItem {
        void UpdateMediaWhenAllSongClickItem(int pos);
    }


    private IUpdateMediaWhenAllSongClickItem iUpdateMediaWhenAllSongClickItem;
    /* method này để đảm bảo activity sẽ giao tiếp với allSongFragment thông qua interface IUpdateMediaWhenAllSongClickItem*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IUpdateMediaWhenAllSongClickItem) {
            iUpdateMediaWhenAllSongClickItem = (IUpdateMediaWhenAllSongClickItem) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IUpdateMediaWhenAllSongClickItem");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iUpdateMediaWhenAllSongClickItem = null;
    }
    ///////////////////////
    protected abstract void absSetFavorite();
//    public abstract  void updateAdapter();
    public abstract void  updatePopup(View v, Song song, int pos);

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
            long id = mMediaPlaybackService.getCurrentSongId();
            int temp = -1;
            for (int i = 0; i < mListFull.size(); i++) {
                if (mListFull.get(i).getSongID() == id) {
                    temp = i;
                }

            }
            mNameSongPlay.setText(mListFull.get(temp).getSongName());
             mSongArtistPlay.setText(mListFull.get(temp).getSongArtist());
            final byte[] songArt = getAlbumArt(mListFull.get(temp).getSongImage());
            Glide.with(view.getContext()).asBitmap()
                    .load(songArt)
                    .error(R.drawable.cute)
                    .into(mImagePlay);

            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setPlay(false);
                if(mList.get(i).getSongID()==mListFull.get(temp).getSongID())
                {
                    mList.get(i).setPlay(true);
                }
            }
            if (!mMediaPlaybackService.isPlay()) {
                mBtnPlay.setImageResource(R.drawable.ic_play_black);
            } else {
                mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);
            }
//            mList.get(mMediaPlaybackService.getCurrentSong()).setPlay(true);
        }
        mAdapter.notifyDataSetChanged();

    }

    public static byte[] getAlbumArt(String uri) {
        byte[] albumArt = new byte[0];
        try {

                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(uri);
                albumArt = mediaMetadataRetriever.getEmbeddedPicture();
                mediaMetadataRetriever.release();
            Log.d("HoangLD", "getAlbumArt: "+albumArt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albumArt;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_bottom: {
                long id = mMediaPlaybackService.getCurrentSongId();
                int temp = -1;
                for (int i = 0; i < mListFull.size(); i++) {
                    if (mListFull.get(i).getSongID() == id) {
                        temp = i;
                    }
                }

                Song song = mListFull.get(temp);
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
                    if(!mMediaPlaybackService.isResumeRe())
                    {
                        mMediaPlaybackService.resumeSong();
                    }
                    else {
                        int position = sharedPreferences.getInt("DATA_CURRENT_STREAM_POSITION", 0);
                        mMediaPlaybackService.playSong(mList.get(mMediaPlaybackService.getCurrentSong()).getSongImage());
                        mMediaPlaybackService.seekTo(position);

                    }
                    mBtnPlay.setImageResource(R.drawable.ic_pause_black_large);

                }
                mAdapter.notifyDataSetChanged();
                int pos = mMediaPlaybackService.getCurrentSong();
                Song song = getListSong().get(pos);
                mMediaPlaybackService.startForegroundService(song,pos);
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

            long id = mMediaPlaybackService.getCurrentSongId();
            int temp = -1;
            for (int i = 0; i < mListFull.size(); i++) {
                if (mList.get(i).getSongID() == id) {
                    temp = i;
                }
            }
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setPlay(false);
                if(mList.get(i).getSongID()==mList.get(temp).getSongID())
                {
                    mList.get(i).setPlay(true);
                }
            }


    }

    @Override
    public void updateData() {//interface notification
        Log.d("HoangLD", "notifi next ");
        UpdateUI();
    }
    ////////////menu search
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("HoangLDssss", "onCreateOptionsMenu: ");
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

    @Override
    public void updateBase() {
        mAdapter.notifyDataSetChanged();
    }
}
