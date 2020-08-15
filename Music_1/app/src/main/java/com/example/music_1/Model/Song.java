package com.example.music_1.Model;

import android.graphics.Bitmap;

public class Song {
    private long mSongID;
    private String mSongName;
    private long mSongTime;
    private String mSongArtist;
    private String mSongImage;

    public Song(long mSongID, String mSongName, long mSongTime, String mSongArtist, String mSongImage) {
        this.mSongID = mSongID;
        this.mSongName = mSongName;
        this.mSongTime = mSongTime;
        this.mSongArtist = mSongArtist;
        this.mSongImage = mSongImage;
    }

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
    public  String getTimeDurationString(long s )
    {
        int mm=0;
        String Time ;
        s=s/1000;
        while (s>=60)
        {
            ++mm;
            s-=60;
        }
        Time = mm+":"+s;
        return Time;
//        String mm = String.valueOf(mSongTime/60000);
    }
}

