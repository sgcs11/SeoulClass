package com.sc.jn.seoulclass.Fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.sc.jn.seoulclass.DetailActivity;
import com.sc.jn.seoulclass.MainActivity;
import com.sc.jn.seoulclass.R;
import com.sc.jn.seoulclass.Util.ReviewItem;
import com.sc.jn.seoulclass.Util.ReviewItemView;

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

public class ReviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private Button submit;
    private RatingBar ratingBar;
    private EditText Review;
    private ReviewAdapter Reviewadapter;
    private CheckBox checkBox;

    String contents;
    String anonymous="yes";
    String class_name="";
    float rating;
    boolean download;//download이면 true 아니면 false;

    public ReviewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReviewFragment newInstance() {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public void setClass_name(String str){
        class_name=str;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_review, container, false);

        //==== Class_name을 세팅합니다. ====//
        class_name=((DetailActivity)getActivity()).getClassListItem().getId();

        //==== REVIEW를 불러옵니다. ====//
        new REVIEW_FIND().execute("https://seoulclass.ml/review/download");

        //==== ListView ====//
        final ListView reviewlistView = (ListView)view.findViewById(R.id.listView);
        Reviewadapter = new ReviewAdapter();
        reviewlistView.setAdapter(Reviewadapter);

        //==== ListView에서 삭제 ====//
        reviewlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReviewItem item = (ReviewItem) Reviewadapter.getItem(position);
                //Toast.makeText(getApplicationContext(),"삭제:"+item.getID(),Toast.LENGTH_SHORT).show();
                show(position);
            }
        });

        //==== 익명 설정을 위한 Checkbox ====//
        checkBox=(CheckBox)view.findViewById(R.id.checkBox);
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

        //==== ListView에 추가 ====//
        submit = (Button)view.findViewById(R.id.submit);
        ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        Review = (EditText)view.findViewById(R.id.Review);
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
                    idtemp= MainActivity.user_id+"/"+MainActivity.login_route;
                Reviewadapter.addItem(new ReviewItem(ratingBar.getRating(),Review.getText().toString(),idtemp,class_name));
            }
        });
        return view;
    }

    //==== ListView 삭제할지 묻는 Dialog ====//
    void show(final int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("SeoulClass");
        builder.setMessage("리뷰를 삭제하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ReviewItem object=(ReviewItem) Reviewadapter.getItem(position);
                        Reviewadapter.removeItem(position);
                        Reviewadapter.notifyDataSetChanged();
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

    //==== ListView Adapter ====//
    class ReviewAdapter extends BaseAdapter {
        ArrayList<ReviewItem> items = new ArrayList<ReviewItem>();
        ReviewItem titem;

        @Override
        public int getCount() {//arraylist 갯수 반환
            return items.size();
        }

        public void addItem(ReviewItem item){
            if (!download) {
                titem = item;
                if(MainActivity.isLoggedIn)
                    new REVIEW_SAVE().execute("https://seoulclass.ml/review/upload");
                else
                    Toast.makeText(getActivity(),"로그인 후 이용해 주세요.",Toast.LENGTH_SHORT).show();
            } else {
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
                view = new ReviewItemView(getActivity());
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
        class REVIEW_REMOVE extends AsyncTask<String,String,String> {

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
                    POST_PARAMS+="&class_name="+class_name;

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
                finally {


                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
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

                    POST_PARAMS="userid="+MainActivity.user_id;
                    POST_PARAMS+="&login_route="+MainActivity.login_route;
                    POST_PARAMS+="&contents="+contents;
                    POST_PARAMS+="&anonymous="+anonymous;
                    POST_PARAMS+="&class_name="+class_name;
                    POST_PARAMS+="&rating="+rating;

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
                Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
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
                        item= new ReviewItem((float) object.getDouble("rating"), object.getString("contents"), "익명",class_name);
                    }
                    else {
                        item = new ReviewItem((float) object.getDouble("rating"), object.getString("contents"), object.getString("user_id") + "/" + object.getString("login_route"),class_name);
                    }
                    download=true;
                    Reviewadapter.addItem(item);
                    Reviewadapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
