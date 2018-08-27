package com.sc.jn.seoulclass.Util;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.sc.jn.seoulclass.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class DetailParser extends AsyncTask<String, Void, ArrayList<String>>{
    Activity context;
    Elements contents;
    Element pImg;
    ImageView img;
    TextView[] detail;


    public DetailParser(Activity context) {
        this.context = context;
        detail = new TextView[11];
        img = (ImageView)context.findViewById(R.id.dt_img);
//        detail[0] = (TextView)context.findViewById(R.id.dt_txt_for);
//        detail[1] = (TextView)context.findViewById(R.id.dt_txt_period);
//        detail[2] = (TextView)context.findViewById(R.id.dt_txt_place);
//        detail[3] = (TextView)context.findViewById(R.id.dt_txt_pay);
//        detail[4] = (TextView)context.findViewById(R.id.dt_txt_to);
//        detail[5] = (TextView)context.findViewById(R.id.dt_txt_restriction);
//        detail[6] = (TextView)context.findViewById(R.id.dt_txt_how);
//        detail[7] = (TextView)context.findViewById(R.id.dt_txt_pperiod);
//        detail[8] = (TextView)context.findViewById(R.id.dt_txt_cperiod);
//        detail[9] = (TextView)context.findViewById(R.id.dt_txt_tel);
//        detail[10] = (TextView)context.findViewById(R.id.dt_txt_select);

    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        String url = "https://yeyak.seoul.go.kr/reservation/view.web?rsvsvcid=S180823155530015071"+strings[0];
        ArrayList<String> str= new ArrayList<String>();
        try {
            Document doc = Jsoup.connect(url).get();
            contents = doc.select(".productInfo td");
            pImg = doc.selectFirst(".imgBox img");


            int i=0;
            for(Element content : contents){
                str.add(content.text());
                i++;
                if(i==11)
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return str;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {
//        Uri uri = Uri.parse(pImg.attr("src"));
        Uri uri = Uri.parse("https://yeyak.seoul.go.kr/fileDownload.web?p=/TB_SVCIMG/2018/08/23/S180823155530015071&m=i&n=azZ68kpVy1OwWd2oZyQE49i9EQ1A4p_400058&on=%EC%9E%A0%EC%8B%A4%EC%A2%85%ED%95%A9%EC%9A%B4%EB%8F%99%EC%9E%A5%20%EB%82%B4%20%EC%96%B4%EB%A6%B0%EC" +
                "%9D%B4%20%EC%A0%84%EC%9A%A9%20%EC%97%B0%EC%8B%9D%EC%95%BC%EA%B5%AC%EC%9E%A5.jpg");
        img.setImageURI(uri);

        for(int i=0 ; i<detail.length;i++){
            detail[i].setText(s.get(i));
        }
        super.onPostExecute(s);
    }
}
