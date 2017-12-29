package com.shuvojit.crux.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.shuvojit.crux.R;
import com.shuvojit.crux.adapter.ViewPagerAdapter;
import com.shuvojit.crux.view.Fragment.All_User_Post_Frag;
import com.shuvojit.crux.view.Fragment.Profile_Frag;
import com.shuvojit.crux.view.Fragment.Profile_Post_Frag;

public class MainActivity extends AppCompatActivity {
    private static final  String token = "TOKEN";
    SharedPreferences sp;
    private BottomNavigationView mbottomNavigationView;
    private ViewPager viewPager;
    Toolbar tl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        mbottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                switch (item.getItemId()) {
                    case R.id. promenu_profile:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.promenu_userpost :
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.promenu_post :
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }

    private void init() {
        tl = findViewById(R.id.main_appBar);
        setSupportActionBar(tl);
        getSupportActionBar().setTitle("CRUX");
        tl.setTitleTextColor(0xFFFFFFFF);
        mbottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottom_bar);
        viewPager = findViewById(R.id.main_fragment_container);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Profile_Frag());
        adapter.addFragment(new Profile_Post_Frag());
        adapter.addFragment(new All_User_Post_Frag());
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sp = getSharedPreferences(token , Context.MODE_PRIVATE);
        String token = sp.getString("token","no data");
        if(token.equals("no data")){
            startActivity(new Intent(MainActivity.this,StartActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //==============Toolbar menu item Click Listener===============
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.main_logout_btn) {
            sp  =  getSharedPreferences(token, MODE_PRIVATE);
            sp.edit().remove("token").commit();
            startActivity(new Intent(MainActivity.this,StartActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
