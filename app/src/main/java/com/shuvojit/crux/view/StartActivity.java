package com.shuvojit.crux.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shuvojit.crux.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.start_login_btn)
                .setOnClickListener((View)->
                        startActivity(new Intent(this,Login.class)));


        findViewById(R.id.start_reg_btn)
                .setOnClickListener((View)->
                        startActivity(new Intent(this,Register.class)));
    }
}
