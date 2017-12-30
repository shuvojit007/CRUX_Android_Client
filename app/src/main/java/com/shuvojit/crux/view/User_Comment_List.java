package com.shuvojit.crux.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shuvojit.crux.R;
import com.shuvojit.crux.adapter.ItemDecoration;
import com.shuvojit.crux.model.rec_model.Post_Comment_model.Add_Comment_model;
import com.shuvojit.crux.model.rec_model.Post_Comment_model.Post_Comment_model;
import com.shuvojit.crux.service.Api;
import com.shuvojit.crux.view.Fragment.All_User_Post_Frag;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class User_Comment_List extends AppCompatActivity {
    String postId;
    FloatingActionButton mAddCmnt;
    String token;
    List<Post_Comment_model>list;
    private RecyclerView recyclerView;
    ProgressDialog mDialog;
    String url = "https://young-peak-53218.herokuapp.com/";
    SharedPreferences sp;
    RecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__comment__list);
        postId = getIntent().getStringExtra("id");
        init();

        if (!token.equals("no data")) {
            mDialog.setTitle("Fetching User Comments");
            mDialog.setMessage("Please wait while we fetch comments from server");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            GetAllUserPostFrmServer(token);
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        
        findViewById(R.id.user_cmnt_add).setOnClickListener(v->AddCmntDlg());
    }

    private void AddCmntDlg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = this.getLayoutInflater().inflate(R.layout.add_coment_dialog,null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        final TextInputLayout cmntEdit = (TextInputLayout) dialogView.findViewById(R.id.cmnt_edittext);
        final Button cmntGo = (Button) dialogView.findViewById(R.id.cmnt_GO);
        cmntGo.setOnClickListener(v->{
            final String cmntStr=cmntEdit.getEditText().getText().toString();

            if (cmntStr.equals("")){
                Toast.makeText(v.getContext(), "Please fill the form", Toast.LENGTH_SHORT).show();
            }else {
                AddCmnt(cmntStr ,dialog);
            }
        });
        dialog.show();

    }

    private void AddCmnt(String cmntStr, AlertDialog dialog) {
        dialog.dismiss();
        OkHttpClient.Builder retobuilder = new OkHttpClient.Builder();
        retobuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request req = chain.request();
                Request.Builder newReq = req.newBuilder().header("auth",token);
                return  chain.proceed(newReq.build());
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(retobuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api authservice = retrofit.create(Api.class);
        Call<Add_Comment_model>call = authservice.Add_Comment(postId,new Add_Comment_model(cmntStr));
        call.enqueue(new Callback<Add_Comment_model>() {
            @Override
            public void onResponse(Call<Add_Comment_model> call, retrofit2.Response<Add_Comment_model> response) {
                if(response.isSuccessful()){
                    if(response.code()==200){
                        list.clear();
                        mDialog.setTitle("Get All Cmnt frm database");
                        mDialog.setMessage("Please wait while we retrive cmnt frm server !");
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                        GetAllUserPostFrmServer(token);
                    }
                }else {
                    Toast.makeText(User_Comment_List.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Add_Comment_model> call, Throwable t) {
                Toast.makeText(User_Comment_List.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
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
        Call<List<Post_Comment_model>> call = authservice.GetUserComment(postId);
        call.enqueue(new Callback<List<Post_Comment_model>>() {
            @Override
            public void onResponse(Call<List<Post_Comment_model>> call, retrofit2.Response<List<Post_Comment_model>> response) {
                mDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.code()==200){
                        list = response.body();
                        adapter= new RecyclerAdapter(list);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(User_Comment_List.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post_Comment_model>> call, Throwable t) {
               mDialog.dismiss();
                Toast.makeText(User_Comment_List.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        mDialog = new ProgressDialog(this);
        sp = this.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sp.getString("token", "no data");
        recyclerView =findViewById(R.id.user_cmnt_rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemDecoration(20,10,20,0));
    }


    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecHolder> {
        List<Post_Comment_model> result;
        public RecyclerAdapter(List<Post_Comment_model> list) {
            this.result = list;
        }

        @Override
        public RecyclerAdapter.RecHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.user_comment_rec_layout, parent, false);
            v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerAdapter.RecHolder holder, int position) {
            holder.bindData(result.get(position));
        }

        @Override
        public int getItemCount() {
            return result.size();
        }

        public class RecHolder extends RecyclerView.ViewHolder {
            private TextView cmnt,name,date;
            public RecHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.cmnt_name);
                date = itemView.findViewById(R.id.cmnt_date);
                cmnt = itemView.findViewById(R.id.cmnt);
            }

            public void bindData(Post_Comment_model model) {
                name.setText(model.getUser().getFirstName()+ " "+model.getUser().getLastName());
                date.setText(convertMongoDate(model.getDate()));
                cmnt.setText(model.getComment());
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
