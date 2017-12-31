package com.shuvojit.crux.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.shuvojit.crux.R;
import com.shuvojit.crux.model.register_model;
import com.shuvojit.crux.service.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    Toolbar tl;
    SharedPreferences sp;
    private static String token = "TOKEN";
    private int lastLengthOfemail = 0;
    TextInputLayout mFirstName,mLastName,mEmail,mPassword,mPhn;
    ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        setSupportActionBar(tl);
        setSupportActionBar(tl);
        getSupportActionBar().setTitle("Sign Up");
        mEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                autoCompliteEmailForm();
            }
        });

        findViewById(R.id.reg_create_btn).setOnClickListener((View)->{
            String email = mEmail.getEditText().getText().toString();
            String password = mPassword.getEditText().getText().toString();
            String firstname = mFirstName.getEditText().getText().toString();
            String lastname = mLastName.getEditText().getText().toString();
            String phn = mPhn.getEditText().getText().toString();
            if (email.equals("") || password.equals("")||firstname.equals("") || lastname.equals("")|| phn.equals("")) {
                Toast.makeText(this, "Please enter a valid data ", Toast.LENGTH_SHORT).show();
            } else {
                mDialog.setTitle("Registering User");
                mDialog.setMessage("Please wait while we create your account !");
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();
                registerAccount(firstname,lastname,email,password,phn);
            }
        });
    }

    private void registerAccount(String firstname, String lastname, String email, String password,String phn) {
        Api service= Api.retrofit.create(Api.class);
        Call<register_model> call =service.register_user(new register_model(email,password,firstname,lastname,phn));
        call.enqueue(new Callback<register_model>() {
            @Override
            public void onResponse(Call<register_model> call, Response<register_model> response) {
                mDialog.dismiss();
                if (response.code() == 200) {
                    sp.edit().putString("token", response.body().getToken()).apply();
                    startActivity(new Intent(Register.this, MainActivity.class));
                    finish();
                }else if(response.code()==409){
                    Toast.makeText(Register.this, "Email is already used", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<register_model> call, Throwable t) {
                Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        tl =findViewById(R.id.reg_toolbar);
        mFirstName = findViewById(R.id.reg_first_name);
        mLastName = findViewById(R.id.reg_last_name);
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mPhn = findViewById(R.id.reg_phn);
        mDialog =new ProgressDialog(this);
        sp = getSharedPreferences(token, MODE_PRIVATE);
    }


    private void autoCompliteEmailForm() {
        String text =    mEmail.getEditText().getText().toString();
        int length = text.length();
        if (lastLengthOfemail < length) {
            if (length > 1) {
                text = text.substring(length - 2);
                text = text.toLowerCase();
                char[] lastChars = text.toCharArray();
                if (lastChars[0] == '@') {
                    if (lastChars[1] == 'g') {
                        mEmail.getEditText().append("mail.com");
                    } else if (lastChars[1] == 'h') {
                        mEmail.getEditText().append("otmail.com");
                    } else if (lastChars[1] == 'l') {
                        mEmail.getEditText().append("ive.com");
                    } else if (lastChars[1] == 'y') {
                        mEmail.getEditText().append("ahoo.com");
                    }
                }
            }
        }
        lastLengthOfemail = length;
    }
}
