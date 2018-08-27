package com.sc.jn.seoulclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sc.jn.seoulclass.Util.ManagePublicData;
import com.sc.jn.seoulclass.Util.ManageSharedPreference;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent;

        if(ManagePublicData.getManagePublicData()==null){
            if(ManageSharedPreference.getPreference("address",getApplicationContext()).equals("")){
                intent = new Intent(getApplicationContext(), AddressActivity.class);
                startActivity(intent);
                finish();
            } else{
//                설정된 주소가 있다면.....
                ManagePublicData.getInstance(SplashActivity.this).parsePublicData.execute();
            }
        }else{
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

//        Handler handler =  new Handler();
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                Intent intent;
//
//                intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        },2000);
//        //2초 후 화면전환


    }
}
