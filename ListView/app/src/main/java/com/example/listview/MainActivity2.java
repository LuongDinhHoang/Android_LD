package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
 EditText editt;
 Button chon;
 public  static  final  String CHUYEN ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editt=findViewById(R.id.valu);
        chon=findViewById(R.id.chuyen);
    }
    public  void actionn(View view)
    {
        Intent intent =new Intent(this,MainActivity3.class);
        String message = editt.getText().toString();
        intent.putExtra(CHUYEN,message);
        startActivity(intent);
        Toast toast =Toast.makeText(this,"chuyển thành công !",Toast.LENGTH_SHORT);
        toast.show();

    }
}