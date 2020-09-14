package Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
    private SongManager mSongManager;
    public static final String ID_CHANNEL = "999";
    private static final CharSequence NANME_CHANNEL = "App_Music";
    private static final String MUSIC_SERVICE_ACTION_PAUSE = "music_service_action_pause";
    private static final String MUSIC_SERVICE_ACTION_PLAY = "music_service_action_play";
    private static final String MUSIC_SERVICE_ACTION_NEXT = "music_service_action_next";
    private static final String MUSIC_SERVICE_ACTION_PREV = "music_service_action_prev";
    private static final String MUSIC_SERVICE_ACTION_STOP = "music_service_action_stop";

    public static final String SONG_PLAY_COMPLETE = "song_play_complete";
    public static final String MESSAGE_SONG_PLAY_COMPLETE = "message_song_play_complete";
    private List<Song> mListSong = new ArrayList<>();
    private MediaPlayer mPlayer;
    private int currentSong =-1;
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

    private  boolean isPlay;


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
        @RequiresApi(api = Build.VERSION_CODES.O)
        public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction()==MUSIC_SERVICE_ACTION_NEXT) {
            Log.d("HoangLD", "onStartCommand: ");

            nextSong();
            int pos = getCurrentSong();
            Song song = getListSong().get(pos);
            createChannel();
            createNotification(getApplicationContext(), song, pos);
        }
            return START_STICKY;
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
                    Log.d("HoangLD", "nhay"+mListener);

                    mListener.updateUiSongPlay(currentSong);
                    setCurrentSong(currentSong);
                }
                isPlay=false;
            }
        });
    }

    public void playSong(String path) {
        mPlayer.reset();
        isPlay=true;
        try {
            mPlayer.setDataSource(path);
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseSong() {
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



    public void nextSong() {
//        if (isShuffle) {
//            int number = rd.nextInt(3);
//            currentSong = currentSong + number;
//        } else {
        Log.d("HoangLD", "nextSong: ");
        isPlay=false;
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
        isPlay=false;
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
    public SongManageListener mListener;

    public void setListener(SongManageListener mListener) {
        this.mListener = mListener;
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
    public void createNotification(Context context, Song song, int pos)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            //click notification
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent intentMove = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

            Intent nextIntent = new Intent(this, MediaPlaybackService.class).setAction(MUSIC_SERVICE_ACTION_NEXT);
            PendingIntent nextPendingIntent = PendingIntent.getService(this,
                    0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);



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
            //Click button notification
            notification_big.setOnClickPendingIntent(R.id.notification_next, nextPendingIntent);
            notification_small.setOnClickPendingIntent(R.id.notificationSmall_next,nextPendingIntent);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ID_CHANNEL)
                    .setSmallIcon(R.drawable.ic_zing)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(notification_small)
                    .setContentIntent(intentMove)
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
