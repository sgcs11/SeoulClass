package com.sc.jn.seoulclass.Util;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sc.jn.seoulclass.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

public class ClassListTask extends AsyncTask<String, Void, String>{

    View view;
    public ClassListTask(View view) {
        this.view = view;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        String str,receiveMsg=null;
        try{
            url = new URL("http://openapi.seoul.go.kr:8088/6746736e576a696e34315057467a49/json/EPListPublicReservationEducation/1/1/");
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
    protected void onPostExecute(String s) {
        TextView test = view.findViewById(R.id.test);
        test.setText(s);
    }
}
