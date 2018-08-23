package com.sc.jn.seoulclass;
/**
 * Created by Joa Chang Hwa on 2018-08-22.
 */


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class JOIN extends AppCompatActivity{
    private EditText ID;
    private EditText PWD;
    private EditText CONFIRM;
    private EditText PHONE;

    private TextView IDtext;
    private TextView PWDtext;
    private TextView CONFIRMtext;
    private TextView PHONEtext;

    private TextView IDpointer;
    private TextView PWDpointer;
    private TextView Confirmpointer;
    private TextView Phonepointer;

    private LinearLayout id_underline;
    private LinearLayout pwd_underline;
    private LinearLayout confirm_underline;
    private LinearLayout phone_underline;

    private ImageView idcheck_image;
    private ImageView pwdcheck_image;
    private ImageView confirmcheck_image;
    private ImageView phonecheck_image;

    private boolean IDok=false;
    private boolean PWDok=false;
    private boolean CONFIRMok=false;
    private boolean PHONEok=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ID=(EditText)findViewById(R.id.editID);
        PWD=(EditText)findViewById(R.id.editPWD);
        CONFIRM=(EditText)findViewById(R.id.editConfirm);
        PHONE=(EditText)findViewById(R.id.editPHONE);

        IDtext=(TextView)findViewById(R.id.idtext);
        PWDtext=(TextView)findViewById(R.id.pwdtext);
        CONFIRMtext=(TextView)findViewById(R.id.confirmtext);
        PHONEtext=(TextView)findViewById(R.id.phonetext);

        IDpointer=(TextView)findViewById(R.id.IDpointer);
        PWDpointer=(TextView)findViewById(R.id.PWDpointer);
        Confirmpointer=(TextView)findViewById(R.id.Confirmpointer);
        Phonepointer=(TextView)findViewById(R.id.Phonepointer);

        id_underline=(LinearLayout)findViewById(R.id.id_underline);
        pwd_underline=(LinearLayout)findViewById(R.id.pwd_underline);
        confirm_underline=(LinearLayout)findViewById(R.id.confirm_underline);
        phone_underline=(LinearLayout)findViewById(R.id.phone_underline);

        idcheck_image=(ImageView)findViewById(R.id.idcheck_image);
        pwdcheck_image=(ImageView)findViewById(R.id.pwdcheck_image);
        confirmcheck_image=(ImageView)findViewById(R.id.confirmcheck_image);
        phonecheck_image=(ImageView)findViewById(R.id.phonecheck_image);

        int color = ContextCompat.getColor(getApplicationContext(), R.color.coloreditNormal);
        idcheck_image.setColorFilter(color);
        pwdcheck_image.setColorFilter(color);
        confirmcheck_image.setColorFilter(color);
        phonecheck_image.setColorFilter(color);

        ID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    IDpointer.setTextColor(Color.parseColor("#000000"));
                    IDtext.setText("");
                    //ID.getBackground().mutate().clearColorFilter();
                    id_underline.setBackgroundResource(R.drawable.blue_underline);
                }
                else{
                    IDpointer.setTextColor(Color.parseColor("#c0c0c0"));
                    if(ID.getText().toString().length()==0){
                        IDtext.setText("아이디를 입력해주세요");
                        id_underline.setBackgroundResource(R.drawable.red_underline);
                    }
                    else if(ID.getText().toString().length()<2){
                        IDtext.setText("아이디는 두 글자 이상 입력해주세요");
                        id_underline.setBackgroundResource(R.drawable.red_underline);
                    }
                    else if(!Pattern.matches("^[a-zA-Z0-9]*$",ID.getText().toString())){
                        IDtext.setText("아이디 형식은 다음과 같습니다.(영문, 숫자만 가능)");
                        id_underline.setBackgroundResource(R.drawable.red_underline);
                        //ID.getBackground().mutate().setColorFilter(getResources().getColor(R.color.coloreditError), PorterDuff.Mode.SRC_ATOP);
                    }
                    else {
                        IDtext.setText("");
                        id_underline.setBackgroundResource(R.drawable.black_underline);
                    }
                }
            }
        });
        ID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Pattern.matches("^[a-zA-Z0-9]*$",ID.getText().toString())&&ID.getText().toString().length()>=2){
                    //체크 표시 색깔 변함
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.coloreditActive);
                    idcheck_image.setColorFilter(color);
                    IDok=true;
                }
                else{
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.coloreditNormal);
                    idcheck_image.setColorFilter(color);
                    IDok=false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        PWD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    PWDpointer.setTextColor(Color.parseColor("#000000"));
                    PWDtext.setText("");
                    pwd_underline.setBackgroundResource(R.drawable.blue_underline);
                }
                else{
                    PWDpointer.setTextColor(Color.parseColor("#c0c0c0"));
                    if(PWD.getText().toString().length()==0){
                        PWDtext.setText("비밀번호를 입력해주세요");
                        pwd_underline.setBackgroundResource(R.drawable.red_underline);
                    }
                    else if(PWD.getText().toString().length()<8){
                        PWDtext.setText("비밀번호를 8-20자로 입력해주세요");
                        pwd_underline.setBackgroundResource(R.drawable.red_underline);
                    }
                    else {
                        PWDtext.setText("");
                        pwd_underline.setBackgroundResource(R.drawable.black_underline);
                    }

                }
            }
        });
        PWD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(PWD.getText().toString().length()>=8){
                        int color = ContextCompat.getColor(getApplicationContext(), R.color.coloreditActive);
                        pwdcheck_image.setColorFilter(color);
                        PWDok=true;
                    }
                    else{
                        int color = ContextCompat.getColor(getApplicationContext(), R.color.coloreditNormal);
                        pwdcheck_image.setColorFilter(color);
                        PWDok=false;
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        CONFIRM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Confirmpointer.setTextColor(Color.parseColor("#000000"));
                    CONFIRMtext.setText("");
                    confirm_underline.setBackgroundResource(R.drawable.blue_underline);
                }
                else{
                    Confirmpointer.setTextColor(Color.parseColor("#c0c0c0"));
                    if(CONFIRM.getText().toString().length()==0){
                        CONFIRMtext.setText("비밀번호를 다시 한 번 입력해주세요");
                        confirm_underline.setBackgroundResource(R.drawable.red_underline);
                    }
                    else if(!PWD.getText().toString().equals(CONFIRM.getText().toString())){
                        CONFIRMtext.setText("비밀번호가 일치하지 않습니다");
                        confirm_underline.setBackgroundResource(R.drawable.red_underline);
                    }
                    else{
                        CONFIRMtext.setText("");
                        confirm_underline.setBackgroundResource(R.drawable.black_underline);
                    }

                }
            }
        });
        CONFIRM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(PWD.getText().toString().equals(CONFIRM.getText().toString())){
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.coloreditActive);
                    confirmcheck_image.setColorFilter(color);
                    CONFIRMok=true;
                }
                else{
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.coloreditNormal);
                    confirmcheck_image.setColorFilter(color);
                    CONFIRMok=false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        PHONE.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Phonepointer.setTextColor(Color.parseColor("#000000"));
                    PHONEtext.setText("");
                    phone_underline.setBackgroundResource(R.drawable.blue_underline);
                }
                else{
                    Phonepointer.setTextColor(Color.parseColor("#c0c0c0"));
                    if(PHONE.getText().toString().length()==0)
                    {
                        PHONEtext.setText("휴대전화 번호를 입력해주세요");
                        phone_underline.setBackgroundResource(R.drawable.red_underline);
                    }
                    else if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$",PHONE.getText().toString())){
                        PHONEtext.setText("휴대전화 번호 형식으로 입력해주세요. (-없이 번호만 입력)");
                        phone_underline.setBackgroundResource(R.drawable.red_underline);
                    }
                    else{
                        PHONEtext.setText("");
                        phone_underline.setBackgroundResource(R.drawable.black_underline);
                    }
                }
            }
        });
        PHONE.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$",PHONE.getText().toString())){
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.coloreditActive);
                    phonecheck_image.setColorFilter(color);
                    PHONEok=true;
                }
                else{
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.coloreditNormal);
                    phonecheck_image.setColorFilter(color);
                    PHONEok=false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_join_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_signup:
                if(IDok&&PWDok&&CONFIRMok&&PHONEok) {
                    new JOINTASK().execute("https://seoulclass.ml/signup");
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                POST_PARAMS+="&phone="+PHONE.getText().toString();

                URL url = null;
                url = new URL(urls[0]);

                con = (HttpURLConnection) url.openConnection();
                if(Login.m_session){
                    Log.e("cookie","cookie working");
                    con.setRequestProperty("Cookie",Login.m_cookies);
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

