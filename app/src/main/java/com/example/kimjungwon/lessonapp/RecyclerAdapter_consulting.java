package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kimjungwon on 2017-04-22.
 */

public class RecyclerAdapter_consulting extends RecyclerView.Adapter<RecyclerAdapter_consulting.mViewHolder> {
    Context context;
    private ArrayList<Consult> consults;
    int item_layout;
    String TAG = RecyclerAdapter_consulting.class.getSimpleName();

    public RecyclerAdapter_consulting(Context context, ArrayList<Consult> consults, int item_layout) {
        this.context = context;
        this.consults = consults;
        this.item_layout = item_layout;
    }

    @Override
    public RecyclerAdapter_consulting.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_consulting, null);

        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter_consulting.mViewHolder holder, int position) {
        Consult consult = consults.get(position);

        holder.lessonname.setText(consult.getLessonname());


        if(consult.getCategory().equals("제안")){
            holder.category.setBackgroundResource(R.color.Pink);
        }else if(consult.getCategory().equals("문의")){
            holder.category.setBackgroundResource(R.color.SkyBlue);
        }

        holder.category.setText(consult.getCategory());

        holder.consulee.setText(consult.getYourname());
        holder.state.setText(consult.getState());

        if (consult.getState().equals("상담중")) {
            holder.state.setBackgroundResource(R.color.Yellow);
        } else if (consult.getState().equals("수락 대기중")){
            holder.state.setBackgroundResource(R.color.Blue);
        }else if (consult.getState().equals("과외 거절")) {
            holder.state.setBackgroundResource(R.color.Red);
        } else if (consult.getState().equals("과외중")) {
            holder.state.setBackgroundResource(R.color.Orange);
        } else if (consult.getState().equals("과외 완료")) {
            holder.state.setBackgroundResource(R.color.Green);
        } else {
            holder.state.setBackgroundColor(Color.BLACK);
        }

        Log.d(TAG, "Reason: " + consult.getReason());
        if (consult.getReason().equals("null") | consult.getReason() == null) {
            holder.reason.setVisibility(View.GONE);
        } else {
            holder.reason.setVisibility(View.VISIBLE);
            holder.reason.setText("사유: " + consult.getReason());
        }

        if(consult.getConfiramtion() == 0){
            holder.confirmation.setVisibility(View.VISIBLE);
        }else{
            holder.confirmation.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return this.consults.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        TextView lessonname, state, consulee, reason, category, confirmation;

        public mViewHolder(View itemView) {
            super(itemView);
            lessonname = (TextView) itemView.findViewById(R.id.cs_lessonname);
            state = (TextView) itemView.findViewById(R.id.cs_state);
            consulee = (TextView) itemView.findViewById(R.id.cs_consulee);
            reason = (TextView) itemView.findViewById(R.id.cs_reason);
            category = (TextView) itemView.findViewById(R.id.consult_category);
            confirmation = (TextView) itemView.findViewById(R.id.confirmation);
        }
    }

    public void clear(){
        consults.clear();
    }

    public void addAll(ArrayList<Consult> consults){
        this.consults.addAll(consults);
    }
}
