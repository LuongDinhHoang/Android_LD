package com.example.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.model.Student;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private Context context;
    private List<Student> listStudent;
    private IItemClick mListener;

    public ItemAdapter(Context context, List<Student> listStudent) {
        this.context = context;
        this.listStudent = listStudent;
    }
    public void setClick(IItemClick iItemClick){
        mListener = iItemClick;
    }
    public void setData(List<Student> list){
        listStudent.clear();
        listStudent.addAll(list);
        notifyDataSetChanged();
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
        public void binData(final Student student) {
            name.setText(student.getName());
            className.setText(student.getClassName());
            Glide.with(context)
                    .load(student.getImage())
                    .error(R.drawable.ic_launcher_background)
                    .into(imgAvatar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

               //     mListener.clickItem(student);
                }
            });
        }
    }
    public interface IItemClick{
        void clickItem(Student student);
    }
}
