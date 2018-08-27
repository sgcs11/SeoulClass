package com.sc.jn.seoulclass;

import android.app.Activity;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.sc.jn.seoulclass.Util.ManagePublicData;
import com.sc.jn.seoulclass.Util.ManageSharedPreference;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private Button[] mButton = new Button[26];
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);


        mButton[0] = (Button)findViewById(R.id.pa_addr_0);
        mButton[1] = (Button)findViewById(R.id.pa_addr_1);
        mButton[2] = (Button)findViewById(R.id.pa_addr_2);
        mButton[3] = (Button)findViewById(R.id.pa_addr_3);
        mButton[4] = (Button)findViewById(R.id.pa_addr_4);
        mButton[5] = (Button)findViewById(R.id.pa_addr_5);
        mButton[6] = (Button)findViewById(R.id.pa_addr_6);
        mButton[7] = (Button)findViewById(R.id.pa_addr_7);
        mButton[8] = (Button)findViewById(R.id.pa_addr_8);
        mButton[9] = (Button)findViewById(R.id.pa_addr_9);
        mButton[10] = (Button)findViewById(R.id.pa_addr_10);
        mButton[11] = (Button)findViewById(R.id.pa_addr_11);
        mButton[12] = (Button)findViewById(R.id.pa_addr_12);
        mButton[13] = (Button)findViewById(R.id.pa_addr_13);
        mButton[14] = (Button)findViewById(R.id.pa_addr_14);
        mButton[15] = (Button)findViewById(R.id.pa_addr_15);
        mButton[16] = (Button)findViewById(R.id.pa_addr_16);
        mButton[17] = (Button)findViewById(R.id.pa_addr_17);
        mButton[18] = (Button)findViewById(R.id.pa_addr_18);
        mButton[19] = (Button)findViewById(R.id.pa_addr_19);
        mButton[20] = (Button)findViewById(R.id.pa_addr_20);
        mButton[21] = (Button)findViewById(R.id.pa_addr_21);
        mButton[22] = (Button)findViewById(R.id.pa_addr_22);
        mButton[23] = (Button)findViewById(R.id.pa_addr_23);
        mButton[24] = (Button)findViewById(R.id.pa_addr_24);
        mButton[25] = (Button)findViewById(R.id.pa_addr_25);

        for(int i=0; i < mButton.length ; i++){
            mButton[i].setTag(i);
            mButton[i].setOnClickListener(this);
            mButton[i].setOnFocusChangeListener(this);
        }

    }



    @Override
    public void onClick(View v) {
        Button newButton = (Button)v;

    }

    public void submit(View v){
// 기존에 설정된 주소와 같으면 Just finish();
        if(value.equals(ManageSharedPreference.getPreference("address",getApplicationContext()))){
            finish();
        }
        else {
            ManageSharedPreference.insertPreference("address", value, getApplicationContext());
            this.setVisible(false);
            ManagePublicData.reFreshManagePublicData();
            ManagePublicData.getInstance(AddressActivity.this).parsePublicData.execute();
        }

    }

    public void cancel(View v){
        if(ManageSharedPreference.getPreference("address",getApplicationContext())==""){
            Toast.makeText(this,"설정된 지역이 없습니다.", Toast.LENGTH_SHORT).show();
        }else{
            finish();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            value = ((Button)v).getText().toString();
        }
    }
}
