package com.shuvojit.crux.model;

/**
 * Created by SHOBOJIT on 12/28/2017.
 */

public class login_model {

    private String token;
    private String email;
    private  String password;

    public login_model(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getToken() {
        return token;
    }
}
