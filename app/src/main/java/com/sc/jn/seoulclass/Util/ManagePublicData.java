package com.sc.jn.seoulclass.Util;

import android.os.AsyncTask;
import android.util.Log;

import com.sc.jn.seoulclass.Model.ClassListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ManagePublicData {
    private static ManagePublicData managePublicData;

    ClassListItem classListItem;

    ClassListAdapter adapter;
    private ArrayList<ClassListItem> classListItemArrayList;

    public ParsePublicData parsePublicData;

    String tag_name = null;
    boolean bSet = false;

    public static ManagePublicData getInstance(ClassListAdapter adapter){
        if(managePublicData == null){
            managePublicData = new ManagePublicData(adapter);
        }
        return managePublicData;
    }

    private ManagePublicData(ClassListAdapter adapter) {
        classListItem = new ClassListItem();
        classListItemArrayList = new ArrayList<ClassListItem>();
        parsePublicData = new ParsePublicData();
        this.adapter = adapter;
    }

    public ArrayList<ClassListItem> getClassListItemArrayList(){
        return classListItemArrayList;
    }

    public void setClassListItemArrayList(ArrayList<ClassListItem> classListItemArrayList){
        this.classListItemArrayList = classListItemArrayList;
    }

    public class ParsePublicData extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String nm=strings[0];
            URL url = null;
            String str,receiveMsg=null;
            try{
                url = new URL("http://openapi.seoul.go.kr:8088/6746736e576a696e34315057467a49/json/GSListPublicReservationEducation/1/10/"+nm);
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

        }

        private void doJSONParser(String str){
            try{

                JSONObject json = new JSONObject(str);
                JSONObject list = json.getJSONObject("GSListPublicReservationEducation");
                JSONArray jarr = list.getJSONArray("row");

                for(int i=0 ; i< jarr.length(); i++){
                    json=jarr.getJSONObject(i);
                    String maxclassnm = json.getString("MAXCLASSNM");
                    String minclassnm = json.getString("MINCLASSNM");
                    String pay = json.getString("PAYATNM");
                    String title = json.getString("SVCNM");
                    String location = json.getString("PLACENM");
                    String usetgtinfo = json.getString("USETGTINFO");
                    String url = json.getString("SVCURL");
                    String opnbgndt = json.getString("SVCOPNBGNDT");
                    String opnenddt = json.getString("SVCOPNENDDT");
                    String rcptbgnt = json.getString("RCPTBGNDT");
                    String rcptenddt = json.getString("RCPTENDDT");


                    adapter.addItem( title,  location,  maxclassnm,  minclassnm,  pay,  usetgtinfo,  url,  opnbgndt,  opnenddt,  rcptbgnt,  rcptenddt);
                    adapter.notifyDataSetChanged();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }


        }
    }
}
