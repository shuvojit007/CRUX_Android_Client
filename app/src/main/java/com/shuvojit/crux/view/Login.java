package com.shuvojit.crux.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.shuvojit.crux.R;
import com.shuvojit.crux.model.login_model;
import com.shuvojit.crux.service.Authentication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    Toolbar tl;
    TextInputLayout mEmail, mPass;
    SharedPreferences sp;
    private static String token = "TOKEN";
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        //set up the toolbar
        setSupportActionBar(tl);
        getSupportActionBar().setTitle("Sign In");

        findViewById(R.id.login_btn).setOnClickListener((View) -> {
            String email = mEmail.getEditText().getText().toString();
            String password = mPass.getEditText().getText().toString();
            if (email.equals("") || password.equals("")) {
                Toast.makeText(this, "Please enter a valid data ", Toast.LENGTH_SHORT).show();
            } else {
                mDialog.setTitle("Logging in");
                mDialog.setMessage("Please wait while we check your credentials");
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();
                GetTokenFromServer(email, password);
            }
        });


    }

    private void GetTokenFromServer(String email, String password) {
        Authentication service = Authentication.retrofit.create(Authentication .class);
        Call<login_model> call = service.lgoin_user(new login_model(email, password));
        call.enqueue(new Callback<login_model>() {
            @Override
            public void onResponse(Call<login_model> call, Response<login_model> response) {
                if (response.code() == 200) {
                    mDialog.dismiss();
                    //Store the token on Shared Pref
                    sp.edit().putString("token", response.body().getToken()).apply();
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                } else if (response.code() == 401) {
                    mDialog.dismiss();
                    Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                } else {
                    mDialog.dismiss();
                    Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<login_model> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        mDialog = new ProgressDialog(this);
        tl = findViewById(R.id.login_toolbar);
        mEmail = findViewById(R.id.login_email);
        mPass = findViewById(R.id.login_password);
        sp = getSharedPreferences(token, MODE_PRIVATE);
    }
}
