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
public class FavoriteAdapter extends BaseAdapter {
    ArrayList<FavoriteItem> items = new ArrayList<FavoriteItem>();

    @Override
    public int getCount() {//arraylist 갯수 반환
        return items.size();
    }

    public void addItem(FavoriteItem item){
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
        FavoriteItemView view = null;
        Context context = parent.getContext();
        if(convertView == null) {
            view = new FavoriteItemView(context);
        }
        else{
            view = (FavoriteItemView) convertView;
        }

        FavoriteItem item = items.get(position);
        view.setClassTitle(item.getClassTitle());
        return view;
    }
}
