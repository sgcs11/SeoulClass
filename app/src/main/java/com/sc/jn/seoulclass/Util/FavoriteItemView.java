package com.sc.jn.seoulclass.Util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sc.jn.seoulclass.R;

public class FavoriteItemView extends LinearLayout {
    private TextView textView;

    public FavoriteItemView(Context context) {
        super(context);

        init(context);
    }

    public FavoriteItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//화면에 띄우지 않고 백그라운드 시스템 서비스
        inflater.inflate(R.layout.favoritelistview_item,this,true);

        textView = (TextView)findViewById(R.id.ClassItem);
    }

    public void setClassTitle(String ClassTitle){
        textView.setText(ClassTitle);
    }
}

