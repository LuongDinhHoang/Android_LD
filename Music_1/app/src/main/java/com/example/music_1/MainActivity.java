package com.example.music_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;
import androidx.appcompat.widget.Toolbar;

import com.example.music_1.Fragment.AllSongsFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    private FrameLayout frameLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.Rcv_View);
        frameLayout = (FrameLayout)findViewById(R.id.ll_out);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addFragmentList();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void addFragmentList()
    {

        AllSongsFragment allSongsFragment =new AllSongsFragment();
        fragmentTransaction.replace(R.id.ll_out,allSongsFragment);
        fragmentTransaction.commit();

    }
}