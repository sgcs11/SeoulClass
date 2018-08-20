package com.sc.jn.seoulclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.jn.seoulclass.Model.ClassListItem;
import com.sc.jn.seoulclass.Util.ClassListAdapter;
import com.sc.jn.seoulclass.Util.ClassListTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClassListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        String nm = getIntent().getStringExtra("nm");

        ListView listView;
        ClassListAdapter adapter;


         adapter = new ClassListAdapter();
        listView = (ListView)findViewById(R.id.listview_classList);
        listView.setAdapter(adapter);

        ClassListTask task = new ClassListTask(adapter);
        task.execute(nm);

    }
}
