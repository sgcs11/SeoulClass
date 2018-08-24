package com.sc.jn.seoulclass;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.jn.seoulclass.Model.User;
import com.sc.jn.seoulclass.Util.LOGOUTTASK;
import com.sc.jn.seoulclass.Util.PermissionUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String[] permissions = {Manifest.permission.INTERNET};
    private WebView webView;
    private TextView txtToolbar;

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

        txtToolbar = (TextView) findViewById(R.id.txt_toolbar);
        txtToolbar.setText(User.address);

        webView = (WebView) findViewById(R.id.webView_main);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAppCacheEnabled(false);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView=navigationView.getHeaderView(0);
        WhoLogin = (TextView)headerView.findViewById(R.id.WhoLogin);

        //    Click Event
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FindAdressActivity.class));
            }
        });

        // Login event
        View headerview = navigationView.getHeaderView(0);
        Login_btn =(Button)headerview.findViewById(R.id.login_btn);
        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggedIn==true){//로그인 되 있으면
                    new LOGOUTTASK().execute("https://seoulclass.ml/logout");
                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    m_session = false;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        txtToolbar.setText(User.address);
        if(nickname.length()==0)
            WhoLogin.setText(user_id);
        else
            WhoLogin.setText(nickname);
        if(isLoggedIn)
            Login_btn.setText("로그아웃");
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!PermissionUtil.checkPermissions(this, Manifest.permission.INTERNET)) {
            PermissionUtil.requestPermissions(this, permissions, 1);
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

    public void CategoryClickHandler(View view) {
        Intent intent = new Intent(getApplicationContext(), ClassListActivity.class);
        switch (view.getId()) {
            case R.id.main_1: {
                intent.putExtra("nm", getString(R.string.main_1));
                startActivity(intent);
            }
            break;
            case R.id.main_6: {
                intent.putExtra("nm", getString(R.string.main_6));
                startActivity(intent);
            }
            break;


        }

    }

}