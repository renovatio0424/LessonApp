package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.example.kimjungwon.lessonapp.URLconfig.MyURL;

/**
 * Created by kimjungwon on 2017-05-19.
 */

public class RecyclerAdapter_review extends RecyclerView.Adapter<RecyclerAdapter_review.mViewHolder> {
    Context context;
    private ArrayList<Review> reviews;
    int item_layout;
    String TAG = RecyclerAdapter_review.class.getSimpleName();

    public RecyclerAdapter_review(Context context, ArrayList<Review> reviews, int item_layout) {
        this.context = context;
        this.reviews = reviews;
        this.item_layout = item_layout;
    }

    @Override
    public RecyclerAdapter_review.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_review, null);

        return new RecyclerAdapter_review.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter_review.mViewHolder holder, int position) {
        Review review = reviews.get(position);

        Log.d(TAG,"content: " + review.getContent());

        if (!review.getUser_img().equals("null")) {
            Glide
                    .with(context)
                    .load(MyURL + review.getUser_img())
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(holder.usr_img);
        } else {
            Glide
                    .with(context)
                    .load(R.drawable.ic_user)
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(holder.usr_img);
        }


        holder.usr_id_tv.setText(review.getUser_name());
        holder.content_tv.setText(review.getContent());
        holder.ratingBar.setRating(review.getRating());

        SimpleDateFormat original = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date original_date = (Date) original.parse(review.getDate());
            String newDate = newFormat.format(original_date);
            holder.date_tv.setText(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.reviews.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        ImageView usr_img;
        TextView usr_id_tv, content_tv, date_tv;
        RatingBar ratingBar;

        public mViewHolder(View itemView) {
            super(itemView);
            usr_img = (ImageView) itemView.findViewById(R.id.rv_usr_img);
            usr_id_tv = (TextView) itemView.findViewById(R.id.rv_user_id);
            content_tv = (TextView) itemView.findViewById(R.id.rv_content);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rv_rating);
            date_tv = (TextView) itemView.findViewById(R.id.rv_date);
        }
    }

    public void clear() {
        reviews.clear();
    }

    public void addAll(ArrayList<Review> reviews) {
        this.reviews.addAll(reviews);
    }
}
