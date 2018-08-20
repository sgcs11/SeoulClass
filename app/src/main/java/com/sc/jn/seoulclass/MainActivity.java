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
import android.widget.TextView;

import com.sc.jn.seoulclass.Model.User;
import com.sc.jn.seoulclass.Util.ClassListAdapter;
import com.sc.jn.seoulclass.Util.PermissionUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String[] permissions = {Manifest.permission.INTERNET};
    private WebView webView;
    private TextView txtToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtToolbar = (TextView)findViewById(R.id.txt_toolbar);
        txtToolbar.setText(User.address);

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



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //    Click Event
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FindAdressActivity.class));
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        txtToolbar.setText(User.address);
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
                intent.putExtra("nm", getString(R.string.main_1));
                startActivity(intent);
            }
            break;
            case R.id.main_6 : {
                intent.putExtra("nm",getString(R.string.main_6));
                startActivity(intent);
            }
            break;



        }

    }
}
