package com.example.music_1.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.music_1.MusicDB;
import com.example.music_1.MusicProvider;

import java.util.ArrayList;
import java.util.List;

public class Song {
    private long mSongID;

    public long getSongIDProvider() {
        return mSongIDProvider;
    }

    public void setSongIDProvider(long mSongIDProvider) {
        this.mSongIDProvider = mSongIDProvider;
    }

    private long mSongIDProvider;
    private String mSongName;
    private long mSongTime;
    private String mSongArtist;
    private String mSongImage;
    private boolean isPlay;

    public int getPos() {
        return mPos;
    }

    public void setPos(int mPos) {
        this.mPos = mPos;
    }

    private  int mPos;

    public long getSongID() {
        return mSongID;
    }

    public void setSongID(long mSongID) {
        this.mSongID = mSongID;
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String mSongName) {
        this.mSongName = mSongName;
    }

    public long getSongTime() {
        return mSongTime;
    }

    public void setSongTime(long mSongTime) {
        this.mSongTime = mSongTime;
    }

    public String getSongArtist() {
        return mSongArtist;
    }

    public void setSongArtist(String mSongArtist) {
        this.mSongArtist = mSongArtist;
    }

    public String getSongImage() {
        return mSongImage;
    }

    public void setSongImage(String mSongImage) {
        this.mSongImage = mSongImage;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public Song(long mSongID,long mSongIDProvider, String mSongName, long mSongTime, String mSongArtist, String mSongImage, boolean isPlay,int mPos) {
        this.mSongID = mSongID;
        this.mSongIDProvider=mSongIDProvider;
        this.mSongName = mSongName;
        this.mSongTime = mSongTime;
        this.mSongArtist = mSongArtist;
        this.mSongImage = mSongImage;
        this.isPlay = isPlay;
        this.mPos=mPos;
    }

    public  String getTimeDurationString(long s )
    {

//        String mm = String.valueOf(mSongTime/60000);
        String seconds = String.valueOf((s/1000) % 60);
        String minutes = String.valueOf((s/1000) / 60);
        if (minutes.length() <= 1) minutes = "0" + minutes;
        if (seconds.length() <= 1) seconds = "0" + seconds;
        return minutes +":"+seconds;
    }

    public static void getSongAll(List<Song> mList,Context context) {

        ContentResolver musicResolver = context.getContentResolver();
        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = musicResolver.query(songUri, null, null, null, null);


        if (songCursor != null && songCursor.moveToFirst()) {
            int pos = 0;
            do {
                int songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int songName = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                long songTime = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                int songAuthor = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int songArt = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                long currentId = songCursor.getLong(songID);
                String currentName = songCursor.getString(songName);
                String currentAuthor = songCursor.getString(songAuthor);
                String currentArt = songCursor.getString(songArt);
                mList.add(new Song(currentId,-1, currentName, songTime, currentAuthor, currentArt, false, pos));
                pos++;
            } while (songCursor.moveToNext());
        }
    }


    public static List<Song> getSongFavorite (Context context){
        // get data in SQL lite
        List<Song> mListSong = new ArrayList<>();
        int posFavor = 0;
        Uri uri = Uri.parse(String.valueOf(MusicProvider.CONTENT_URI));
        ;
        String[] projection = {
                MusicDB.ID,
                MusicDB.ID_PROVIDER,
                MusicDB.TITLE,
                MusicDB.ARTIST,
                MusicDB.DURATION,
                MusicDB.DATA,
                MusicDB.IS_FAVORITE,
                MusicDB.COUNT_OF_PLAY
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                int id_provider = cursor.getInt(1);
                String title = cursor.getString(2);
                String artistName = cursor.getString(3);
                long duration = cursor.getInt(4);
                String data = cursor.getString(5);
                int is_fravorite = cursor.getInt(6);
                String count_of_play = cursor.getString(7);

                if (is_fravorite == 2) {
                    Song song = new Song(id, id_provider, title, duration, artistName, data, false, posFavor);
                    mListSong.add(song);
                }
                posFavor++;
            }
            cursor.close();
        }

        return mListSong;
    }
}

