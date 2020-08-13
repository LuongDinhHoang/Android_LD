package com.example.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.recyclerview.adapter.ItemAdapter;
import com.example.recyclerview.adapter.SongAdapter;
import com.example.recyclerview.model.Songs;
import com.example.recyclerview.model.Student;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcvStudent;
    private List<Student> listStudent;
    private ItemAdapter adapter;
    private ArrayList<Songs> listSong;
    private SongAdapter adapterSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcvStudent = findViewById(R.id.rcvStudent);
        listStudent = new ArrayList<>();
        listSong = new ArrayList<>();
        getData();
        //getSong();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        rcvStudent.setLayoutManager(manager);
      adapter = new ItemAdapter(this,listStudent);
        adapterSong = new SongAdapter(this,listSong);
        rcvStudent.setAdapter(adapter);



    }
    public void getSong(){
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if(songCursor != null && songCursor.moveToFirst())
        {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

            do {
            long currentId = songCursor.getLong(songId);
            String currentTitle = songCursor.getString(songTitle);
            listSong.add(new Songs(currentId, currentTitle));
        } while(songCursor.moveToNext());
    }
    }
    
    private void getData() {
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG1","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG2","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));
        listStudent.add(new Student("HOANG","PT13352","http://giadinh.mediacdn.vn/2020/7/13/photo-1-1594613593967672774340.jpg"));

        adapter.setData(listStudent);
    }

}