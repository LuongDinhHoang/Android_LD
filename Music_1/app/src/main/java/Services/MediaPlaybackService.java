package Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.music_1.Fragment.AllSongsFragment;
import com.example.music_1.Fragment.MediaPlaybackFragment;
import com.example.music_1.Model.Song;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class MediaPlaybackService extends Service  implements MediaPlayer.OnCompletionListener {
    private SongManager mSongManager;

    public MediaPlaybackFragment getMedia() {
        return mMedia;
    }

    private MediaPlaybackFragment mMedia;
    private MusicBinder binder = new MusicBinder();

    public List<Song> getList() {
        return mList;
    }

    private List<Song> mList = new ArrayList<>();

//    public void setList(List<Song> list) {
//        this.mList = list;
//        mSongManager.setListSong(mList);
//
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class   MusicBinder extends Binder {
        public MediaPlaybackService getMusicService() {
            return MediaPlaybackService.this;//tra ve cac phuong thuc o service
        }
    }

    @Override

    public void onCreate() {
        super.onCreate();
        Log.d("HoangLD", "onCreate: service");
        mSongManager = new SongManager(this);
        mMedia=new MediaPlaybackFragment();
    }
/*
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMediaManager.playSong();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Music");
        builder.setContentText("I'm playing music");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        NotificationChannel channel =
                new NotificationChannel("music", "music",
                        NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        builder.setChannelId(channel.getId());

        Notification notification = builder.build();
        startForeground(1, notification);
        return START_STICKY;
    }
 */

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public SongManager getMediaManager() {
        return mSongManager;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        int pos = mSongManager.getCurrentSong();
        pos++;
        mediaPlayer.reset();
        mSongManager.playSong(mSongManager.getListSong().get(pos).getSongImage());
    }
}
