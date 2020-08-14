package com.example.music;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.adapter.SongAdapter;
import com.example.music.fragment.AllSongsFragment;
import com.example.music.fragment.MediaPlaybackFragment;
import com.example.music.model.Song;

import java.util.ArrayList;
import java.util.List;

public class ActivityMusic extends AppCompatActivity {

    private List<Song> mListSong;
    private FrameLayout llcontent;
    private SongAdapter mSongAdapter;
    private RecyclerView mRcvSong;
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mRcvSong = findViewById(R.id.rcv_Song);
        llcontent = findViewById(R.id.ll_content);
        mListSong = new ArrayList<>();
        //ActionBar bar = getActionBar();
        getAllSongsFragment();
        // getMediaPlaybackFragment();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);      // create button search in action bar
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:                                  //set click for button search in action bar
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getMediaPlaybackFragment() {

        MediaPlaybackFragment mediaPlaybackFragment = new MediaPlaybackFragment();
        getSupportActionBar().hide();  // hide action bar
        fragmentTransaction.replace(R.id.ll_content, mediaPlaybackFragment);           // get fragment MediaPlayBackFragment vào activity main
        fragmentTransaction.commit();
    }

    public void getAllSongsFragment() {
        AllSongsFragment allSongsFragment = new AllSongsFragment();
        fragmentTransaction.replace(R.id.ll_content, allSongsFragment);               //get fragment AllSongsFragment vào activity main
        fragmentTransaction.commit();
    }


}