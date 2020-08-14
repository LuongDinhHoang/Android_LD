package com.example.music.model;

public class Song {
    private long mSongID;
    private String mSongName;
    private  String mSongTime;
    private String mSongAuthor;
    private String mSongImage;

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

    public String getmSongAuthor() {
        return mSongAuthor;
    }

    public void setmSongAuthor(String mSongAuthor) {
        this.mSongAuthor = mSongAuthor;
    }

    public String getmSongImage() {
        return mSongImage;
    }

    public void setmSongImage(String mSongImage) {
        this.mSongImage = mSongImage;
    }



    public Song(long mSongID, String mSongName, String mSongTime, String mSongAuthor, String mSongImage) {
        this.mSongID = mSongID;
        this.mSongName = mSongName;
        this.mSongTime = mSongTime;
        this.mSongAuthor = mSongAuthor;
        this.mSongImage = mSongImage;
    }

}
