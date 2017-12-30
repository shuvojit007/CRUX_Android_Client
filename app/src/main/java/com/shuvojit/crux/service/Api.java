package com.shuvojit.crux.service;

import com.shuvojit.crux.model.Update_Image_model;
import com.shuvojit.crux.model.addPost_model;
import com.shuvojit.crux.model.login_model;
import com.shuvojit.crux.model.rec_model.All_User_Post_Model;
import com.shuvojit.crux.model.rec_model.Update_post_model;
import com.shuvojit.crux.model.rec_model.user_post_model;

import com.shuvojit.crux.model.register_model;
import com.shuvojit.crux.model.user_profile_model_1;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by SHOBOJIT on 12/28/2017.
 */

public interface Api {

    //-------------------Authentication---------------------
    @POST("signin")
    Call<login_model> lgoin_user(@Body login_model model);

    @POST("signup")
    Call<register_model> register_user(@Body register_model model);

    //-------------------User Details----------------------
    @GET("user")
    Call<user_profile_model_1> GetUserData();

    @PUT("user")
    Call<Update_Image_model> UpdateImage(@Body Update_Image_model model);

    @GET("userpost")
    Call<List<user_post_model>> GetUserPost();

    //---------------Post Detail--------------
    @POST("posts")
    Call<addPost_model> PostToServer(@Body addPost_model model);

    @DELETE("posts/{postId}")
    Call<Void> deleteUser(@Path("postId") String postId);

    @PUT("posts/{postId}")
    Call<Update_post_model> UpdatePost(@Path("postId") String postId, @Body Update_post_model model);

    @GET("posts")
    Call<List<All_User_Post_Model>> GETAllUserPost();


    String url = "https://young-peak-53218.herokuapp.com/user/";
    public static Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(url)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
