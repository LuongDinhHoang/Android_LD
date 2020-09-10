package Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.music_1.Fragment.AllSongsFragment;
import com.example.music_1.Fragment.MediaPlaybackFragment;
import com.example.music_1.Model.Song;
import com.example.music_1.R;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class MediaPlaybackService extends Service {
    private SongManager mSongManager;
    public static final String ID_CHANNEL = "999";
    private static final CharSequence NANME_CHANNEL = "App_Music";
    private static final String MUSIC_SERVICE_ACTION_PAUSE = "music_service_action_pause";
    private static final String MUSIC_SERVICE_ACTION_PLAY = "music_service_action_play";
    private static final String MUSIC_SERVICE_ACTION_NEXT = "music_service_action_next";
    private static final String MUSIC_SERVICE_ACTION_PREV = "music_service_action_prev";
    private static final String MUSIC_SERVICE_ACTION_STOP = "music_service_action_stop";

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

    public class MusicBinder extends Binder {
        public MediaPlaybackService getMusicService() {
            return MediaPlaybackService.this;//tra ve cac phuong thuc o service
        }
    }

    @Override

    public void onCreate() {
        super.onCreate();
        Log.d("HoangLD", "onCreate: service");
        mSongManager = new SongManager(this);
        mMedia = new MediaPlaybackFragment();
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
    public void createChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ID_CHANNEL, NANME_CHANNEL, NotificationManager.IMPORTANCE_LOW);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }
    public void createNotification(Context context, Song song, int pos)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);


            RemoteViews notification_small = new RemoteViews(context.getPackageName(), R.layout.notification_small);
            RemoteViews notification_big = new RemoteViews(context.getPackageName(), R.layout.notification);
            Bitmap bitmap =  getAlbumArt(song.getSongImage());
            if (bitmap == null)
            {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cute);
            }

            //set data notification
            notification_big.setTextViewText(R.id.notification_title,song.getSongName());
            notification_big.setTextViewText(R.id.notification_Author,song.getSongArtist());
            notification_big.setImageViewBitmap(R.id.notification_image,bitmap);
            notification_small.setImageViewBitmap(R.id.notificationSmall_Image,bitmap);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ID_CHANNEL)
                    .setSmallIcon(R.drawable.ic_zing)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(notification_small)
                    //.setContentIntent(contentIntent)
                    .setCustomBigContentView(notification_big);
            notificationManagerCompat.notify(10, builder.build());
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public SongManager getMediaManager() {
        return mSongManager;
    }
    public static Bitmap getAlbumArt(String path){
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        byte [] data=mediaMetadataRetriever.getEmbeddedPicture();
        if(data!=null){
            return BitmapFactory.decodeByteArray(data, 0 , data.length);
        }
        return null;
    }

}
