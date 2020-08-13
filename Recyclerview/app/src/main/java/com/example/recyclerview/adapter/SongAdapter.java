package com.example.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recyclerview.R;
import com.example.recyclerview.model.Songs;
import com.example.recyclerview.model.Student;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context context;
    private List<Songs> listStudent;
    private IItemClick mListener;

    public SongAdapter(Context context, List<Songs> listStudent) {
        this.context = context;
        this.listStudent = listStudent;
    }
    public void setClick(IItemClick iItemClick){
        mListener = iItemClick;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);// vẽ view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binData(listStudent.get(position)); // setdata vào từng vị trí
    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView className;
        ImageView imgAvatar;

        public ViewHolder(@NonNull View itemView) {   // ánh xạ  dữ liệu
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            className = itemView.findViewById(R.id.tvClass);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);

        }
        public void binData(final Songs student) {
            name.setText(student.getSongID()+"");
            className.setText(student.getSongTitle());
        }
    }
    public interface IItemClick{
        void clickItem(Student student);
    }
}
