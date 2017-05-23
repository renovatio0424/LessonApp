package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.kimjungwon.lessonapp.URLconfig.MyURL;

/**
 * Created by kimjungwon on 2017-04-27.
 */

public class RecyclerAdapter_chat extends RecyclerView.Adapter<RecyclerAdapter_chat.mViewHolder> {

    private static final String TAG = RecyclerAdapter_chat.class.getSimpleName();
    Context context;
    ArrayList<Chat> chats;
    int item_layout;

    public RecyclerAdapter_chat(Context context, ArrayList<Chat> chats, int item_layout) {
        this.context = context;
        this.chats = chats;
        this.item_layout = item_layout;
    }


    @Override
    public RecyclerAdapter_chat.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null);

        return new RecyclerAdapter_chat.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter_chat.mViewHolder holder, int position) {
        Chat chat = chats.get(position);

        if(chat.ismychat){
            holder.mylayout.setVisibility(View.VISIBLE);
            holder.yourlayout.setVisibility(View.INVISIBLE);

            holder.my_message.setText(chat.getMessage());
            holder.my_time.setText(chat.getTime());
        }else{
            holder.mylayout.setVisibility(View.INVISIBLE);
            holder.yourlayout.setVisibility(View.VISIBLE);

            if(chat.getName().startsWith("##")){
                holder.yourlayout.setVisibility(View.GONE);
                holder.yourImage.setVisibility(View.GONE);
                holder.your_time.setVisibility(View.GONE);
                holder.your_message.setVisibility(View.GONE);
            }else{
                holder.yourImage.setVisibility(View.VISIBLE);
                holder.your_time.setVisibility(View.VISIBLE);
                holder.your_message.setVisibility(View.VISIBLE);
            }

            holder.your_name.setText(chat.getName());
            holder.your_message.setText(chat.getMessage());
            holder.your_time.setText(chat.getTime());

            if(chat.getImageURL() != null){
                Glide.with(context).load(chat.getImageURL()).centerCrop().into(holder.yourImage);
                Log.d(TAG,"chat image url: " + chat.getImageURL());
            }else{
                Glide.with(context).load(R.drawable.ic_female_student).into(holder.yourImage);
            }

        }

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        TextView my_name,my_message,my_time,your_name,your_message,your_time;
        ImageView yourImage;
        RelativeLayout mylayout, yourlayout;

        public mViewHolder(View itemView) {
            super(itemView);
            my_message = (TextView) itemView.findViewById(R.id.my_message);
            my_time = (TextView) itemView.findViewById(R.id.my_time);

            your_name = (TextView) itemView.findViewById(R.id.opponant_name);
            your_message = (TextView) itemView.findViewById(R.id.opponant_message);
            your_time = (TextView) itemView.findViewById(R.id.opponant_time);
            yourImage = (ImageView) itemView.findViewById(R.id.opponant_image);

            mylayout = (RelativeLayout) itemView.findViewById(R.id.my_chat);
            yourlayout = (RelativeLayout) itemView.findViewById(R.id.yourchat);
        }
    }
}
