package com.example.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity5 extends AppCompatActivity {
    public static final String REPLY =
            "com.example.android.twoactivities.extra.REPLY";

    private EditText edit ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        edit = findViewById(R.id.editText_Rep);
        Intent intent =getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE);
        TextView text = findViewById(R.id.text_message);
        text.setText(message);
    }
    public  void Rep(View view)
    {
        String reply = edit.getText().toString();
        Intent intentt = new Intent();
        intentt.putExtra(REPLY,reply);
        setResult(RESULT_OK,intentt);
        finish();
    }
}