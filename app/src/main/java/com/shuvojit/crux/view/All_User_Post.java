package com.shuvojit.crux.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuvojit.crux.R;
import com.shuvojit.crux.model.rec_model.All_User_Post_Model;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class All_User_Post extends AppCompatActivity {
    All_User_Post_Model model;
    TextView title,date,name,des;
    ImageView img;
    Button cmnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__user__post);
        model = getIntent().getParcelableExtra("user_object");
        init();
        SetUp(model.getTitle(),
                model.getDate(),
                model.getUser().getFirstName()+" "+model.getUser().getLastName(),
                model.getDescription(),
                model.getImage());
    }

    private void SetUp(String titlestr, String datestr, String namestr, String descriptionstr, String image) {
        title.setText(titlestr);



        //date.setText("Date : "+convertMongoDate(datestr));
        date.setText("Date : "+datestr);
        name.setText("Name : "+namestr);
        des.setText(descriptionstr);
        Picasso.with(getApplicationContext()).load(image).into(img);
    }

    String convertMongoDate(String val){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat= new SimpleDateFormat("yyyy/MM/dd");
        try {
            String finalStr = outputFormat.format(inputFormat.parse(val));
            return finalStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    private void init() {
    title = findViewById(R.id.all_user_post_details_title);
    date = findViewById(R.id.all_user_post_details_date);
    name = findViewById(R.id.all_user_post_details_user_name);
    des = findViewById(R.id.all_user_post_details_des);
    img=findViewById(R.id.all_user_post_details_img);
    cmnt = findViewById(R.id.all_user_post_details_show_cmnt_btn);
    }
}
