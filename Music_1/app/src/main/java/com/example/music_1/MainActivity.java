package com.example.music_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.music_1.Fragment.AllSongsFragment;
import com.example.music_1.Fragment.MediaPlaybackFragment;
import com.google.android.material.navigation.NavigationView;

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
        addFragmentList();
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_close,
//                R.string.navigation_drawer_close);
//        if (drawer != null) {
//            drawer.addDrawerListener(toggle);
//        }
//        toggle.syncState();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        getSupportActionBar().show();                 //setActionBar
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void addFragmentList()
    {
        mOrientation = getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            allSongsFragment.setCheck(true);
            fragmentTransaction.replace(R.id.ll_out,allSongsFragment);
            fragmentTransaction.commit();
        }
        else {
            allSongsFragment.setCheck(false);
            mediaPlaybackFragment.setVertical(false);
            fragmentTransaction.replace(R.id.ll_out1,allSongsFragment);
           // mllBottom.setVisibility(view.VISIBLE);
            fragmentTransaction.replace(R.id.ll_out_land,mediaPlaybackFragment);
            mediaPlaybackFragment.setListenerMedia(allSongsFragment);
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
}