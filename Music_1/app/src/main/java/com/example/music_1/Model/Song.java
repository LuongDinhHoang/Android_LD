package com.example.music_1.Model;

import android.graphics.Bitmap;

public class Song {
    private long mSongID;
    private String mSongName;
    private String mSongTime;
    private String mSongArtist;
    private Bitmap mSongImage;

    public Song(long mSongID, String mSongName, String mSongTime, String mSongArtist, Bitmap mSongImage) {
        this.mSongID = mSongID;
        this.mSongName = mSongName;
        this.mSongTime = mSongTime;
        this.mSongArtist = mSongArtist;
        this.mSongImage = mSongImage;
    }

    public long getmSongID() {
        return mSongID;
    }

    public void setmSongID(long mSongID) {
        this.mSongID = mSongID;
    }

    public String getmSongName() {
        return mSongName;
    }

    public void setmSongName(String mSongName) {
        this.mSongName = mSongName;
    }

    public String getmSongTime() {
        return mSongTime;
    }

    public void setmSongTime(String mSongTime) {
        this.mSongTime = mSongTime;
    }

    public String getmSongArtist() {
        return mSongArtist;
    }

    public void setmSongArtist(String mSongArtist) {
        this.mSongArtist = mSongArtist;
    }

    public Bitmap getmSongImage() {
        return mSongImage;
    }

    public void setmSongImage(Bitmap mSongImage) {
        this.mSongImage = mSongImage;
    }
}

