package com.example.music_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.example.music_1.Fragment.BaseSongListFragment;
import com.example.music_1.Fragment.FavoritesFragment;
import com.example.music_1.Fragment.MediaPlaybackFragment;
import com.example.music_1.Model.Song;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import com.example.music_1.Services.MediaPlaybackService;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public boolean isVertical() {
        return mIsVertical;
    }

    public void setVertical(boolean mIsVertical) {
        this.mIsVertical = mIsVertical;
    }

    public boolean mIsVertical;
    private MediaPlaybackFragment mediaPlaybackFragment;

    private int mOrientation;
    private int Repeat = 11, Shuffle = 12;
    public static final int NORMAL = 12;

    public SongAdapter getAdapter() {
        return mAdapter;
    }

    private SongAdapter mAdapter;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 5;
    private MediaPlaybackService mMediaPlaybackService;

    public MediaPlaybackService getMediaPlaybackService() {
        return mMediaPlaybackService;
    }

    public List<Song> getList() {
        return mList;
    }

    private List<Song> mList;
    SharedPreferences sharedPreferences;

    protected BaseSongListFragment mBaseSongsFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("HoangLD", "onCreate: ");
        Toolbar toolbar;
        //////////////navigation
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }


        sharedPreferences = getSharedPreferences("DATA_PLAY_MEDIA", MODE_PRIVATE);

        Repeat = sharedPreferences.getInt("DATA_REPEAT", NORMAL);
        Shuffle = sharedPreferences.getInt("DATA_SHUFFLE", NORMAL);

        if (savedInstanceState != null) {
            Repeat = savedInstanceState.getInt("REPEAT");
            Shuffle = savedInstanceState.getInt("SHUFFLE");
        }
        /////////check
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);

        } else {
            getData();
        }
    }

    public void getData() {
        mList = new ArrayList<>();
        Song.getSongAll(mList,this);
        mAdapter = new SongAdapter(this, mList);

    }
//
//    public void getFavorData() {
//        mList = new ArrayList<>();
//        mList = getSongFavorite(this);
//        mAdapter = new SongAdapter(this, mList);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_MEDIA) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                addFragmentList();                        //  Override  onRequestPermissionsResult() để nhận lại cuộc gọi (check permission)\
                getData();
                Toast.makeText(this, "Permission Granted !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied !", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().show();                 //setActionBar
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        setService();
        Log.d("HoangLD", "onStart: ");
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("HoangLD", "onStop: ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("HoangLD", "onDestroy: ");
        if (mMediaPlaybackService != null) {
            mBaseSongsFragment.saveData();
            unbindService(serviceConnection);
            mMediaPlaybackService.setNotificationData(null);
            mMediaPlaybackService.setNotificationDataMedia(null);

        }

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addFragmentList() {
        //mMediaPlaybackService.setListener(mediaPlaybackFragment);
        mOrientation = getResources().getConfiguration().orientation;
        Log.d("HoangLDssss", "addFragmentList: " + mOrientation);
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {

            mBaseSongsFragment = AllSongsFragment.newInstance(true);
            setVertical(true);
            mMediaPlaybackService.setListener(mBaseSongsFragment);//get vao
            mMediaPlaybackService.setNotificationData(mBaseSongsFragment);
            mBaseSongsFragment.setCheck(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.ll_out, mBaseSongsFragment).commit();
        } else {
            if (mMediaPlaybackService.getCurrentSong() == -1) {
                mMediaPlaybackService.setCurrentSong(0);
            }
            mBaseSongsFragment = AllSongsFragment.newInstance(true);
            mediaPlaybackFragment = new MediaPlaybackFragment();
            mBaseSongsFragment.setCheck(false);
            setVertical(false);
            mediaPlaybackFragment.setVertical(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.ll_out1, mBaseSongsFragment)
                    .replace(R.id.ll_out_land, mediaPlaybackFragment).commit();
            mediaPlaybackFragment.setListenerMedia(mBaseSongsFragment);
            mediaPlaybackFragment.setPlayPauseMedia(mBaseSongsFragment);
            mMediaPlaybackService.mListenerMe(mediaPlaybackFragment);
            mMediaPlaybackService.setListener(mBaseSongsFragment);//get vao
            mMediaPlaybackService.setNotificationData(mBaseSongsFragment);
            mMediaPlaybackService.setNotificationDataMedia(mediaPlaybackFragment);

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
        switch (item.getItemId()) {
            case R.id.nav_listView:
                getSupportActionBar().setTitle("Music");
                mBaseSongsFragment.setList(mList);
                mBaseSongsFragment = AllSongsFragment.newInstance(true);
                mMediaPlaybackService.setListener(mBaseSongsFragment);//get vao
                mMediaPlaybackService.setNotificationData(mBaseSongsFragment);
                mBaseSongsFragment.setCheck(true);
                if(isVertical())
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.ll_out, mBaseSongsFragment).commit();
                }else {
                    mediaPlaybackFragment.setPlayPauseMedia(mBaseSongsFragment);
                    mediaPlaybackFragment.setVertical(false);
                    mBaseSongsFragment.setCheck(false);
                    getSupportFragmentManager().beginTransaction().replace(R.id.ll_out1, mBaseSongsFragment).commit();
                }

                Toast toast = Toast.makeText(this, "AllSong", Toast.LENGTH_SHORT);
                mAdapter.notifyDataSetChanged();
                toast.show();
                Layout.closeDrawer(GravityCompat.START);

                return true;
            case R.id.nav_ListFavorites:
                getSupportActionBar().setTitle("Favorite");
                mBaseSongsFragment = FavoritesFragment.newInstance(true);
                mMediaPlaybackService.setListener(mBaseSongsFragment);//get vao
                mMediaPlaybackService.setNotificationData(mBaseSongsFragment);
                mBaseSongsFragment.setCheck(true);
                if(isVertical())
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.ll_out, mBaseSongsFragment).commit();
                }
                else {
                    mediaPlaybackFragment.setPlayPauseMedia(mBaseSongsFragment);
                    mediaPlaybackFragment.setVertical(false);
                    mBaseSongsFragment.setCheck(false);
                    getSupportFragmentManager().beginTransaction().replace(R.id.ll_out1, mBaseSongsFragment).commit();
                }
                Toast toast1 = Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT);
                mAdapter.notifyDataSetChanged();
                toast1.show();
                Layout.closeDrawer(GravityCompat.START);
                return true;

            default:
                return false;
        }
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

            Log.d("HoangLD", "onServiceConnectedall: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMediaPlaybackService = null;
            Log.d("HoangLD", ": ");
        }
    };


}