package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView QLMonHoc;
    ArrayList<String> Array;
    Button btnthem;
    EditText edit;
    Button Sua,xoa;
    int vitri =-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               setContentView(R.layout.activity_main);
        btnthem = (Button) findViewById(R.id.btn_them);
        edit = (EditText) findViewById(R.id.Values);
        QLMonHoc = (ListView) findViewById(R.id.MonHoc);
        Sua =(Button) findViewById(R.id.btn_S);
        xoa =(Button) findViewById(R.id.btn_xoa);
        Array=new ArrayList<>();
        Array.add("Toán");
        Array.add("Lý");
        Array.add("Hóa");
        Array.add("Văn");
        Array.add("Sử");
        Array.add("Địa");
       final ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_expandable_list_item_1,Array);
       QLMonHoc.setAdapter(adapter);
       btnthem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String Edit = edit.getText().toString();
               Array.add(Edit);
               adapter.notifyDataSetChanged();
           }
       });
       QLMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               edit.setText(Array.get(i));
               vitri=i;
           }
       });
       Sua.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Array.set(vitri,edit.getText().toString());
               adapter.notifyDataSetChanged();
           }
       });
       xoa.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Array.remove(vitri);
               adapter.notifyDataSetChanged();;
           }
       });
    }
    public void chuyen(View view)
    {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);


    }


}