package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kimjungwon on 2017-04-06.
 */

public class UnivAdapter extends RecyclerView.Adapter<UnivAdapter.mViewHolder>{

    Context context;
    private ArrayList<String> CollegeList;
    int item_layout;

    public UnivAdapter(Context context, ArrayList<String> Colleges, int item_layout){
        this.context = context;
        this.CollegeList = Colleges;
        this.item_layout = item_layout;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.item_layout,null);

        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        String college = CollegeList.get(position);

        holder.name_view.setText(college);
    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        TextView name_view;


        public mViewHolder(View itemView) {
            super(itemView);
            name_view = (TextView) itemView.findViewById(R.id.collegeview);

        }
    }

    @Override
    public int getItemCount() {
        return CollegeList.size();
    }

}