package com.sc.jn.seoulclass.Util;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sc.jn.seoulclass.Model.ClassListItem;
import com.sc.jn.seoulclass.R;

import java.util.ArrayList;

public class ClassListAdapter extends BaseAdapter{

    private ArrayList<ClassListItem> classItemList = new ArrayList<ClassListItem>();


    public ClassListAdapter() {
    }

    @Override
    public int getCount() {
        return classItemList.size();
    }

    @Override
    public Object getItem(int position) {
       return classItemList.get(position);
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }


    //position에 위치한 데이터를 출력하는데 사용될 View 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_class_list" Layout을 inflate하여 convertView 참조 획득.
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_class_list, parent, false);

            }
            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

            TextView titleTextView = (TextView)convertView.findViewById(R.id.txt_classList_title);
            TextView dateTextView = (TextView)convertView.findViewById(R.id.txt_classList_date);
            TextView locationTextView = (TextView)convertView.findViewById(R.id.txt_classList_location);

            ClassListItem classListItem = classItemList.get(position);

            titleTextView.setText(classListItem.getTitle());
            dateTextView.setText(classListItem.getOpnbgndt());
            locationTextView.setText(classListItem.getLocation());

            return convertView;

    }

    public void addItem(String title, String location, String maxclassnm, String minclassnm, String pay, String usetgtinfo, String url, String opnbgndt, String opnenddt, String rcptbgnt, String rcptenddt){
        ClassListItem item = new ClassListItem( title,  location,  maxclassnm,  minclassnm,  pay,  usetgtinfo,  url,  opnbgndt,  opnenddt,  rcptbgnt,  rcptenddt);


        classItemList.add(item);
    }
}
