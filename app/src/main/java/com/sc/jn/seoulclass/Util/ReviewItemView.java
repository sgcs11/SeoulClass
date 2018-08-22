package com.sc.jn.seoulclass.Util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sc.jn.seoulclass.R;

public class ReviewItemView extends LinearLayout {
    private RatingBar ratingresult;
    private TextView textView;
    private TextView textView2;

    public ReviewItemView(Context context) {
        super(context);

        init(context);
    }

    public ReviewItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//화면에 띄우지 않고 백그라운드 시스템 서비스
        inflater.inflate(R.layout.listview_item,this,true);

        textView = (TextView)findViewById(R.id.IDitem);
        textView2 = (TextView)findViewById(R.id.Contentitem);
        ratingresult = (RatingBar)findViewById(R.id.ratingresult);

    }

    public void setRating(float rating){
        ratingresult.setRating(rating);
    }

    public void setID(String ID){
        textView.setText(ID);
    }
    public void setContent(String Content){
        textView2.setText(Content);
    }
}
