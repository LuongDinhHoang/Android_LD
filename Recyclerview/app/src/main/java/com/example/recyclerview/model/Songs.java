package com.example.recyclerview.model;

public class Songs {
    private long mSongID;
    private String mSongTitle;

    public Songs(long id, String title){
        mSongID = id;
        mSongTitle = title;
    }

    public long getSongID(){
        return mSongID;
    }

    public String getSongTitle(){
        return mSongTitle;
    }
}
