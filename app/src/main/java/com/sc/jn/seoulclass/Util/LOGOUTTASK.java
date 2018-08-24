package com.sc.jn.seoulclass.Util;

import android.os.AsyncTask;
import android.util.Log;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;
import com.sc.jn.seoulclass.MainActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public  class LOGOUTTASK extends AsyncTask<String,String,String> {
    HttpURLConnection con=null;
    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = null;
            url = new URL(urls[0]);

            con = (HttpURLConnection) url.openConnection();

            if(MainActivity.m_session){
                Log.e("cookie","cookie working");
                con.setRequestProperty("Cookie", MainActivity.m_cookies);
            }
            con.connect();

            Map<String, List<String>> imap = con.getHeaderFields();//뭐라도 수행해야됨
            con.disconnect();
            MainActivity.isLoggedIn=false;
            OAuthLogin.getInstance().logout(com.sc.jn.seoulclass.Login.mContext);

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
