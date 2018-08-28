package com.sc.jn.seoulclass;

/**
 * Created by Joa Chang Hwa on 2018-08-22.
 */
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sc.jn.seoulclass.Util.ReviewItem;
import com.sc.jn.seoulclass.Util.ReviewManageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReviewActivity extends AppCompatActivity {
    private ReviewManageAdapter reviewManageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        try {
            //==== REVIEW를 불러옵니다. ====//
            if(MainActivity.isLoggedIn) {
                new Review_management_find().execute("https://seoulclass.ml/review/management");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //==== ListView ====//
        final ListView listView = (ListView) findViewById(R.id.listView);
        reviewManageAdapter = new ReviewManageAdapter();
        listView.setAdapter(reviewManageAdapter);

        //==== ListView 클릭시 DetailActivity에 띄움 ====//
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReviewItem item = (ReviewItem) reviewManageAdapter.getItem(position);

                Intent intent= new Intent(ReviewActivity.this,DetailActivity.class);
                intent.putExtra("class_id",item.getClassID());
                startActivity(intent);
            }
        });
    }

    //==== REVIEW를 DB에서 불러오는 TASK ====//
    class Review_management_find extends AsyncTask<String,String,String> {

        HttpURLConnection con=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            BufferedReader reader= null;

            try{
                String POST_PARAMS="";

                POST_PARAMS="user_id="+MainActivity.user_id;
                POST_PARAMS+="&login_route="+MainActivity.login_route;

                URL url = null;
                url = new URL(urls[0]);

                con = (HttpURLConnection) url.openConnection();
                if(MainActivity.m_session){
                    Log.e("cookie","cookie working");
                    con.setRequestProperty("Cookie", MainActivity.m_cookies);
                }
                con.setRequestMethod("POST");
                con.setRequestProperty("Cache-Control","no-cache");
                con.setRequestProperty("Content-type","application/x-www-form-urlencoded");//서버에 전달 형식
                con.setRequestProperty("Accept","text/html");//서버에서 전달 받는 형식
                con.setDoOutput(true); //서버로 POST 데이터 넘김
                con.setDoInput(true); //서버로부터 응답 받음
                con.connect();

                OutputStream outputStream=con.getOutputStream();
                BufferedWriter writer =  new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(POST_PARAMS);
                writer.flush();
                writer.close();

                InputStream stream=con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer=new StringBuffer();

                String line="";
                while((line=reader.readLine())!=null){
                    buffer.append(line);
                }

                reader.close();
                con.disconnect();
                return buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result==null)
                return;

            try {
                ReviewItem item;
                JSONArray jsonArray=new JSONArray(result);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    if(object.getString("anonymous").equals("yes")){
                        item= new ReviewItem((float) object.getDouble("rating"), object.getString("contents"), "익명",object.getString("class_name"));
                    }
                    else {
                        item = new ReviewItem((float) object.getDouble("rating"), object.getString("contents"), object.getString("user_id") + "/" + object.getString("login_route"),object.getString("class_name"));
                    }
                    reviewManageAdapter.addItem(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


