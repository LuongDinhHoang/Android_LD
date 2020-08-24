package Services;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.music_1.Fragment.AllSongsFragment;
import com.example.music_1.Model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongManager {
    private List<Song> mListSong = new ArrayList<>();
    private MediaPlayer mPlayer;
    private int currentSong;
    private int currentStatus;
    private final int STATUS_IDEAL = 1;
    private final int STATUS_PLAYING = 2;
    private final int STATUS_PAUSED = 3;
    private final int STATUS_STOP = 4;
    private AllSongsFragment allSongsFragment = new AllSongsFragment();

    private Context mContext;

    public SongManager(Context mContext) {
        this.mContext = mContext;
        currentStatus = STATUS_IDEAL;
        initMediaPlayer();
    }

    private void initMediaPlayer() {
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
        mPlayer.pause();
        currentStatus = STATUS_PAUSED;
    }

    public void stop() {
        mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
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
        if (currentSong >= mListSong.size() - 1) {
            currentSong = 0;
        } else {
            currentSong++;
        }
        playSong("");
    }

    public void previousSong() {
        if (currentSong <= 0) {
            currentSong = mListSong.size() - 1;
        } else {
            currentSong--;
        }
        playSong("");
    }

}
