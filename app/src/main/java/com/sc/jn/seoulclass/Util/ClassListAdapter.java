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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ClassListAdapter extends BaseAdapter{

    private ArrayList<ClassListItem> classItemList = new ArrayList<ClassListItem>();



    public ClassListAdapter(ArrayList<ClassListItem> classItemList) {
        this.classItemList = classItemList;
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

        TextView title = (TextView)convertView.findViewById(R.id.txt_classList_title);
        TextView place = (TextView)convertView.findViewById(R.id.txt_classList_place);
        TextView rdate = (TextView)convertView.findViewById(R.id.txt_classList_rdate);
        TextView sdate = (TextView)convertView.findViewById(R.id.txt_classList_sdate);
        TextView pay = (TextView)convertView.findViewById(R.id.txt_classList_pay);
        TextView use = (TextView)convertView.findViewById(R.id.txt_classList_use);



        ClassListItem classListItem = classItemList.get(position);

        String Rcptbgnt= classListItem.getRcptbgnt().split(" ")[0];
        String Rcptenddt= classListItem.getRcptenddt().split(" ")[0];
        String Opnbgndt= classListItem.getOpnbgndt().split(" ")[0];
        String Opnenddt= classListItem.getOpnenddt().split(" ")[0];


        title.setText(classListItem.getTitle());
        place.setText(classListItem.getLocation());
        rdate.setText("접수기간 : "+Rcptbgnt + " ~ " + Rcptenddt);
        sdate.setText("이용기간 : "+Opnbgndt + " ~ " + Opnenddt);
        pay.setText("결제방법 : "+classListItem.getPay());
        use.setText("대상 : "+classListItem.getUsetgtinfo());
        return convertView;

    }

}
