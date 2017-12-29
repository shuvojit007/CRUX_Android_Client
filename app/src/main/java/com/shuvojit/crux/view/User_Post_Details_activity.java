package com.shuvojit.crux.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuvojit.crux.R;
import com.shuvojit.crux.model.rec_model.user_post_model;
import com.squareup.picasso.Picasso;

public class User_Post_Details_activity extends AppCompatActivity {
    TextView title, des;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__post__details_activity);
        user_post_model model = getIntent().getParcelableExtra("user_object");
        Toast.makeText(this, model.getTitle(), Toast.LENGTH_SHORT).show();
        init(model.getTitle(),model.getDescription(),model.getImage());
    }

    private void init(String titlestring, String description, String image) {
        title=findViewById(R.id.user_post_details_title);
        des =findViewById(R.id.user_post_details_des);
        img =findViewById(R.id.user_post_details_img);

        title.setText(titlestring);
        des.setText(description);
        Picasso.with(getApplicationContext()).load(image).into(img);
    }
}
