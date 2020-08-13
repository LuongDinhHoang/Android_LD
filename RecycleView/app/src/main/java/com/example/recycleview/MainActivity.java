package com.example.recycleview;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleview.adapter.Adapter;

import java.util.ArrayList;

import model.student;

public class MainActivity extends AppCompatActivity {

    ImageView button;
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (ImageView) findViewById(R.id.buttonn);

        load();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        return super.onCreateOptionsMenu(menu);

    }

    public void load() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.Rcv_View);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        final ArrayList<student> arrayList = new ArrayList<>();
        arrayList.add(new student("Hoa no khong mau", "chao hoang", R.raw.abc));
        arrayList.add(new student("Hoang", "chao hoang", R.drawable.ic_launcher_foreground));
        arrayList.add(new student("Hoang", "chao hoang", R.drawable.ic_launcher_foreground));
        arrayList.add(new student("Hoang", "chao hoang", R.drawable.ic_launcher_foreground));
        arrayList.add(new student("Hoang", "chao hoang", R.drawable.ic_launcher_foreground));
        arrayList.add(new student("Hoang", "chao hoang", R.drawable.ic_launcher_foreground));
        arrayList.add(new student("Hoang", "chao hoang", R.drawable.ic_launcher_foreground));
        arrayList.add(new student("Hoang", "chao hoang", R.drawable.ic_launcher_foreground));
        arrayList.add(new student("Hoang", "chao hoang", R.drawable.ic_launcher_foreground));
        arrayList.add(new student("Hoang", "chao hoang", R.drawable.ic_launcher_foreground));
        Adapter adapter = new Adapter(arrayList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        final MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, arrayList.get(position).getImage());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    button.setImageResource(R.drawable.pause_music);
                }
                else
                {
                    mediaPlayer.start();
                    button.setImageResource(R.drawable.play_music);
                }
            }
        });


    }
}