package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kimjungwon on 2017-03-14.
 */

public class Addr_Adapter extends BaseAdapter {

    private ArrayList<String> addressItems = new ArrayList<>();

    @Override
    public int getCount() {
        return addressItems.size();
    }

    @Override
    public Object getItem(int i) {
        return addressItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public ArrayList getItemList(){
        return this.addressItems;
    }

    public void setItemList(ArrayList list){
        this.addressItems = list;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int position = i ;
        Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_addr,viewGroup,false);
        }

        TextView areaview = (TextView) view.findViewById(R.id.addr);

        String address = addressItems.get(position);

        areaview.setText(address);

        return view;
    }
}

