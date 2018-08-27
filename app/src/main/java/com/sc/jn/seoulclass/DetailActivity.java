package com.sc.jn.seoulclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sc.jn.seoulclass.Model.ClassListItem;
import com.sc.jn.seoulclass.Util.DetailAdapter;

public class DetailActivity extends AppCompatActivity {


    ClassListItem classListItem;
    TextView txt_title;

    public ClassListItem getClassListItem() {
        return classListItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        classListItem = (ClassListItem) intent.getSerializableExtra("obj");
        txt_title = (TextView)findViewById(R.id.dt_txt_title);

        DetailAdapter detailAdapter = new DetailAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager)findViewById(R.id.dt_viewPager);
        viewPager.setAdapter(detailAdapter);

        TabLayout mTab = (TabLayout)findViewById(R.id.dt_tabs);
        mTab.setupWithViewPager(viewPager);

        //==== adapter로 데이터 전달 ====//

    }

    @Override
    protected void onResume() {
        super.onResume();
        txt_title.setText(classListItem.getTitle());
//        DetailParser detailParser = new DetailParser(DetailActivity.this);
//        detailParser.execute(classListItem.getId());
    }
}
