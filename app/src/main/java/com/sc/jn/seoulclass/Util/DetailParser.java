package com.sc.jn.seoulclass.Util;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.sc.jn.seoulclass.Fragments.InfoFragment;
import com.sc.jn.seoulclass.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class DetailParser extends AsyncTask<String, Void, Void> {
    private Activity context;
    private Element pImg;
    private Elements contents;

    private WebView webView;
    private TextView[] txt_info;
    private ArrayList<String> str_info;



    public DetailParser(Activity context) {
        this.context = context;
        webView = context.findViewById(R.id.dt_webView);
        txt_info = new TextView[11];
        str_info = new ArrayList<String>();

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View info = layoutInflater.inflate(R.layout.fragment_info,null);




    }

    @Override
    protected Void doInBackground(String... strings) {
        String url = strings[0];
        try {
            Document doc = Jsoup.connect(url).get();
            pImg = doc.selectFirst(".imgBox img");
            contents = doc.select(".productInfo td");

            int i=0;
            for(Element content : contents){
                str_info.add(content.text());
                i++;
                if(i>10)
                    break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void s) {
        String url = "https://yeyak.seoul.go.kr"+pImg.attr("src");
        webView.loadData(getHtmlSource(url), "text/html","UTF-8");
        InfoFragment.getInfoFragment().changeFragmentTextView(str_info);
        super.onPostExecute(s);
    }

    private String getHtmlSource(String url){
        String head = "<head><style>body {padding : 0; margin : 0}</style></head>";
        String body = "<body><img src='"+url+"' width='100%' height='100%' align='middle'/></body>";
        return "<html>"+head+body+"</html>";
    }

}