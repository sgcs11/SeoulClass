package com.changhwa.logintest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JOIN extends AppCompatActivity{
    private Button button;
    private EditText ID;
    private EditText PWD;
    private EditText SEX;
    private EditText BIRTH;
    private EditText NAME;
    private EditText PHONE;
    private EditText CONFIRM;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        ID=(EditText)findViewById(R.id.editID);
        PWD=(EditText)findViewById(R.id.editPWD);
        SEX=(EditText)findViewById(R.id.editSEX);
        BIRTH=(EditText)findViewById(R.id.editBIRTH);
        NAME=(EditText)findViewById(R.id.editNAME);
        PHONE=(EditText)findViewById(R.id.editPHONE);
        CONFIRM=(EditText)findViewById(R.id.editConfirm);

        button = (Button)findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateValue=BIRTH.getText().toString();
                SimpleDateFormat df = new SimpleDateFormat("yyMMdd", Locale.KOREA);

                df.setLenient(false);
                Date formDate = null;

                try {
                    formDate = df.parse(dateValue);
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(),"잘못된 날짜 형식입니다.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!PWD.getText().toString().equals(CONFIRM.getText().toString()))
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                else
                    new JOINTASK().execute("https://seoulclass.ml/signup");
            }
        });

    }

    class JOINTASK extends AsyncTask<String,String,String> {

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

                POST_PARAMS="userid="+ID.getText().toString();
                POST_PARAMS+="&password="+PWD.getText().toString();
                POST_PARAMS+="&sex="+SEX.getText().toString();
                POST_PARAMS+="&birth="+BIRTH.getText().toString();
                POST_PARAMS+="&name="+NAME.getText().toString();
                POST_PARAMS+="&phone="+PHONE.getText().toString();

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
                return buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {

                con.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
        }

    }
}
