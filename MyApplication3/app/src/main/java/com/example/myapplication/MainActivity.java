package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button btnLongin;
    public Button btntoast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLongin=(Button) findViewById(R.id.btn_Login);
        btnLongin.setOnClickListener(this);
        btntoast=(Button) findViewById(R.id.btn_toast);
        btntoast.setOnClickListener(this);
    }
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.btn_Login:
                Toast.makeText(this, "Chuyen Man", Toast.LENGTH_SHORT).show();
                Intent inten= new Intent(this, MainActivity2.class);
                startActivity(inten);
        }
    }


}
