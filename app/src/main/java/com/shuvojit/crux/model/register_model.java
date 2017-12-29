package com.shuvojit.crux.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SHOBOJIT on 12/28/2017.
 */

public class register_model {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phnNumber;
    @SerializedName("token")
    private String token;

    public register_model(String email, String password, String firstName, String lastName, String phnNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phnNumber = phnNumber;
    }

    public String getToken() {
        return token;
    }
}
