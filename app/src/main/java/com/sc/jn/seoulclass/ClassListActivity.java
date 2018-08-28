package com.sc.jn.seoulclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sc.jn.seoulclass.Model.ClassListItem;
import com.sc.jn.seoulclass.Util.ClassListAdapter;
import com.sc.jn.seoulclass.Util.ManagePublicData;

import java.util.ArrayList;

public class ClassListActivity extends AppCompatActivity {

    ClassListAdapter adapter;
    Toolbar toolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        int nm = getIntent().getIntExtra("nm",0);
        String title = getIntent().getStringExtra("title");
        ArrayList<ClassListItem> temp;
        temp = selectCategory(nm);

        ListView listView;
        toolbar = (Toolbar) findViewById(R.id.dt_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView)findViewById(R.id.dt_txt_toolbar)).setText(title);


        adapter = new ClassListAdapter(temp);
        listView = (ListView)findViewById(R.id.listview_classList);
        listView.setEmptyView(findViewById(R.id.listview_empty));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
                ClassListItem item =(ClassListItem) adapter.getItem(position);
                intent.putExtra("obj", item );
                adapter.getItem(position);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_classlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.filter :
                return true;
        }
        return true;
    }

    private ArrayList<ClassListItem> selectCategory(int nm){
        ArrayList<ClassListItem> temp=null;
        switch(nm){
            case 1 :{
                temp = ManagePublicData.getInstance(ClassListActivity.this).getCategory1ArrayList();
                break;
            }
            case 2 :{
                temp = ManagePublicData.getInstance(ClassListActivity.this).getCategory2ArrayList();
                break;
            }
            case 3 :{
                temp = ManagePublicData.getInstance(ClassListActivity.this).getCategory3ArrayList();
                break;
            }
            case 4 :{
                temp = ManagePublicData.getInstance(ClassListActivity.this).getCategory4ArrayList();
                break;
            }
            case 5 :{
                temp = ManagePublicData.getInstance(ClassListActivity.this).getCategory5ArrayList();
                break;
            }
            case 6 :{
                temp = ManagePublicData.getInstance(ClassListActivity.this).getCategory6ArrayList();
                break;
            }
            case 7 :{
                temp = ManagePublicData.getInstance(ClassListActivity.this).getCategory7ArrayList();
                break;
            }
            case 8 :{
                temp = ManagePublicData.getInstance(ClassListActivity.this).getCategory8ArrayList();
                break;
            }
            case 9 :{
                temp = ManagePublicData.getInstance(ClassListActivity.this).getCategory9ArrayList();
                break;
            }
            case 10 :{
                temp = ManagePublicData.getInstance(ClassListActivity.this).getCategory10ArrayList();
                break;
            }
            case 11 :{
                temp = ManagePublicData.getInstance(ClassListActivity.this).getCategory11ArrayList();
                break;
            }
        }

        return temp;

    }
}