package Services;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.music_1.Fragment.AllSongsFragment;
import com.example.music_1.Model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongManager{


    private List<Song> mListSong = new ArrayList<>();
    private MediaPlayer mPlayer;
    private int currentSong;
    private int currentStatus;
    private final int STATUS_IDEAL = 1;
    private final int STATUS_PLAYING = 2;
    private final int STATUS_PAUSED = 3;
    private final int STATUS_STOP = 4;
    private boolean StatusPlaying;


    public void setListSong(List<Song> mListSong) {
        this.mListSong = mListSong;
    }

    public boolean getShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    public boolean isShuffle = false;

    public List<Song> getmListSong() {
        return mListSong;
    }

    public boolean isStatusPlaying() {
        return StatusPlaying;
    }

    public List<Song> getListSong() {
        return mListSong;
    }

    public void setCurrentSong(int currentSong) {
        this.currentSong = currentSong;
    }
    public int getCurrentSong() {
        return currentSong;
    }

    //private AllSongsFragment allSongsFragment = new AllSongsFragment();

    private Context mContext;

    public MediaPlayer getMediaPlayer(){
        return mPlayer;
    }

    public SongManager(Context mContext) {
        this.mContext = mContext;
        currentStatus = STATUS_IDEAL;
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        StatusPlaying = true;
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
                currentStatus = STATUS_PLAYING;
            }
        });
    }

    public void playSong(String path) {
        mPlayer.reset();

        try {
            mPlayer.setDataSource(path);
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseSong() {
        StatusPlaying=false;
        mPlayer.pause();
    }

    public void stop() {
        mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
    }
    public  void resumeSong()
    {
        StatusPlaying=true;
        mPlayer.start();
    }
    public List<Song> getDataMusic(){
        return mListSong;
    }

    public MediaPlayer getPlayer() {
        return mPlayer;
    }

    public void seekTo(int position) {
        mPlayer.seekTo(position);
    }



    public void nextSong() {
        Log.d("HoangLD", "nextSong: "+currentSong);
        if (currentSong >= mListSong.size() - 1) {
            currentSong = 0;
        } else {
            currentSong++;
        }
        Log.d("HoangLD", "nextSong: "+currentSong);
        playSong(mListSong.get(currentSong).getSongImage());
    }

    public void previousSong() {
        Log.d("HoangLD", "previousSong1: "+currentSong);
        if (currentSong <= 0) {
            currentSong = mListSong.size() - 1;
        } else {
            currentSong--;
        }
        Log.d("HoangLD1", "previousSong2: "+currentSong);
        playSong(mListSong.get(currentSong).getSongImage());
    }




}
