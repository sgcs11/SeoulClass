package com.sc.jn.seoulclass.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.jn.seoulclass.R;

import java.util.ArrayList;

public class InfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private TextView[] contents=null;
    private static InfoFragment infoFragment=null;

    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        infoFragment=fragment;
        return fragment;
    }

    public static InfoFragment getInfoFragment() {
        return infoFragment;
    }



    public static InfoFragment getInstance(){
        if(infoFragment == null){
            infoFragment = new InfoFragment();
            Bundle args = new Bundle();
            infoFragment.setArguments(args);
        }
        return infoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contents= new TextView[11];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_info, container, false);

        Log.d("jinho","OncreateView");
        contents[0] = (TextView)v.findViewById(R.id.dt_txt_for);
        contents[1] = (TextView)v.findViewById(R.id.dt_txt_period);
        contents[2] = (TextView)v.findViewById(R.id.dt_txt_place);
        contents[3] = (TextView)v.findViewById(R.id.dt_txt_pay);
        contents[4] = (TextView)v.findViewById(R.id.dt_txt_to);
        contents[5] = (TextView)v.findViewById(R.id.dt_txt_restriction);
        contents[6] = (TextView)v.findViewById(R.id.dt_txt_how);
        contents[7] = (TextView)v.findViewById(R.id.dt_txt_pperiod);
        contents[8] = (TextView)v.findViewById(R.id.dt_txt_cperiod);
        contents[9] = (TextView)v.findViewById(R.id.dt_txt_tel);
        contents[10] = (TextView)v.findViewById(R.id.dt_txt_select);

        return v;
    }

    public void changeFragmentTextView(ArrayList<String> str){
        for(int i=0 ; i<str.size();i++){
            contents[i].setText(str.get(i));
        }
    }

}