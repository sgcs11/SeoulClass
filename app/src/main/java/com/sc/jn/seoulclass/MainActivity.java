package com.sc.jn.seoulclass;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.jn.seoulclass.Util.LOGOUTTASK;
import com.sc.jn.seoulclass.Util.ManagePublicData;
import com.sc.jn.seoulclass.Util.ManageSharedPreference;
import com.sc.jn.seoulclass.Util.PermissionUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private String[] permissions = {Manifest.permission.INTERNET};
    private WebView webView;
    private TextView txtToolbar;
    private TextView[] txtView;

    private TextView WhoLogin;
    private Button Login_btn;
    public static String user_id="";
    public static String login_route="";
    public static String nickname="";

    public static String m_cookies="";
    public static boolean m_session=false;
    public static boolean isLoggedIn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//       카테고리 텍스트
        txtView = new TextView[12];
        txtView[1]= (TextView)findViewById(R.id.main_txt_1);
        txtView[2]= (TextView)findViewById(R.id.main_txt_2);
        txtView[3]= (TextView)findViewById(R.id.main_txt_3);
        txtView[4]= (TextView)findViewById(R.id.main_txt_4);
        txtView[5]= (TextView)findViewById(R.id.main_txt_5);
        txtView[6]= (TextView)findViewById(R.id.main_txt_6);
        txtView[7]= (TextView)findViewById(R.id.main_txt_7);
        txtView[8]= (TextView)findViewById(R.id.main_txt_8);
        txtView[9]= (TextView)findViewById(R.id.main_txt_9);
        txtView[10]= (TextView)findViewById(R.id.main_txt_10);
        txtView[11]= (TextView)findViewById(R.id.main_txt_11);


        txtToolbar = (TextView)findViewById(R.id.txt_toolbar);
        txtToolbar.setText(ManageSharedPreference.getPreference("address",getApplicationContext()));

        webView = (WebView) findViewById(R.id.webView_main);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAppCacheEnabled(false);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });



        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        HeaderView Click Event
        View headerView = navigationView.getHeaderView(0);
        LinearLayout nav_dibs = (LinearLayout)headerView.findViewById(R.id.nav_dibs);//찜
        LinearLayout nav_review = (LinearLayout)headerView.findViewById(R.id.nav_review);//리뷰관리
        Login_btn =(Button)headerView.findViewById(R.id.login_btn);
        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggedIn==true){//로그인 되 있으면
                    new LOGOUTTASK().execute("https://seoulclass.ml/logout");
                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    m_session = false;
                    isLoggedIn=false;
                    user_id=""; nickname=""; login_route="";
                    WhoLogin.setText("");
                    Login_btn.setText("로그인");
                }
                else{//로그인 안 되있으면
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }
            }
        });


        nav_dibs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DibsActivity.class);
                startActivity(intent);

            }
        });
        nav_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                startActivity(intent);

            }
        });



        //    Click Event
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddressActivity.class);
                startActivity(intent);



            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        txtToolbar.setText(ManageSharedPreference.getPreference("address",getApplicationContext()));
        ManagePublicData managePublicData = ManagePublicData.getInstance(MainActivity.this);
        txtView[1].setText(getString(R.string.main_1,managePublicData.getCategory1ArrayList().size()));
        txtView[2].setText(getString(R.string.main_2,managePublicData.getCategory2ArrayList().size()));
        txtView[3].setText(getString(R.string.main_3,managePublicData.getCategory3ArrayList().size()));
        txtView[4].setText(getString(R.string.main_4,managePublicData.getCategory4ArrayList().size()));
        txtView[5].setText(getString(R.string.main_5,managePublicData.getCategory5ArrayList().size()));
        txtView[6].setText(getString(R.string.main_6,managePublicData.getCategory6ArrayList().size()));
        txtView[7].setText(getString(R.string.main_7,managePublicData.getCategory7ArrayList().size()));
        txtView[8].setText(getString(R.string.main_8,managePublicData.getCategory8ArrayList().size()));
        txtView[9].setText(getString(R.string.main_9,managePublicData.getCategory9ArrayList().size()));
        txtView[10].setText(getString(R.string.main_10,managePublicData.getCategory10ArrayList().size()));
        txtView[11].setText(getString(R.string.main_11,managePublicData.getCategory11ArrayList().size()));


    }

    @Override
    public void onStart() {
        super.onStart();

        if (!PermissionUtil.checkPermissions(this, Manifest.permission.INTERNET)) {
            PermissionUtil.requestPermissions(this,permissions,1);
        } else {
            webView.loadUrl("http://www.seoul.go.kr/main/index.html");
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    public void CategoryClickHandler(View view){
        Intent intent = new Intent(getApplicationContext(), ClassListActivity.class);
        switch(view.getId()){
            case  R.id.main_1 : {
                intent.putExtra("nm", 1);
                startActivity(intent);
                break;
            }
            case  R.id.main_2 : {
                intent.putExtra("nm", 2);
                startActivity(intent);
                break;
            }
            case  R.id.main_3 : {
                intent.putExtra("nm", 3);
                startActivity(intent);
                break;
            }
            case  R.id.main_4 : {
                intent.putExtra("nm", 4);
                startActivity(intent);
                break;
            }
            case  R.id.main_5 : {
                intent.putExtra("nm", 5);
                startActivity(intent);
                break;
            }
            case  R.id.main_6 : {
                intent.putExtra("nm", 6);
                startActivity(intent);
                break;
            }
            case  R.id.main_7 : {
                intent.putExtra("nm", 7);
                startActivity(intent);
                break;
            }
            case  R.id.main_8 : {
                intent.putExtra("nm", 8);
                startActivity(intent);
                break;
            }

            case R.id.main_9 : {
                intent.putExtra("nm",9);
                startActivity(intent);
                break;
            }
            case R.id.main_10 : {
                intent.putExtra("nm",10);
                startActivity(intent);
                break;
            }
            case R.id.main_11 : {
                intent.putExtra("nm",11);
                startActivity(intent);
                break;
            }
        }

    }
    private boolean isExistAddress(){
        if(ManageSharedPreference.getPreference("address",getApplicationContext()).equals("")){
            return true;
        }
        return false;
    }

    public Point getScreenSize(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public int getToolBarHeight() {
        int[] attrs = new int[] {R.attr.actionBarSize};
        TypedArray ta = getApplicationContext().obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return toolBarHeight;
    }



}
