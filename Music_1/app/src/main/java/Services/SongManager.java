package Services;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;


import com.example.music_1.Model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SongManager {

    public static final String SONG_PLAY_COMPLETE = "song_play_complete";
    public static final String MESSAGE_SONG_PLAY_COMPLETE = "message_song_play_complete";
    private List<Song> mListSong = new ArrayList<>();
    private MediaPlayer mPlayer;
    private int currentSong;
    private int currentStatus;
    private final int STATUS_IDEAL = 1;
    private final int STATUS_PLAYING = 2;
    private final int STATUS_PAUSED = 3;
    private final int STATUS_STOP = 4;
    private boolean mRepeat = false;
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

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public boolean isRepeatAll() {
        return isRepeatAll;
    }

    public void setRepeatAll(boolean repeatAll) {
        isRepeatAll = repeatAll;
    }

    public boolean isShuffle, isRepeat = false, isRepeatAll = false;

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
    public  int getCurrentStreamPosition(){
        if(mPlayer!=null)
            return mPlayer.getCurrentPosition();  //trả về vtri đang phát

        return 0;
    }

    //private AllSongsFragment allSongsFragment = new AllSongsFragment();

    private Context mContext;

    public MediaPlayer getMediaPlayer() {
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
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                Random rd = new Random();
                int random = rd.nextInt(8);
                Log.d("TAG", "onCompletion: " + isShuffle);
                if (isShuffle) {
                    if ( isRepeat) {
                        currentSong = currentSong;
                    } else {
                        currentSong = currentSong + random;
                        if (currentSong >= mListSong.size() - 1) {
                            currentSong = 0;
                        }
                    }

                } else {
                    if (currentSong >= mListSong.size() - 1) {
                        currentSong = 0;
                    } else {
                        currentSong++;
                    }
                }
                if (isRepeatAll) {
                    if (currentSong >= mListSong.size() - 1) {
                        currentSong = 0;
                    }
                    Log.d("HoangLD", "RepeatAll"+currentSong);

                } else if (isRepeat) {
                    Log.d("HoangLD", "Repeat: ");
                    currentSong --;
                }
                playSong(getListSong().get(currentSong).getSongImage());
                if (mListener != null) {
                    Log.d("HoangLD", "nhay");
                    mListener.updateUiSongPlay(currentSong);
                    setCurrentSong(currentSong);
                }

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
        StatusPlaying = false;
        mPlayer.pause();
    }

    public void stop() {
        mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
    }

    public void resumeSong() {
        StatusPlaying = true;
        mPlayer.start();
    }
    public long getDuration(){
        if(mPlayer!=null)
            return mPlayer.getDuration();      //trả về vtri cuối

        return 0;
    }

    public List<Song> getDataMusic() {
        return mListSong;
    }

    public MediaPlayer getPlayer() {
        return mPlayer;
    }

    public void seekTo(int position) {
        mPlayer.seekTo(position);
    }


    Random rd = new Random();

    public void nextSong() {
//        if (isShuffle) {
//            int number = rd.nextInt(3);
//            currentSong = currentSong + number;
//        } else {
        Log.d("HoangLD", "nextSong: ");
        if (currentSong >= mListSong.size() - 1) {
            currentSong = 0;
        } else {
            currentSong++;
        }
        // }
        playSong(mListSong.get(currentSong).getSongImage());
    }

    public void previousSong() {
        Log.d("HoangLD", "previousSong1: " + currentSong);
//        if (isShuffle) {
//            int number = rd.nextInt(3);
//            currentSong = currentSong - number;
//        } else {
        if (currentSong <= 0) {
            currentSong = mListSong.size() - 1;
        } else {
            currentSong--;
//            }
        }

        Log.d("HoangLD1", "previousSong2: " + currentSong);
        playSong(mListSong.get(currentSong).getSongImage());
    }

    public interface SongManageListener {
        void updateUiSongPlay(int pos);
    }
    private SongManageListener mListener;

    public void setListener(SongManageListener mListener) {
        this.mListener = mListener;
    }
}
