package com.example.music_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.widget.Toolbar;

import com.example.music_1.Fragment.AllSongsFragment;
import com.example.music_1.Fragment.MediaPlaybackFragment;

public class MainActivity extends AppCompatActivity {


    public boolean mCheck;

    public boolean getCheck() {
        return mCheck;
    }

    public void setCheck(boolean mCheck) {
        this.mCheck = mCheck;
    }

    private  int mOrientation;
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    AllSongsFragment allSongsFragment =new AllSongsFragment();
    MediaPlaybackFragment mediaPlaybackFragment =new MediaPlaybackFragment();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addFragmentList();

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
            mediaPlaybackFragment.setVertical(true);
            fragmentTransaction.replace(R.id.ll_out,allSongsFragment);
            fragmentTransaction.commit();
        }
        else {
            allSongsFragment.setCheck(false);
            mediaPlaybackFragment.setVertical(false);
            fragmentTransaction.replace(R.id.ll_out,allSongsFragment);
           // mllBottom.setVisibility(view.VISIBLE);
            fragmentTransaction.replace(R.id.ll_out_land,mediaPlaybackFragment);
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
}