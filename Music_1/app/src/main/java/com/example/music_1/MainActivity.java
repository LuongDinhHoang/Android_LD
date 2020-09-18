package com.example.music_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.music_1.Adapter.SongAdapter;
import com.example.music_1.Fragment.AllSongsFragment;
import com.example.music_1.Fragment.MediaPlaybackFragment;
import com.example.music_1.Model.Song;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Services.MediaPlaybackService;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public boolean mCheck;

    public boolean getCheck() {
        return mCheck;
    }

    public void setCheck(boolean mCheck) {
        this.mCheck = mCheck;
    }

    private  int mOrientation;


    public SongAdapter getAdapter() {
        return mAdapter;
    }

    private SongAdapter mAdapter;

    private MediaPlaybackService mMediaPlaybackService;

    public MediaPlaybackService getMediaPlaybackService() {
        return mMediaPlaybackService;
    }

    public List<Song> getList() {
        return mList;
    }

    private List<Song> mList;

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    AllSongsFragment allSongsFragment =new AllSongsFragment();
    MediaPlaybackFragment mediaPlaybackFragment =new MediaPlaybackFragment();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        mList = new ArrayList<>();
        getSong(mList);
        mAdapter = new SongAdapter(this, mList);
        ////////////

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_close,
//                R.string.navigation_drawer_close);
//        if (drawer != null) {
//            drawer.addDrawerListener(toggle);
//        }
//        toggle.syncState();

    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
    @Override
    public void onBackPressed() {
        getSupportActionBar().show();                 //setActionBar
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        setService();
        super.onStart();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addFragmentList()
    {
        mMediaPlaybackService.setListener(allSongsFragment);//get vao
        mMediaPlaybackService.setNotificationData(allSongsFragment);
        //mMediaPlaybackService.setListener(mediaPlaybackFragment);
        mOrientation = getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
//            allSongsFragment.setMediaPlaybackService(mMediaPlaybackService);
//            allSongsFragment.setList(mList);
            allSongsFragment.setCheck(true);
            fragmentTransaction.replace(R.id.ll_out,allSongsFragment);
            fragmentTransaction.commit();
        }
        else {
            if(mMediaPlaybackService.getCurrentSong()==-1)
            {
                mMediaPlaybackService.setCurrentSong(0);
            }
            allSongsFragment.setCheck(false);

            mMediaPlaybackService.setNotificationDataMedia(mediaPlaybackFragment);
            mediaPlaybackFragment.setVertical(false);
            fragmentTransaction.replace(R.id.ll_out1,allSongsFragment);
            // mllBottom.setVisibility(view.VISIBLE);
            fragmentTransaction.replace(R.id.ll_out_land,mediaPlaybackFragment);
            mediaPlaybackFragment.setListenerMedia(allSongsFragment);
            mMediaPlaybackService.mListenerMe(mediaPlaybackFragment);
         //   mMediaPlaybackService.setListener(mediaPlaybackFragment);
//            mMediaPlaybackService.setListener(allSongsFragment);//get vao
            fragmentTransaction.commit();
        }

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // setpossion =
        /*AllSongsFragment.savePos*/
       // allSongsFragment.set
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout Layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (item.getItemId()){
            case R.id.nav_listView:
                Layout.closeDrawer(GravityCompat.START);
                displayToast("list");
                return true;
            case R.id.nav_ListFavorites:
                Layout.closeDrawer(GravityCompat.START);
                displayToast("list_favorites");
                return true;

            default:
                return false;
        }
    }
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setService() {
        Intent intent = new Intent(this, MediaPlaybackService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlaybackService.MusicBinder binder = (MediaPlaybackService.MusicBinder) service;
            mMediaPlaybackService = binder.getMusicService();
            mMediaPlaybackService.setListSong(mList);                     //đưa list vào list service nếu service chạy
            //mMediaPlaybackService.getMedia().setListMedia(mList);
            addFragmentList();

//            if(mIsBound){

//            if(!isVertical)
//            {
//                mediaPlaybackFragment.setListenerMedia(AllSongsFragment.this);
//            }`

            Log.d("HoangLD", "onServiceConnectedall: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMediaPlaybackService = null;
            Log.d("HoangLD", ": ");
        }
    };
    public void getSong(List<Song> mList) {

        ContentResolver musicResolver = getContentResolver();
        Uri songUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = musicResolver.query(songUri, null, null, null, null);


            if (songCursor != null && songCursor.moveToFirst()) {
                int pos=0;
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
                    mList.add(new Song(currentId, currentName, songTime, currentAuthor, currentArt, false,pos));
                    pos++;
                } while (songCursor.moveToNext());
//                if (songCursor != null && songCursor.moveToFirst()) {
//                    for (int i = 0; i < mList.size(); i++) {
//                        for (int j = i + 1; j < mList.size(); j++) {
//                            if (mList.get(i).getSongName().compareTo(mList.get(j).getSongName()) > 0) {
//                                Collections.swap(mList, i, j);
//                            }
//
//                        }
//                    }

           // }

        }
    }

}