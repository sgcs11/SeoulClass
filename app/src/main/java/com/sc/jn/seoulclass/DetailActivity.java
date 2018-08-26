package com.sc.jn.seoulclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sc.jn.seoulclass.Model.ClassListItem;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        ClassListItem classListItem = (ClassListItem) intent.getSerializableExtra("obj");

        TextView tv_title = (TextView)findViewById(R.id.title);
        TextView tv_location = (TextView)findViewById(R.id.location);
        TextView tv_maxclassnm = (TextView)findViewById(R.id.maxclassnm);
        TextView tv_minclassnm = (TextView)findViewById(R.id.minclassnm);
        TextView tv_pay = (TextView)findViewById(R.id.pay);
        TextView tv_usetgtinfo = (TextView)findViewById(R.id.usetgtinfo);
        TextView tv_url = (TextView)findViewById(R.id.url);
        TextView tv_opnbgndt = (TextView)findViewById(R.id.opnbgndt);
        TextView tv_opnenddt = (TextView)findViewById(R.id.opnenddt);
        TextView tv_rcptbgnt = (TextView)findViewById(R.id.rcptbgnt);
        TextView tv_rcptenddt = (TextView)findViewById(R.id.rcptenddt);


        tv_title.setText(classListItem.getTitle());
        tv_location.setText(classListItem.getLocation());
        tv_maxclassnm.setText(classListItem.getMaxclassnm());
        tv_minclassnm.setText(classListItem.getMinclassnm());
        tv_pay.setText(classListItem.getPay());
        tv_usetgtinfo.setText(classListItem.getUsetgtinfo());
        tv_url.setText(classListItem.getUrl());
        tv_opnbgndt.setText(classListItem.getOpnbgndt());
        tv_opnenddt.setText(classListItem.getOpnenddt());
        tv_rcptbgnt.setText(classListItem.getRcptbgnt());
        tv_rcptenddt.setText(classListItem.getRcptenddt());
    }
}
