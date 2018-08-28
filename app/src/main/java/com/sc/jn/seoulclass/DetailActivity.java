package com.sc.jn.seoulclass;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sc.jn.seoulclass.Model.ClassListItem;
import com.sc.jn.seoulclass.Util.DetailAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {


    ClassListItem classListItem;
    TextView txt_title;
    private ImageView favorite;
    private boolean isPreferred=false;

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

        favorite = (ImageView)findViewById(R.id.favorite);
        if(MainActivity.isLoggedIn) {//로그인 되 있으면
            new favoriteTask().execute("https://seoulclass.ml/favorite/find");
        }
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.favorite_color);
                    if(MainActivity.isLoggedIn) {
                        if (!isPreferred) {
                            favorite.setColorFilter(color);
                            new favoriteTask().execute("https://seoulclass.ml/favorite/add");
                        } else {
                            favorite.clearColorFilter();
                            new favoriteTask().execute("https://seoulclass.ml/favorite/remove");
                        }
                    }
                }
            });
    }
    //==== REVIEW를 DB에서 불러오는 TASK ====//
    public class favoriteTask extends AsyncTask<String,String,String> {

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

                POST_PARAMS="user_id="+ MainActivity.user_id;
                POST_PARAMS+="&login_route="+MainActivity.login_route;
                POST_PARAMS+="&class_name="+ classListItem.getId();
                POST_PARAMS+="&class_title="+classListItem.getTitle();

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
            if(result==null) {
                return;
            }
            if(result.equals("favorite remove")){
                isPreferred=false;
                return;
            }
            if(result.equals("favorite add")){
                isPreferred=true;
                return;
            }

            try {
                if(!result.equals("no find")) {
                    isPreferred = true;
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.favorite_color);
                    favorite.setColorFilter(color);
                }
                else{
                    isPreferred=false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        txt_title.setText(classListItem.getTitle());
//        DetailParser detailParser = new DetailParser(DetailActivity.this);
//        detailParser.execute(classListItem.getId());
        if(MainActivity.isLoggedIn) {//로그인 되 있으면
            new favoriteTask().execute("https://seoulclass.ml/favorite/find");
        }

    }
}
