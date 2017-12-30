package com.shuvojit.crux.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuvojit.crux.R;
import com.shuvojit.crux.model.rec_model.Update_post_model;
import com.shuvojit.crux.model.rec_model.user_post_model;
import com.shuvojit.crux.service.Api;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class User_Post_Details_activity extends AppCompatActivity {
    TextView title, des;
    ImageView img;
    SharedPreferences sp;
    ProgressDialog mDialog;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__post__details_activity);
        user_post_model model = getIntent().getParcelableExtra("user_object");

        init(model.getTitle(), model.getDescription(), model.getImage());

        findViewById(R.id.user_post_details_update_btn).setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View dialogView = this.getLayoutInflater().inflate(R.layout.activity_user_post_details_update, null);
            builder.setView(dialogView);
            final AlertDialog dialog = builder.create();
            final TextInputLayout titleEdit = (TextInputLayout) dialogView.findViewById(R.id.user_post_title);
            final TextInputLayout descriptionEdit = (TextInputLayout) dialogView.findViewById(R.id.user_post_description);
            final Button selcetImagebtn = (Button) dialogView.findViewById(R.id.user_add_image);

            titleEdit.getEditText().setText(model.getTitle());
            descriptionEdit.getEditText().setText(model.getDescription());


            Button post = (Button) dialogView.findViewById(R.id.user_post_go_btn);
            post.setOnClickListener(v -> {
                final String title = titleEdit.getEditText().getText().toString();
                final String Description = descriptionEdit.getEditText().getText().toString();
                if (title.equals("") || Description.equals("")) {
                    Toast.makeText(v.getContext(), "Please fill the form", Toast.LENGTH_SHORT).show();
                } else {

                    Update_Post(title,Description,model.getId(),dialog);
                }

            });
            dialog.show();

        });
    }

    private void Update_Post(String title, String description, String id, AlertDialog dialog) {
        dialog.dismiss();
//        mDialog.setTitle("Update Post");
//        mDialog.setMessage("Please wait while we update your post to server !");
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.show();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //Ok Http intercepter
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request req = chain.request();
                Request.Builder newreq = req.newBuilder().header("auth", token);
                return chain.proceed(newreq.build());
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://young-peak-53218.herokuapp.com/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api authservice = retrofit.create(Api.class);
        Call<Update_post_model> call = authservice
                .UpdatePost(id,new Update_post_model(title,description));

        call.enqueue(new Callback<Update_post_model>() {
            @Override
            public void onResponse(Call<Update_post_model> call, retrofit2.Response<Update_post_model> response) {
             //  mDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.code()==200){
                        Toast.makeText(User_Post_Details_activity.this, "Post Sucessfully Updated", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(User_Post_Details_activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Update_post_model> call, Throwable t) {
                Toast.makeText(User_Post_Details_activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(String titlestring, String description, String image) {
        title = findViewById(R.id.user_post_details_title);
        des = findViewById(R.id.user_post_details_des);
        img = findViewById(R.id.user_post_details_img);
        title.setText(titlestring);
        des.setText(description);
        mDialog = new ProgressDialog(getApplicationContext());
        Picasso.with(getApplicationContext()).load(image).into(img);
        sp = getApplication().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sp.getString("token", "no data");
    }
}
