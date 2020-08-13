package com.example.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String LOG = Activity.class.getSimpleName();
    public static final  String MESSAGE = "com.example.android.twoactivities.extra.MESSAGE";
    public  EditText messageedit ;
    public static final int TEXT_REQUEST = 1;
    private TextView RepText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageedit = findViewById(R.id.editText_main);
        RepText= findViewById(R.id.reply);

    }
    public void action(View view)
    {
        Log.d(LOG, "On Click. ");
        Intent intent =  new Intent(this,MainActivity5.class);
        String message = messageedit.getText().toString();
        intent.putExtra(MESSAGE,message);
        startActivityForResult(intent,TEXT_REQUEST);
        Toast toast= Toast.makeText(this,"chao mung",Toast.LENGTH_SHORT);
        toast.show();

    }
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String reply =
                        data.getStringExtra(MainActivity5.REPLY);
                RepText.setText(reply);
                RepText.setVisibility(View.VISIBLE);
            }
        }
    }


}