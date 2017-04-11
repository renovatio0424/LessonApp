package com.example.kimjungwon.lessonapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kimjungwon on 2017-04-08.
 */

public class CustomExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<String> mParentList;
    private ArrayList<String> mChildList;
    private ChildListViewHolder mChildListViewHolder;
    private HashMap<String, ArrayList<Area>> mChildHashMap;
    private static final String TAG = CustomExpandableListViewAdapter.class.getSimpleName();

    // CustomExpandableListViewAdapter 생성자
    public CustomExpandableListViewAdapter(Context context, ArrayList<String> parentList, HashMap<String, ArrayList<Area>> childHashMap) {
        this.mContext = context;
        this.mParentList = parentList;
        this.mChildHashMap = childHashMap;
    }

    /* ParentListView에 대한 method */
    @Override
    public String getGroup(int groupPosition) { // ParentList의 position을 받아 해당 TextView에 반영될 String을 반환
        return mParentList.get(groupPosition);
    }

    @Override
    public int getGroupCount() { // ParentList의 원소 개수를 반환
        return mParentList.size();
    }

    @Override
    public long getGroupId(int groupPosition) { // ParentList의 position을 받아 long값으로 반환
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) { // ParentList의 View
        if (convertView == null) {
            LayoutInflater groupInfla = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // ParentList의 layout 연결. root로 argument 중 parent를 받으며 root로 고정하지는 않음
            convertView = groupInfla.inflate(R.layout.item_parent, parent, false);
        }
//        Log.d(TAG,"getGroupView@@@@@@@@@@@@@@@@@@@@@@@@");

        // ParentList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView parentText = (TextView) convertView.findViewById(R.id.parenttext);
        parentText.setText(getGroup(groupPosition));
        return convertView;
    }

    /* 여기서부터 ChildListView에 대한 method */
    @Override
    public Area getChild(int groupPosition, int childPosition) { // groupPostion과 childPosition을 통해 childList의 원소를 얻어옴
        return this.mChildHashMap.get(this.mParentList.get(groupPosition)).get(childPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) { // ChildList의 크기를 int 형으로 반환
        return this.mChildHashMap.get(this.mParentList.get(groupPosition)).size();

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) { // ChildList의 ID로 long 형 값을 반환
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // ChildList의 View. 위 ParentList의 View를 얻을 때와 비슷하게 Layout 연결 후, layout 내 TextView, ImageView를 연결
        Area childData = (Area) getChild(groupPosition, childPosition);
        Log.d(TAG,"getChildView");

        if (convertView == null) {
            LayoutInflater childInfla = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = childInfla.inflate(R.layout.item_child, null);

            mChildListViewHolder = new ChildListViewHolder(convertView);
            mChildListViewHolder.mChildListViewText = (TextView) convertView.findViewById(R.id.childtext);

            convertView.setTag(mChildListViewHolder);
        } else {
            mChildListViewHolder = (ChildListViewHolder) convertView.getTag();
        }

        if(childData.getChecked()){
            mChildListViewHolder.mChildListViewIV.setVisibility(View.VISIBLE);
        }else {
            mChildListViewHolder.mChildListViewIV.setVisibility(View.GONE);
        }

        if(mChildListViewHolder.mChildListViewIV.getVisibility() == View.GONE){
            Log.d(TAG,"GONE");
        }else if(mChildListViewHolder.mChildListViewIV.getVisibility() == View.VISIBLE){
            Log.d(TAG,"VISIBLE");
        }else{
            Log.d(TAG,"INVISIBLE");
        }
        mChildListViewHolder.mChildListViewText.setText(childData.getAreaname());

        return convertView;

    }

    public class ChildListViewHolder {
        TextView mChildListViewText;
        ImageView mChildListViewIV;

        public ChildListViewHolder(View itemview){
            mChildListViewText = (TextView) itemview.findViewById(R.id.childtext);
            mChildListViewIV = (ImageView) itemview.findViewById(R.id.check_icon2);
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    } // stable ID인지 boolean 값으로 반환

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    } // 선택여부를 boolean 값으로 반환

}