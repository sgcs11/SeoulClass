package com.sc.jn.seoulclass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FindAdressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_adress);

    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fa_img_cancel :
                finish();
        }
    }
}
