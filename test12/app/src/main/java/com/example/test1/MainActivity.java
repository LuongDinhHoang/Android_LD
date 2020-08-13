package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private  int count=0;
    private TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show=(TextView) findViewById(R.id.show_count);
    }
    public void showToast(View view) {
        Toast toast = Toast.makeText(this, "hello",
                Toast.LENGTH_SHORT);
        toast.show();
    }
    public  void  show(View view)
    {
        count++;
        if (show != null)
        {
            show.setText(Integer.toString(count));
        }
    }
}