package com.shuvojit.crux.service;

import com.shuvojit.crux.model.login_model;
import com.shuvojit.crux.model.register_model;
import com.shuvojit.crux.model.user_profile_model_1;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by SHOBOJIT on 12/28/2017.
 */

public interface Authentication {

    @POST("signin")
    Call<login_model> lgoin_user (@Body login_model model);

    @POST("signup")
    Call<register_model> register_user (@Body register_model model);

    @GET("user")
    Call<user_profile_model_1>GetUserData();


    String url = "https://young-peak-53218.herokuapp.com/user/";
    public static Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(url)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
