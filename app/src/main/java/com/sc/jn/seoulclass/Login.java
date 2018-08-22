package com.sc.jn.seoulclass;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Joa Chang Hwa on 2018-08-22.
 */

public class Login extends AppCompatActivity{
    private CallbackManager callbackManager;
    private Button button;
    private Button button2;
    private Button facebook_login;
    private Button Naver_login;
    private Button Kakako_login;
    private EditText ID;
    private EditText password;
    public static String m_cookies="";
    public static boolean m_session=false;
    public static boolean isLoggedIn=false;
    private enum Type {facebook, naver, kakao,local};
    private Type type;

    private JSONObject facebook_user;

    //==== NAVER LOGIN VARIABLES ====//
    OAuthLogin mOAuthLoginModule;
    public static Context mContext;

    //==== KAKAO LOGIN VARIABLES ====//
    private SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getHashKey();

        callbackManager = CallbackManager.Factory.create();

        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);

        facebook_login = (Button)findViewById(R.id.facebook_login);
        Naver_login=(Button)findViewById(R.id.Naver_login);
        Kakako_login=(Button)findViewById(R.id.Kakao_login);

        ID=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);

        //==== Local Login ====//
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=Type.local;

                if(!isLoggedIn) {
                    Toast.makeText(getApplicationContext(), "post", Toast.LENGTH_SHORT).show();

                    new PRETASK().execute("https://seoulclass.ml/test");
                    new POSTTASK().execute("https://seoulclass.ml/login");
                }
                else{
                    Toast.makeText(getApplicationContext(),"이미 로그인 되어 있습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //==== Local SignUp ====//
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,JOIN.class);
                startActivity(intent);
                finish();
            }
        });

        //==== facebook LOGIN ====//
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=Type.facebook;
                if(!isLoggedIn) {
                    LoginManager.getInstance().logInWithReadPermissions(Login.this,
                            Arrays.asList("public_profile","email"));

                    LoginManager.getInstance().registerCallback(callbackManager,
                            new FacebookCallback<LoginResult>() {
                                @Override
                                public void onSuccess(LoginResult loginResult) {
                                    Log.e("토큰",loginResult.getAccessToken().getToken());
                                    Log.e("유저아이디",loginResult.getAccessToken().getUserId());
                                    Log.e("퍼미션 리스트",loginResult.getAccessToken().getPermissions()+"");

                                    //==== 로그인 되었는지 여부 세팅====//
                                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                                    isLoggedIn = accessToken != null && !accessToken.isExpired();

                                    GraphRequest request =GraphRequest.newMeRequest(loginResult.getAccessToken() ,
                                            new GraphRequest.GraphJSONObjectCallback() {
                                                @Override
                                                public void onCompleted(JSONObject object, GraphResponse response) {
                                                    try {
                                                        facebook_user=object;
                                                        Log.e("user profile",object.toString());

                                                        JSONObject sObject = new JSONObject();
                                                        sObject.put("userid",facebook_user.get("email"));
                                                        sObject.put("nickname",facebook_user.get("name"));
                                                        sObject.put("type","facebook");
                                                        Intent intent = new Intent(Login.this, ReviewTest.class);
                                                        intent.putExtra("Login_info", sObject.toString());
                                                        startActivity(intent);
                                                        finish();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                    Bundle parameters = new Bundle();
                                    parameters.putString("fields", "id,name,email,gender,birthday,link");//요청할 내용들 이 중 있는거만 반환됨
                                    request.setParameters(parameters);
                                    request.executeAsync();
                                }

                                @Override
                                public void onCancel() {
                                    Log.e("onCancel", "onCancel");
                                }

                                @Override
                                public void onError(FacebookException error) {
                                    Log.e("onError", "onError " + error.getLocalizedMessage());
                                }
                            }
                    );

                }
                else{
                    Toast.makeText(getApplicationContext(),"이미 로그인 되어 있습니다.",Toast.LENGTH_SHORT).show();
                }


            }

        });
        //==== KAKAO LOGIN INFO ====//
        callback =  new SessionCallback();


        //==== KAKAO LOGIN ====//
        Kakako_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLoggedIn) {
                    type = Type.kakao;
                    Session.getCurrentSession().addCallback(callback);
                    Session.getCurrentSession().checkAndImplicitOpen();
                    Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, Login.this);
                }
                else{
                    Toast.makeText(getApplicationContext(),"이미 로그인 되어 있습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //==== NAVER LOGIN INFO ====//

        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                Login.this
                ,"2Qm8giKOCl7gUsN0mHoa"
                ,"w751IgpUiF"
                ,"SeoulClass"
        );

        //==== NAVER LOGIN ====//
        Naver_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=Type.naver;
                if(!isLoggedIn) {
                    mOAuthLoginModule.startOauthLoginActivity(Login.this, mOAuthLoginHandler);
                }
                else{
                    Toast.makeText(getApplicationContext(),"이미 로그인 되어 있습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //==== kakao Class ====//

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }
    private class SessionCallback implements ISessionCallback{

        @Override
        public void onSessionOpened() {
            List<String> keys = new ArrayList<>();
            keys.add("properties.nickname");
            keys.add("account.birthday");
            keys.add("kakao_account.email");
            keys.add("kakao_account.gender");

            UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    super.onFailure(errorResult);
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {

                }

                @Override
                public void onSuccess(MeV2Response result) {//로그인 성공
                    isLoggedIn=true;
                    Log.e("user id : ",""+result.getId());
                    Log.e("nickname",result.getNickname());
                    Log.e("mail",result.getKakaoAccount().getEmail());

                    Intent intent = new Intent(Login.this, ReviewTest.class);
                    intent.putExtra("Login_info", "{\"userid\":\""+result.getKakaoAccount().getEmail()+"\",\"nickname\":\""+result.getNickname()+"\",\"type\":\"kakao\"}");
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception!=null){
                com.kakao.util.helper.log.Logger.e(exception);
            }
        }
    }


    //==== Kakao, Facebook Result ====//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(type==Type.kakao) {
            if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }
        if(type==Type.facebook)
            callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                new RequestApiTask().execute();

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
            }
        };
    };

    private class RequestApiTask extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            final String accessToken = mOAuthLoginModule.getAccessToken(mContext);

            String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
            long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
            String tokenType = mOAuthLoginModule.getTokenType(mContext);
            isLoggedIn=true;

            String data = mOAuthLoginModule.requestApi(mContext,accessToken,"https://openapi.naver.com/v1/nid/me");
            Log.e("data", data);

            try {
                JSONObject object = new JSONObject(data);
                JSONObject sObject = new JSONObject();
                JSONObject temp = object.getJSONObject("response");
                sObject.put("userid", temp.get("email"));
                sObject.put("nickname",temp.get("nickname"));
                sObject.put("type","naver");
                String redata=sObject.toString();
                return redata;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return data;
        }

        protected void onPostExecute(String result) {
            Intent intent = new Intent(Login.this, ReviewTest.class);
            intent.putExtra("Login_info", result);
            startActivity(intent);
            finish();
        }
    }

//==== LOCAL LOGIN ====//

    class PRETASK extends AsyncTask<String,String,String>{
        HttpURLConnection con=null;
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = null;
                url = new URL(urls[0]);

                con = (HttpURLConnection) url.openConnection();

                con.connect();

                Map<String, List<String>> imap = con.getHeaderFields();
                if(!m_session) {

                    if (imap.containsKey("set-cookie")) {
                        List<String> cookies = imap.get("set-cookie");

                        for (int i = 0; i < cookies.size(); i++) {
                            m_cookies = "";
                            m_cookies += cookies.get(i);
                        }
                        Log.e("cookie content", m_cookies);
                        m_session = true;
                    } else {
                        m_session = false;
                    }
                }


                con.disconnect();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
    class POSTTASK extends AsyncTask<String,String,String>{

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
                POST_PARAMS+="&password="+password.getText().toString();

                URL url = null;
                url = new URL(urls[0]);

                con = (HttpURLConnection) url.openConnection();
                if(m_session){
                    Log.e("cookie","cookie working");
                    con.setRequestProperty("Cookie",m_cookies);
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
            if(!result.equals("아이디 또는 비밀번호가 일치하지 않습니다.")) {//로그인이 됐으면 화면 전환
                isLoggedIn=true;
                Intent intent = new Intent(Login.this, ReviewTest.class);
                intent.putExtra("Login_info", result);
                startActivity(intent);
                finish();
            }
        }
    }

    //HashKey 생성
    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    @Override
    public void onPause(){//앱 멈출 때 하지 말아야할 것
        super.onPause();
    }
    @Override
    public void onResume(){//앱 재부팅시 동작
        super.onResume();
    }

}