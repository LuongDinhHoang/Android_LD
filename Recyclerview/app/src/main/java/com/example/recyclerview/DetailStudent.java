package com.example.recyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.recyclerview.model.Student;

public class DetailStudent extends AppCompatActivity {
    private TextView tvName;
    private Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_student);
        tvName = findViewById(R.id.tvName);

        student= (Student) getIntent().getSerializableExtra("STUDENT");
        tvName.setText(student.getName());
    }
}