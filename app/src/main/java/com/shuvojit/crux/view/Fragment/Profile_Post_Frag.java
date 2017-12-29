package com.shuvojit.crux.view.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shuvojit.crux.R;
import com.shuvojit.crux.adapter.ItemDecoration;
import com.shuvojit.crux.model.Update_Image_model;
import com.shuvojit.crux.model.addPost_model;
import com.shuvojit.crux.model.rec_model.user_post_model;

import com.shuvojit.crux.service.Api;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

/**
 * Created by SHOBOJIT on 12/28/2017.
 */

public class Profile_Post_Frag extends Fragment {
    View v;
    private RecyclerView recyclerView;
    SharedPreferences sp;

    String token;
    private StorageReference mImageStorageReference;
    private static Uri resultUri;
    List<user_post_model> list;
    FloatingActionButton mAddPost;
    private CircleImageView selectedImg;
    ProgressDialog mDialog;
    String url = "https://young-peak-53218.herokuapp.com/posts/";
    RecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profile_post_frag, container, false);
        init(v);
        if (!token.equals("no data")) {
            mDialog.setTitle("Fetching User Data");
            mDialog.setMessage("Please wait while we fetch your data from server");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            GetUserPostFrmServer(token);
        } else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        mAddPost.setOnClickListener((View v)-> AddPost(v));
        return v;
    }

    private void AddPost(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.user_add_post_layout,null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        final TextInputLayout titleEdit = (TextInputLayout) dialogView.findViewById(R.id.user_post_title);
        final TextInputLayout descriptionEdit = (TextInputLayout) dialogView.findViewById(R.id.user_post_description);
        final Button selcetImagebtn = (Button) dialogView.findViewById(R.id.user_add_image);
        Button post = (Button) dialogView.findViewById(R.id.user_post_go_btn);
        selectedImg= (CircleImageView) dialogView.findViewById(R.id.user_post_image);
        dialog.show();

        selcetImagebtn.setOnClickListener((View vv)->{
            Intent in = new Intent(Intent.ACTION_GET_CONTENT);
            in.setType("*/*");
            in.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(Intent.createChooser(in, "Selcet a img file to upload"), 1);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        post.setOnClickListener((View vvv)->{
            final String title =titleEdit.getEditText().getText().toString();
            final String Description = descriptionEdit.getEditText().getText().toString();
            if (title.equals("")|| Description.equals("")){
                Toast.makeText(v.getContext(), "Please fill the form", Toast.LENGTH_SHORT).show();

            }else{
                String uriString =resultUri.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                String displayName = null;
                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getActivity()
                                .getContentResolver()
                                .query(resultUri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    String[] parts = myFile.getName().split("-");
                    displayName =parts[1];
                }


                //Storage Reference for main image
                StorageReference filepath = mImageStorageReference.child("Postpic/"+ UUID.randomUUID().toString() );

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                            final String download_url = task.getResult().getDownloadUrl().toString();
                            AddNewPostToServer(title,Description,download_url);

                            mDialog.setTitle("Registering User");
                            mDialog.setMessage("Please wait while we push your post to server !");
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void AddNewPostToServer(String title, String description, String download_url) {
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
        Call<addPost_model> call = authservice.PostToServer(new addPost_model(title,description,download_url));
        call.enqueue(new Callback<addPost_model>() {
            @Override
            public void onResponse(Call<addPost_model> call, retrofit2.Response<addPost_model> response) {
                if(response.isSuccessful()){
                    if(response.code()==200){
                        mDialog.dismiss();
                        list.clear();
                        GetUserPostFrmServer(token);
                    }
                }else {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<addPost_model> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            resultUri = data.getData();
            selectedImg.setVisibility(View.VISIBLE);
            Picasso.with(v.getContext()).load(resultUri).noPlaceholder().centerCrop().fit()
                    .into(selectedImg);
        }
    }

    private void GetUserPostFrmServer(String token) {
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
        Call<List<user_post_model>> call = authservice.GetUserPost();
        call.enqueue(new Callback<List<user_post_model>>() {
            @Override
            public void onResponse(Call<List<user_post_model>> call, retrofit2.Response<List<user_post_model>> response) {
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
            public void onFailure(Call<List<user_post_model>> call, Throwable t) {
                Toast.makeText(getContext(), "SomeThing Went wrong", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });


    }

    private void init(View v) {
        mImageStorageReference = FirebaseStorage.getInstance().getReference();
        sp = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sp.getString("token", "no data");
        list =new ArrayList<>();
        mDialog = new ProgressDialog(getContext());
        recyclerView = v.findViewById(R.id.user_post_rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.addItemDecoration(new ItemDecoration(0,0,0,10));
        adapter= new RecyclerAdapter(list);
        recyclerView.setAdapter(adapter);
        mAddPost = v.findViewById(R.id.user_add_post);
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecHolder> {
    List<user_post_model> result;

        public RecyclerAdapter(List<user_post_model> result) {
            this.result = result;
        }

        @Override
        public RecHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.user_post_rec_layout, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecHolder(v);
        }

        @Override
        public void onBindViewHolder(RecHolder holder, int position) {
            View v = holder.itemView;
            v.setTag(position);
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
                postimg = itemView.findViewById(R.id.user_post_rec_img);
                title = itemView.findViewById(R.id.user_post_rec_title);
                date = itemView.findViewById(R.id.user_post_rec_date);
                description = itemView.findViewById(R.id.user_post_rec_details);
            }

            void bindData(user_post_model model){
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




























