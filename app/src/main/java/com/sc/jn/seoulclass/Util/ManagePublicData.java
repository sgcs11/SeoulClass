package com.sc.jn.seoulclass.Util;

import android.app.Activity;

import android.app.ProgressDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sc.jn.seoulclass.MainActivity;
import com.sc.jn.seoulclass.Model.ClassListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ManagePublicData extends AppCompatActivity{
    private static ManagePublicData managePublicData;

    private Activity context;

    private ArrayList<ClassListItem> Category1ArrayList; //건강/스포츠
    private ArrayList<ClassListItem> Category2ArrayList; //취미레저
    private ArrayList<ClassListItem> Category3ArrayList; //취미
    private ArrayList<ClassListItem> Category4ArrayList; //자연/과학
    private ArrayList<ClassListItem> Category5ArrayList; //생활체육
    private ArrayList<ClassListItem> Category6ArrayList; //체험/견학
    private ArrayList<ClassListItem> Category7ArrayList; //교양
    private ArrayList<ClassListItem> Category8ArrayList; //역사
    private ArrayList<ClassListItem> Category9ArrayList;  //도시농업
    private ArrayList<ClassListItem> Category10ArrayList;  //정보통신
    private ArrayList<ClassListItem> Category11ArrayList;  //기타

    public ParsePublicData parsePublicData;


    public static ManagePublicData getInstance(Activity context){
        if(managePublicData == null){
            managePublicData = new ManagePublicData(context);
        }
        return managePublicData;
    }



    public static void reFreshManagePublicData(){
        managePublicData = null;
    }


    private ManagePublicData(Activity context) {
        this.context = context;
        Category1ArrayList = new ArrayList<ClassListItem>();
        Category2ArrayList = new ArrayList<ClassListItem>();
        Category3ArrayList = new ArrayList<ClassListItem>();
        Category4ArrayList = new ArrayList<ClassListItem>();
        Category5ArrayList = new ArrayList<ClassListItem>();
        Category6ArrayList = new ArrayList<ClassListItem>();
        Category7ArrayList = new ArrayList<ClassListItem>();
        Category8ArrayList = new ArrayList<ClassListItem>();
        Category9ArrayList = new ArrayList<ClassListItem>();
        Category10ArrayList = new ArrayList<ClassListItem>();
        Category11ArrayList = new ArrayList<ClassListItem>();

        parsePublicData = new ParsePublicData();

    }

    public static ManagePublicData getManagePublicData() {
        return managePublicData;
    }

    public ArrayList<ClassListItem> getCategory1ArrayList() {
        return Category1ArrayList;
    }

    public void setCategory1ArrayList(ArrayList<ClassListItem> category1ArrayList) {
        Category1ArrayList = category1ArrayList;
    }

    public ArrayList<ClassListItem> getCategory2ArrayList() {
        return Category2ArrayList;
    }

    public void setCategory2ArrayList(ArrayList<ClassListItem> category2ArrayList) {
        Category2ArrayList = category2ArrayList;
    }

    public ArrayList<ClassListItem> getCategory3ArrayList() {
        return Category3ArrayList;
    }

    public void setCategory3ArrayList(ArrayList<ClassListItem> category3ArrayList) {
        Category3ArrayList = category3ArrayList;
    }

    public ArrayList<ClassListItem> getCategory4ArrayList() {
        return Category4ArrayList;
    }

    public void setCategory4ArrayList(ArrayList<ClassListItem> category4ArrayList) {
        Category4ArrayList = category4ArrayList;
    }

    public ArrayList<ClassListItem> getCategory5ArrayList() {
        return Category5ArrayList;
    }

    public void setCategory5ArrayList(ArrayList<ClassListItem> category5ArrayList) {
        Category5ArrayList = category5ArrayList;
    }

    public ArrayList<ClassListItem> getCategory6ArrayList() {
        return Category6ArrayList;
    }

    public void setCategory6ArrayList(ArrayList<ClassListItem> category6ArrayList) {
        Category6ArrayList = category6ArrayList;
    }

    public ArrayList<ClassListItem> getCategory7ArrayList() {
        return Category7ArrayList;
    }

    public void setCategory7ArrayList(ArrayList<ClassListItem> category7ArrayList) {
        Category7ArrayList = category7ArrayList;
    }

    public ArrayList<ClassListItem> getCategory8ArrayList() {
        return Category8ArrayList;
    }

    public void setCategory8ArrayList(ArrayList<ClassListItem> category8ArrayList) {
        Category8ArrayList = category8ArrayList;
    }

    public ArrayList<ClassListItem> getCategory9ArrayList() {
        return Category9ArrayList;
    }

    public void setCategory9ArrayList(ArrayList<ClassListItem> category9ArrayList) {
        Category9ArrayList = category9ArrayList;
    }

    public ArrayList<ClassListItem> getCategory10ArrayList() {
        return Category10ArrayList;
    }

    public void setCategory10ArrayList(ArrayList<ClassListItem> category10ArrayList) {
        Category10ArrayList = category10ArrayList;
    }

    public ArrayList<ClassListItem> getCategory11ArrayList() {
        return Category11ArrayList;
    }

    public void setCategory11ArrayList(ArrayList<ClassListItem> category11ArrayList) {
        Category11ArrayList = category11ArrayList;
    }

    public class ParsePublicData extends AsyncTask<Void, Void, String>{
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("불러오는 중 입니다.");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... number) {
            URL url;
            String str,receiveMsg=null;
            try{
                url = new URL("http://openapi.seoul.go.kr:8088/6746736e576a696e34315057467a49/json/"+addressToSymbol()+"ListPublicReservationEducation/1/1000/");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                if(conn.getResponseCode() == conn.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer  = new StringBuffer();

                    while((str = reader.readLine())!=null){
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    reader.close();
                }else{
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return receiveMsg;
        }

        @Override
        protected void onPostExecute(String str) {
            doJSONParser(str);
            progressDialog.dismiss();
            if(context.getLocalClassName().equals("SplashActivity")){
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
            context.finish();
            super.onPostExecute(str);

        }

        private void doJSONParser(String str){
            try{
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");


                Log.d("jinho",date.toString());

                JSONObject json = new JSONObject(str);
                JSONObject list = json.getJSONObject(addressToSymbol()+"ListPublicReservationEducation");
                JSONArray jarr = list.getJSONArray("row");

                for(int i=0 ; i< jarr.length(); i++){

                    ClassListItem classListItem;
                    classListItem = new ClassListItem();

                    json=jarr.getJSONObject(i);
                    classListItem.setId(json.getString("SVCID"));
                    classListItem.setMaxclassnm(json.getString("MAXCLASSNM"));
                    classListItem.setMinclassnm(json.getString("MINCLASSNM"));
                    classListItem.setPay(json.getString("PAYATNM"));
                    classListItem.setTitle(json.getString("SVCNM"));
                    classListItem.setLocation(json.getString("PLACENM"));
                    classListItem.setUsetgtinfo(json.getString("USETGTINFO"));
                    classListItem.setUrl(json.getString("SVCURL"));
                    classListItem.setOpnbgndt(json.getString("SVCOPNBGNDT"));
                    classListItem.setOpnenddt(json.getString("SVCOPNENDDT"));
                    classListItem.setRcptbgnt(json.getString("RCPTBGNDT"));
                    classListItem.setRcptenddt(json.getString("RCPTENDDT"));

                    try{
                        Date Rcptenddt = dateFormat.parse(classListItem.getRcptenddt());
//                      접수기간 지나면 continue;
                        if(date.after(Rcptenddt))
                            continue;
                    }catch (ParseException e){
                        e.printStackTrace();
                    }


                    switch(nmToNumber(classListItem.getMinclassnm())){
                        case 1 :
                            Category1ArrayList.add(classListItem);
                            break;
                        case 2 :
                            Category2ArrayList.add(classListItem);
                            break;
                        case 3 :
                            Category3ArrayList.add(classListItem);
                            break;
                        case 4 :
                            Category4ArrayList.add(classListItem);
                            break;
                        case 5 :
                            Category5ArrayList.add(classListItem);
                            break;
                        case 6 :
                            Category6ArrayList.add(classListItem);
                            break;
                        case 7 :
                            Category7ArrayList.add(classListItem);
                            break;
                        case 8 :
                            Category8ArrayList.add(classListItem);
                            break;
                        case 9 :
                            Category9ArrayList.add(classListItem);
                            break;
                        case 10 :
                            Category10ArrayList.add(classListItem);
                            break;
                        case 11 :
                            Category11ArrayList.add(classListItem);
                            break;

                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }


        }

        private String addressToSymbol(){
            String result=null;

            switch(ManageSharedPreference.getPreference("address",context)){
                case "은평구" :
                    result="EP";
                    break;
                case "강서구" :
                    result="GS";
                    break;
                case "강남구" :
                    result="GN";
                    break;
                case "강동구" :
                    result="GD";
                    break;
                case "강북구" :
                    result="GB";
                    break;
                case "광진구" :
                    result="GJ";
                    break;
                case "관악구" :
                    result="GA";
                    break;
                case "구로구" :
                    result="GR";
                    break;
                case "금천구" :
                    result="GC";
                    break;
                case "노원구" :
                    result="NW";
                    break;
                case "도봉구" :
                    result="DB";
                    break;
                case "동대문구" :
                    result="DDM";
                    break;
                case "동작구" :
                    result="DJ";
                    break;
                case "마포구" :
                    result="MP";
                    break;
                case "서대문구" :
                    result="SDM";
                    break;
                case "서초구" :
                    result="SC";
                    break;
                case "성동구" :
                    result="SD";
                    break;
                case "성북구" :
                    result="SB";
                    break;
                case "송파구" :
                    result="SP";
                    break;
                case "양천구" :
                    result="YC";
                    break;
                case "영등포구" :
                    result="YDP";
                    break;
                case "용산구" :
                    result="YS";
                    break;
                case "종로구" :
                    result="JN";
                    break;
                case "중구" :
                    result="JG";
                    break;
                case "중랑구" :
                    result="JR";
                    break;
            }


            return result;
        }
        private int nmToNumber(String nm){
            int result = 0;
            switch(nm){
                case "건강/스포츠" :
                    result= 1;
                    break;
                case "취미레저" :
                    result = 2;
                    break;
                case "취미" :
                    result = 3;
                    break;
                case "자연/과학" :
                    result = 4;
                    break;
                case "생활체육" :
                    result =  5;
                    break;
                case "체험/견학" :
                    result = 6;
                    break;
                case "교양" :
                    result = 7;
                    break;
                case "역사" :
                    result = 8;
                    break;
                case "도시농업" :
                    result = 9;
                    break;
                case "정보통신" :
                    result = 10;
                    break;
                case "기타" :
                    result = 11;
                    break;

            }
            return result;
        }
    }
}
