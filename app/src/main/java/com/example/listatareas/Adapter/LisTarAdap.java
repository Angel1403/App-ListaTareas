package com.example.listatareas.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listatareas.AggNuevTar;
import com.example.listatareas.MainActivity;
import com.example.listatareas.Model.LisTarModel;
import com.example.listatareas.R;
import com.example.listatareas.Utils.DataBaseHelper;

import java.util.List;

public class LisTarAdap extends RecyclerView.Adapter<LisTarAdap.MyViewHolder> {

    private List<LisTarModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public LisTarAdap(DataBaseHelper myDB , MainActivity activity){
        this.activity = activity;
        this.myDB = myDB;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout , parent , false);
       return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final LisTarModel item = mList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    myDB.updateStatus(item.getId() , 1);
                }else
                    myDB.updateStatus(item.getId() , 0);
            }
        });
    }

    public boolean toBoolean(int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }

    public void setTasks(List<LisTarModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deletTask(int position){
        LisTarModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        LisTarModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("task" , item.getTask());

        AggNuevTar task = new AggNuevTar();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());
        

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
