package com.example.fragment;

import androidx.appcompat.app.AppCompatActivity;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_a:
                FragmentManager fragmentManager= getFragmentManager();
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                FragmentA fragmentA= new FragmentA();
                fragmentTransaction.add(R.id.content,fragmentA);
                fragmentTransaction.commit();
                break;
            case R.id.btn_b:
                break;
        }

    }
}