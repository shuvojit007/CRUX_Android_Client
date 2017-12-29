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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.shuvojit.crux.R;
import com.shuvojit.crux.model.Update_Image_model;
import com.shuvojit.crux.model.user_profile_model_1;
import com.shuvojit.crux.service.Authentication;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
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

import static android.app.Activity.RESULT_OK;

/**
 * Created by SHOBOJIT on 12/28/2017.
 */

public class Profile_Frag extends Fragment {
    View v;
    SharedPreferences sp;
    String token;
    ProgressDialog mDialog;
    Button pro_btn_upPic;
    CircleImageView propic;
    TextView proname, propost, procmnts, proemail, prophn;
    String url = "https://young-peak-53218.herokuapp.com/user/";
    private StorageReference mStorageRef;
    private StorageTask mStorageTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profile_frag, container, false);
        intit(v);

        if (!token.equals("no data")) {
            mDialog.setTitle("Fetching User Data");
            mDialog.setMessage("Please wait while we fetch your data from server");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            GetUserData(token);
        } else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        pro_btn_upPic.setOnClickListener((View) -> uploadImage());
        return v;
    }

    private void intit(View v) {
        propic = v.findViewById(R.id.pro_pic);
        proname = v.findViewById(R.id.pro_name);
        propost = v.findViewById(R.id.pro_post_count);
        procmnts = v.findViewById(R.id.pro_comments_count);
        proemail = v.findViewById(R.id.pro_email);
        prophn = v.findViewById(R.id.pro_phn);
        pro_btn_upPic = v.findViewById(R.id.pro_update_pic);
        mDialog = new ProgressDialog(getContext());
        sp = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sp.getString("token", "no data");
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }
    private void GetUserData(String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request req = chain.request();
                Request.Builder newreq = req.newBuilder().header("auth", token);
                return chain.proceed(newreq.build());
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Authentication authservice = retrofit.create(Authentication.class);
        Call<user_profile_model_1> call = authservice.GetUserData();
        call.enqueue(new Callback<user_profile_model_1>() {
            @Override
            public void onResponse(Call<user_profile_model_1> call, retrofit2.Response<user_profile_model_1> response) {
                if (response.isSuccessful()) {
                    mDialog.dismiss();
                    if (response.body().getMethod().equals("local")) {
                        Setup(response.body().getFirstName(),
                                response.body().getLastName(),
                                response.body().getPhnNumber().toString(),
                                response.body().getPost().size(),
                                response.body().getComments().size(),
                                response.body().getImage(),
                                response.body().getLocal().getEmail());
                    } else {
                        Setup(response.body().getFirstName(),
                                response.body().getLastName(),
                                response.body().getPhnNumber().toString(),
                                response.body().getPost().size(),
                                response.body().getComments().size(),
                                response.body().getImage(),
                                null);
                    }
                } else {
                    mDialog.dismiss();
                    Toast.makeText(getContext(), "Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<user_profile_model_1> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(getContext(), "Wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void Setup(String firstName, String lastName, String phn, int post, int cmnt, String img, String email) {
        proname.setText(firstName + " " + lastName);
        prophn.setText(phn);
        propost.setText(String.valueOf(post));
        procmnts.setText(String.valueOf(cmnt));
        proemail.setText(email);
        if (!img.equals("default")) {
            Picasso.with(getContext()).load(img).placeholder(R.drawable.ph2).into(propic);
        }


    }

    private void uploadImage() {
        Intent in = new Intent(Intent.ACTION_GET_CONTENT);
        in.setType("*/*");
        in.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(in, "Selcet a img file to upload"), 1);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            UpToFirebas(data.getData());
        }
    }

    private void UpToFirebas(Uri res) {
        String uriString = res.toString();

        File myFile = new File(uriString);
        String path = myFile.getAbsolutePath();
        String displayName = null;
        if (uriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getActivity()
                        .getContentResolver()
                        .query(res, null, null, null, null);
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


        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        final View dialogView = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.user_update_pic, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        final TextView fileName = dialogView.findViewById(R.id.pro_upimg_dlg_name);
        final TextView fileSize = dialogView.findViewById(R.id.pro_upimag_dlg_size);
        final TextView fileSizePercent = dialogView.findViewById(R.id.pro_upimag_dlg_size_percent);
        final Button  cancel= dialogView.findViewById(R.id.pro_upimag_dlg_btn_cancel);
        final Button  pause= dialogView.findViewById(R.id.pro_upimag_dlg_btn_pause);
        final ProgressBar progressBar = dialogView.findViewById(R.id.pro_upimg_dlg_progress);
        fileName.setText(displayName);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        pause.setOnClickListener((View)->{
            String btnText = pause.getText().toString();
            if(btnText.equals("Pause Upload")){
                mStorageTask.pause();
                pause.setText("Resume Upload");
            }else {
                mStorageTask.resume();
                pause.setText("Pause Upload");

            }
        });
        cancel.setOnClickListener((View)->{
         mStorageTask.cancel();
         dialog.dismiss();
        });


        StorageReference Ref = mStorageRef.child("propic/" + displayName);
        mStorageTask =Ref.putFile(res).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                // Get a URL to the uploaded content
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                AddImageToTheServer(downloadUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                fileSize.setText( taskSnapshot.getBytesTransferred()/(1024*1024)+"/"+ taskSnapshot.getTotalByteCount()/(1024*1024)+"mb");
                fileSizePercent.setText(String.valueOf((int)progress)+"%");
            }
        });
    }

    private void AddImageToTheServer(Uri downloadUrl) {
        mDialog.setTitle("Fetching User Data");
        mDialog.setMessage("Please wait while we upadate your data to server");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

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
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Authentication authservice = retrofit.create(Authentication.class);
        Call<Update_Image_model> call = authservice.UpdateImage(new Update_Image_model(downloadUrl.toString()));
        call.enqueue(new Callback<Update_Image_model>() {
            @Override
            public void onResponse(Call<Update_Image_model> call, retrofit2.Response<Update_Image_model> response) {
                mDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.code()==200){
                        Picasso.with(getContext()).load(downloadUrl.toString()).placeholder(R.drawable.ph2).into(propic);
                    }
                }
            }

            @Override
            public void onFailure(Call<Update_Image_model> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(getContext(), "Update Your Profile Pic", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
