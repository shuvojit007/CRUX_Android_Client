package com.shuvojit.crux.view.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shuvojit.crux.R;
import com.shuvojit.crux.adapter.ItemDecoration;
import com.shuvojit.crux.model.rec_model.All_User_Post_Model;
import com.shuvojit.crux.model.rec_model.user_post_model;
import com.shuvojit.crux.service.Api;
import com.shuvojit.crux.view.All_User_Post;
import com.shuvojit.crux.view.User_Post_Details_activity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SHOBOJIT on 12/28/2017.
 */

public class All_User_Post_Frag extends Fragment {
    View v;
    private RecyclerView recyclerView;
    SharedPreferences sp;
    String token;
    List<All_User_Post_Model> list;
    ProgressDialog mDialog;
    String url = "https://young-peak-53218.herokuapp.com/";
    RecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.all_user_post_frag, container, false);
        init(v);
        if (!token.equals("no data")) {
            mDialog.setTitle("Fetching User Data");
            mDialog.setMessage("Please wait while we fetch your data from server");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            GetAllUserPostFrmServer(token);
        } else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        
        return v;
    }

    private void GetAllUserPostFrmServer(String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request req = chain.request();
                Request.Builder newReq = req.newBuilder().header("auth",token);
                return  chain.proceed(newReq.build());
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api authservice = retrofit.create(Api.class);
        Call<List<All_User_Post_Model>>call=authservice.GETAllUserPost();
        call.enqueue(new Callback<List<All_User_Post_Model>>() {
            @Override
            public void onResponse(Call<List<All_User_Post_Model>> call, retrofit2.Response<List<All_User_Post_Model>> response) {
                mDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.code()==200){
                        list = response.body();
                        adapter= new RecyclerAdapter(list);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                   }
                }
            }

            @Override
            public void onFailure(Call<List<All_User_Post_Model>> call, Throwable t) {

            }
        });
    }

    private void init(View v) {
        sp = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sp.getString("token", "no data");
        mDialog = new ProgressDialog(getContext());
        recyclerView = v.findViewById(R.id.all_user_post_rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.addItemDecoration(new ItemDecoration(20,10,20,0));
//        adapter= new RecyclerAdapter(list);
//        recyclerView.setAdapter(adapter);
    }


    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecHolder> {
        List<All_User_Post_Model> result;

        public RecyclerAdapter(List<All_User_Post_Model> result) {
            this.result = result;
        }

        @Override
        public RecyclerAdapter.RecHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.all_user_post_rec_layout, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerAdapter.RecHolder holder, int position) {
            View v = holder.itemView;
            v.setTag(position);
            v.setOnClickListener(view->{
                startActivity(new Intent(getContext(),
                        All_User_Post.class)
                        .putExtra("user_object",result.get(position)));
            });

           holder.bindData(result.get(position));
        }

        @Override
        public int getItemCount() {
            return result.size();
        }

        public class RecHolder extends RecyclerView.ViewHolder {
            private ImageView postimg;
            private TextView title,description,date;
            public RecHolder(View itemView) {
                super(itemView);
                postimg = itemView.findViewById(R.id.all_user_post_rec_img);
                title = itemView.findViewById(R.id.all_user_post_rec_title);
                date = itemView.findViewById(R.id.all_user_post_rec_date);
                description = itemView.findViewById(R.id.all_user_post_rec_details);
            }
            void bindData(All_User_Post_Model model){
                title.setText(model.getTitle());
                description.setText(model.getDescription());
                date.setText(convertMongoDate(model.getDate()));
                Picasso.with(getContext()).load(model.getImage()).into(postimg);
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
        }
    }

}