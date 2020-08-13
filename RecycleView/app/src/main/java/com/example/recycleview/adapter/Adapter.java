package com.example.recycleview.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleview.MainActivity2;
import com.example.recycleview.R;

import java.util.ArrayList;
import model.student;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList<student> ListStudent;
    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item,parent,false);
        return new  ViewHolder(view);
    }

    public Adapter(ArrayList<student> listStudent, Context context) {
        ListStudent = listStudent;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.BinData(ListStudent.get(position));
    }

    @Override
    public int getItemCount() {
        return ListStudent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,title;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name =(TextView) itemView.findViewById(R.id.Name);
            title =(TextView) itemView.findViewById(R.id.Title);
            image =(ImageView) itemView.findViewById(R.id.Image);


        }
        public  void  BinData(final student student)
        {
        name.setText(student.getName());
        title.setText(student.getTitle());
        image.setImageResource(student.getImage());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity2.class) ; }
        });
        }
    }
}
