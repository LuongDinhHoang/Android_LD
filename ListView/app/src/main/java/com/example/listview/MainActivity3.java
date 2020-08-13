package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity3 extends AppCompatActivity {
    TextView nhannay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        nhannay= findViewById(R.id.nhan);
        Intent intent=getIntent();
        String nhan =intent.getStringExtra(MainActivity2.CHUYEN);
        nhannay.setText(nhan);

    }
}