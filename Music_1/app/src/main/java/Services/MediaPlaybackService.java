package Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.security.Provider;

public class MediaPlaybackService extends Service {
    private SongManager mSongManager;
    private MusicBinder binder = new MusicBinder();
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
        mSongManager = new SongManager(this);
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
}
