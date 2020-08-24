package com.example.music_1.Model;

import android.graphics.Bitmap;

public class Song {
    private long mSongID;
    private String mSongName;
    private long mSongTime;
    private String mSongArtist;
    private String mSongImage;
    private boolean isPlay;

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

    public Song(long mSongID, String mSongName, long mSongTime, String mSongArtist, String mSongImage, boolean isPlay) {
        this.mSongID = mSongID;
        this.mSongName = mSongName;
        this.mSongTime = mSongTime;
        this.mSongArtist = mSongArtist;
        this.mSongImage = mSongImage;
        this.isPlay = isPlay;
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
}

