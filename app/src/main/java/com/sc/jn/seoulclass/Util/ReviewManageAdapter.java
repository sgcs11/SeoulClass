package com.sc.jn.seoulclass.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Joa Chang Hwa on 2018-08-28.
 */

//==== ListView Adapter ====//
public class ReviewManageAdapter extends BaseAdapter {
    ArrayList<ReviewItem> items = new ArrayList<ReviewItem>();

    @Override
    public int getCount() {//arraylist 갯수 반환
        return items.size();
    }

    public void addItem(ReviewItem item){
        items.add(item);
        notifyDataSetChanged();
    }
    public void removeItem(int position){
        items.remove(position);
    }

    @Override
    public Object getItem(int position) {//몇 번째 위치의 item을 반환
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {//몇 번째 item인지
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewItemView view = null;
        Context context = parent.getContext();
        if(convertView == null) {
            view = new ReviewItemView(context);
        }
        else{
            view = (ReviewItemView) convertView;
        }

        ReviewItem item = items.get(position);
        view.setID(item.getID());
        view.setContent(item.getContents());
        view.setRating(item.getRating());
        return view;
    }
}
