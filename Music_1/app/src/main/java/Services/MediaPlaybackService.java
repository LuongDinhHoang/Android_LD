package Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.music_1.Fragment.MediaPlaybackFragment;
import com.example.music_1.MainActivity;
import com.example.music_1.Model.Song;
import com.example.music_1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MediaPlaybackService extends Service {
    public static final String ID_CHANNEL = "999";
    private static final CharSequence NANME_CHANNEL = "App_Music";
    private static final String MUSIC_SERVICE_ACTION_PAUSE = "music_service_action_pause";
    private static final String MUSIC_SERVICE_ACTION_PLAY = "music_service_action_play";
    private static final String MUSIC_SERVICE_ACTION_NEXT = "music_service_action_next";
    private static final String MUSIC_SERVICE_ACTION_PREV = "music_service_action_prev";
    private static final String MUSIC_SERVICE_ACTION_STOP = "music_service_action_stop";
    public static final int REPEAT = 10;
    public static final int REPEAT_ALL = 11;
    public static final int NORMAL = 12;
    public static final int SHUFFLE = 13;    public static final int NOTIFICATION_ID = 125;
    private List<Song> mListSong = new ArrayList<>();
    private MediaPlayer mPlayer;
    private int currentSong = -1;
    private int currentStatus;
    private final int STATUS_IDEAL = 1;
    private final int STATUS_PLAYING = 2;
    private final int STATUS_PAUSED = 3;
    private final int STATUS_STOP = 4;
    private boolean mRepeat = false;
    private boolean StatusPlaying;
    public boolean isPlay() {
        return isPlay;
    }
    public void setPlay(boolean play) {
        isPlay = play;
    }
    private boolean isPlay = false;

    public boolean isFirst() {
        return isFirst;
    }

    private boolean isFirst=true;
    SharedPreferences sharedPreferencesCurrent;
    SharedPreferences.Editor editor;

    public boolean isResumeRe() {
        return ResumeRe;
    }

    public void setResumeRe(boolean resumeRe) {
        ResumeRe = resumeRe;
    }

    private boolean ResumeRe;
    public void setListSong(List<Song> mListSong) {
        this.mListSong = mListSong;
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
    public int getCurrentStreamPosition() {
        sharedPreferencesCurrent = getSharedPreferences("DATA_CURRENT_PLAY", MODE_PRIVATE);
        int position = sharedPreferencesCurrent.getInt("DATA_CURRENT_STREAM_POSITION", 0);
        if (isFirst) return position;
        if (mPlayer != null)
            return mPlayer.getCurrentPosition();  //trả về vtri đang phát
        return 0;
    }

    public MediaPlayer getMediaPlayer() {
        return mPlayer;
    }
    private MediaPlaybackFragment mMedia;
    private MusicBinder binder = new MusicBinder();
    private List<Song> mList = new ArrayList<>();
    private int isRepeat;
    private int isShuffle;

    public int isShuffle() {
        return isShuffle;
    }

    public void setShuffle(int shuffle) {
        isShuffle = shuffle;
    }


    public int isRepeat() {
        return isRepeat;
    }

    public void setRepeat(int repeat) {
        isRepeat = repeat;
    }
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
        initMediaPlayer();
        Log.d("HoangLD", "onCreate: service");
        mMedia = new MediaPlaybackFragment();
        createChannel();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() == MUSIC_SERVICE_ACTION_NEXT) {
            nextSong();
            updateUIClickNotification();

        } else if (intent.getAction() == MUSIC_SERVICE_ACTION_PREV) {
            previousSong();
            updateUIClickNotification();

        } else if (intent.getAction() == MUSIC_SERVICE_ACTION_STOP) {
            pauseSong();
            int pos = getCurrentSong();
            Song song = getListSong().get(pos);
            updateUIClickNotification();
            startForegroundService(song, pos);

        } else if (intent.getAction() == MUSIC_SERVICE_ACTION_PLAY) {
            resumeSong();
            int pos = getCurrentSong();
            Song song = getListSong().get(pos);
            updateUIClickNotification();
            startForegroundService(song, pos);
        }
//        if(currentSong>=0)
//        {
//            int pos = getCurrentSong();
//            interfaceUpdate();
//            interfaceUpdateMedia();
//            Song song = getListSong().get(pos);
//            createChannel();
//            createNotification(getApplicationContext(), song, pos);
//        }


        return START_STICKY;
    }


    private void updateUIClickNotification() {
        interfaceUpdate();
        interfaceUpdateMedia();
    }

    public void startForegroundService(Song song, int pos) {
//            showNotification(song, isPlaying);
        startForeground(NOTIFICATION_ID, createNotification(getApplicationContext(), song));
        if (!isPlay)
            stopForeground(false);
    }

    public void interfaceUpdate() {
        if (mNotificationUpdateAllSong != null) {
            mNotificationUpdateAllSong.updateData();
        }
    }

    public void interfaceUpdateMedia() {
        if (mNotificationUpdateMedia != null) {
            mNotificationUpdateMedia.updateDataMedia();
        }
    }

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

    public Notification createNotification(Context context, Song song) {
        //click notification
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent intentMove = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent nextIntent = new Intent(this, MediaPlaybackService.class).setAction(MUSIC_SERVICE_ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getService(this,
                0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent prevIntent = new Intent(this, MediaPlaybackService.class).setAction(MUSIC_SERVICE_ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getService(this,
                0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent stopIntent = new Intent(this, MediaPlaybackService.class).setAction(MUSIC_SERVICE_ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getService(this,
                0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent playIntent = new Intent(this, MediaPlaybackService.class).setAction(MUSIC_SERVICE_ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getService(this,
                0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews notification_small = new RemoteViews(context.getPackageName(), R.layout.notification_small);
        RemoteViews notification_big = new RemoteViews(context.getPackageName(), R.layout.notification);
        Bitmap bitmap = getAlbumArt(song.getSongImage());
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cute);
        }

        //set data notification
        notification_big.setTextViewText(R.id.notification_title, song.getSongName());
        notification_big.setTextViewText(R.id.notification_Author, song.getSongArtist());
        notification_big.setImageViewBitmap(R.id.notification_image, bitmap);
        notification_small.setImageViewBitmap(R.id.notificationSmall_Image, bitmap);
        //Click button notification
        notification_big.setOnClickPendingIntent(R.id.notification_next, nextPendingIntent);
        notification_small.setOnClickPendingIntent(R.id.notificationSmall_next, nextPendingIntent);
        notification_big.setOnClickPendingIntent(R.id.notification_prev, prevPendingIntent);
        notification_small.setOnClickPendingIntent(R.id.notificationSmall_prev, prevPendingIntent);
        notification_big.setImageViewResource(R.id.notification_playing, R.drawable.ic_pause_media);
        notification_small.setImageViewResource(R.id.notification_playing, R.drawable.ic_pause_media);
        if (isPlay) {
            notification_big.setImageViewResource(R.id.notification_playing, R.drawable.ic_pause_media);
            notification_small.setImageViewResource(R.id.notificationSmall_playing, R.drawable.ic_pause_media);
            notification_small.setOnClickPendingIntent(R.id.notificationSmall_playing, stopPendingIntent);
            notification_big.setOnClickPendingIntent(R.id.notification_playing, stopPendingIntent);
        } else {
            notification_big.setImageViewResource(R.id.notification_playing, R.drawable.ic_play_media);
            notification_small.setImageViewResource(R.id.notificationSmall_playing, R.drawable.ic_play_media);
            notification_small.setOnClickPendingIntent(R.id.notificationSmall_playing, playPendingIntent);
            notification_big.setOnClickPendingIntent(R.id.notification_playing, playPendingIntent);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ID_CHANNEL)
                .setSmallIcon(R.drawable.ic_zing)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notification_small)
                .setContentIntent(intentMove)
                .setCustomBigContentView(notification_big);
        return builder.build();
//            notificationManagerCompat.notify(10, builder.build());
//            if (!isPlay)
//            stopForeground(false);
//            else  stopForeground(true);
    }

    public interface notificationUpdateAllSong {
        void updateData();
    }

    private notificationUpdateAllSong mNotificationUpdateAllSong;

    public void setNotificationData(notificationUpdateAllSong mNotificationUpdateAllSong) {
        this.mNotificationUpdateAllSong = mNotificationUpdateAllSong;
    }

    /////////
    public interface notificationUpdateMedia {
        void updateDataMedia();
    }

    private notificationUpdateMedia mNotificationUpdateMedia;

    public void setNotificationDataMedia(notificationUpdateMedia mNotificationUpdateMedia) {
        this.mNotificationUpdateMedia = mNotificationUpdateMedia;
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
                if(!isFirst){

                    Random rd = new Random();
                    int random = rd.nextInt(8);
                    Log.d("TAG", "onCompletion: " + isShuffle);
                    if (isShuffle==SHUFFLE) {
                        if (isRepeat==REPEAT) {
                            currentSong = currentSong;
                        } else
                        {
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
                    if (isRepeat==REPEAT_ALL) {
                        if (currentSong >= mListSong.size() - 1) {
                            currentSong = 0;
                        }
                        Log.d("HoangLD", "RepeatAll" + currentSong);

                    } else if (isRepeat==REPEAT) {
                        Log.d("HoangLD", "Repeat: ");
                        currentSong--;
                    }
                    playSong(getListSong().get(currentSong).getSongImage());
                    if (mListenerAll != null) {
                        mListenerAll.updateUiSongPlay(currentSong);
                        setCurrentSong(currentSong);
                    }
                    if (mListenerMe != null) {
                        mListenerMe.updateUiMediaSong(currentSong);
                        setCurrentSong(currentSong);
                    }
                    int pos = getCurrentSong();
                    Song song = getListSong().get(pos);
                    startForegroundService(song, pos);
                }

            }
        });
    }

    public void playSong(String path) {
        isFirst=false;
        mPlayer.reset();
        try {
            mPlayer.setDataSource(path);
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isPlay = true;
        int pos = getCurrentSong();
        Song song = getListSong().get(pos);
        startForegroundService(song, pos);

    }

    public void pauseSong() {
        mPlayer.pause();
        isPlay = false;
    }

    public void stop() {
        mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
    }

    public void resumeSong() {
        mPlayer.start();
        isPlay = true;
        ResumeRe=true;

    }

    public long getDuration() {
        if (mPlayer != null)
            return mPlayer.getDuration();      //trả về vtri cuối//ggdhdhdhh

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


    public void nextSong() {
        Log.d("HoangLD", "nextSong: ");
        isPlay = false;
        if (currentSong >= mListSong.size() - 1) {
            currentSong = 0;
        } else {
            currentSong++;
        }
        playSong(mListSong.get(currentSong).getSongImage());
    }

    public void previousSong() {

        isPlay = false;
        if (mPlayer.getCurrentPosition() <= 3000) {
            if (currentSong <= 0) {
                currentSong = mListSong.size() - 1;
            } else {
                currentSong--;
            }
        }

        Log.d("HoangLD1", "previousSong2: " + currentSong);
        playSong(mListSong.get(currentSong).getSongImage());
    }

    public interface SongManageListener {
        void updateUiSongPlay(int pos);
    }
    public SongManageListener mListenerAll;

    public void setListener(SongManageListener mListener) {
        this.mListenerAll = mListener;
    }

    ///set interface tu chuyen bài me
    public interface SongManageListenerMedia {
        void updateUiMediaSong(int pos);
    }

    public SongManageListenerMedia mListenerMe;

    public void mListenerMe(SongManageListenerMedia mListenerMe) {
        this.mListenerMe = mListenerMe;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        Log.d("hoang", "onDestroy: ");
        mPlayer.release();
    }

    public static Bitmap getAlbumArt(String path) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        byte[] data = mediaMetadataRetriever.getEmbeddedPicture();
        if (data != null) {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        return null;
    }

}
