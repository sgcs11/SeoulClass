package com.changhwa.logintest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {
    private Button button;
    private Button submit;
    private TextView textView;
    private RatingBar ratingBar;
    private EditText Review;
    private ReviewAdapter adapter;
    private CheckBox checkBox;

    String userid;
    String login_route;
    String contents;
    String anonymous="yes";
    String class_name="default";
    float rating;
    boolean download;//download이면 true 아니면 false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //==== intent 통해서 받아온 data ====//
        final Intent intent = getIntent();
        textView = (TextView)findViewById(R.id.textView);
        try {
            JSONObject jsonObject = new JSONObject(intent.getExtras().getString("Login_info"));
            userid=jsonObject.getString("userid");
            login_route=jsonObject.getString("type");
            //==== REVIEW를 불러옵니다. ====//
            new REVIEW_FIND().execute("https://seoulclass.ml/review/download");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //==== ListView ====//
        final ListView listView = (ListView)findViewById(R.id.listView);
        adapter = new ReviewAdapter();
        listView.setAdapter(adapter);

        //==== ListView에서 삭제 ====//
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReviewItem item = (ReviewItem)adapter.getItem(position);
                Toast.makeText(getApplicationContext(),"삭제:"+item.getID(),Toast.LENGTH_SHORT).show();
                show(position);
            }
        });

        //==== 익명 설정을 위한 Checkbox ====//
        checkBox=(CheckBox)findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()) {
                    anonymous="yes";
                } else {
                    anonymous="no";
                }
            }
        });

        textView.setText(userid+","+login_route);

        button = (Button)findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new LOGOUTTASK().execute("https://seoulclass.ml/logout");
                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    MainActivity.m_session = false;
                    Intent intent1 = new Intent(Login.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
            }
        });
        //==== ListView에 추가 ====//
        submit = (Button)findViewById(R.id.submit);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        Review = (EditText)findViewById(R.id.Review);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    rating=ratingBar.getRating();
                    contents=Review.getText().toString();

                    download=false;
                    String idtemp;
                    if(anonymous.equals("yes"))
                        idtemp="익명";
                    else
                        idtemp=userid+"/"+login_route;
                    adapter.addItem(new ReviewItem(ratingBar.getRating(),Review.getText().toString(),idtemp));
                    textView.setText(String.valueOf(ratingBar.getRating()+","+Review.getText().toString()));
            }
        });
    }

    //==== ListView 삭제할지 묻는 Dialog ====//
    void show(final int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SeoulClass");
        builder.setMessage("리뷰를 삭제하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ReviewItem object=(ReviewItem)adapter.getItem(position);
                        adapter.removeItem(position);
                        adapter.notifyDataSetChanged();
                        //==== REVIEW를 다시 불러옵니다. ====//
                        new REVIEW_FIND().execute("https://seoulclass.ml/review/download");
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }


    class LOGOUTTASK extends AsyncTask<String,String,String>{
        HttpURLConnection con=null;
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = null;
                url = new URL(urls[0]);

                con = (HttpURLConnection) url.openConnection();

                if(MainActivity.m_session){
                    Log.e("cookie","cookie working");
                    con.setRequestProperty("Cookie",MainActivity.m_cookies);
                }
                con.connect();

                Map<String, List<String>> imap = con.getHeaderFields();//뭐라도 수행해야됨
                con.disconnect();
                MainActivity.isLoggedIn=false;
                OAuthLogin.getInstance().logout(MainActivity.mContext);

                //==== kakao Logout =====//
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {

                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

    //==== ListView Adapter ====//
    class ReviewAdapter extends BaseAdapter {
        ArrayList<ReviewItem> items = new ArrayList<ReviewItem>();
        ReviewItem titem;

        @Override
        public int getCount() {//arraylist 갯수 반환
            return items.size();
        }

        public void addItem(ReviewItem item){
                if(!download) {
                    titem = item;
                    new REVIEW_SAVE().execute("https://seoulclass.ml/review/upload");
                }

               else{
                    items.add(item);
                    notifyDataSetChanged();
                }
        }
        public void removeItem(int position){
            //items.remove(position);
            items.clear();
            new REVIEW_REMOVE().execute("https://seoulclass.ml/review/remove");
        }

        @Override
        public Object getItem(int position) {//몇 번째 위치의 item을 반환
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {//몇 번째 item인지
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ReviewItemView view = null;
            if(convertView == null) {
                view = new ReviewItemView(getApplicationContext());
            }
            else{
                view = (ReviewItemView) convertView;
            }

            ReviewItem item = items.get(position);
            view.setID(item.getID());
            view.setContent(item.getContents());
            view.setRating(item.getRating());
            return view;
        }

        //==== REVIEW를 DB에서 삭제하는 TASK ====//
        class REVIEW_REMOVE extends AsyncTask<String,String,String>{

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

                    POST_PARAMS="user_id="+userid;
                    POST_PARAMS+="&login_route="+login_route;
                    POST_PARAMS+="&class_name="+class_name;

                    URL url = null;
                    url = new URL(urls[0]);

                    con = (HttpURLConnection) url.openConnection();
                    if(MainActivity.m_session){
                        Log.e("cookie","cookie working");
                        con.setRequestProperty("Cookie",MainActivity.m_cookies);
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
                finally {


                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            }
        }

        //==== REVIEW를 DB에 저장하는 TASK ====//
        class REVIEW_SAVE extends AsyncTask<String,String,String>{

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

                    POST_PARAMS="userid="+userid;
                    POST_PARAMS+="&login_route="+login_route;
                    POST_PARAMS+="&contents="+contents;
                    POST_PARAMS+="&anonymous="+anonymous;
                    POST_PARAMS+="&class_name="+class_name;
                    POST_PARAMS+="&rating="+rating;

                    URL url = null;
                    url = new URL(urls[0]);

                    con = (HttpURLConnection) url.openConnection();
                    if(MainActivity.m_session){
                        Log.e("cookie","cookie working");
                        con.setRequestProperty("Cookie",MainActivity.m_cookies);
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
                finally {


                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(result.equals("리뷰가 정상적으로 등록되었습니다.")) {
                    items.add(titem);
                    notifyDataSetChanged();
                }
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            }
        }
    }

    //==== REVIEW를 DB에서 불러오는 TASK ====//
    class REVIEW_FIND extends AsyncTask<String,String,String>{

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

                POST_PARAMS="class_name="+class_name;

                URL url = null;
                url = new URL(urls[0]);

                con = (HttpURLConnection) url.openConnection();
                if(MainActivity.m_session){
                    Log.e("cookie","cookie working");
                    con.setRequestProperty("Cookie",MainActivity.m_cookies);
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
            finally {


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
                        item= new ReviewItem((float) object.getDouble("rating"), object.getString("contents"), "익명");
                    }
                    else {
                        item = new ReviewItem((float) object.getDouble("rating"), object.getString("contents"), object.getString("user_id") + "/" + object.getString("login_route"));
                    }
                    download=true;
                    adapter.addItem(item);
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
