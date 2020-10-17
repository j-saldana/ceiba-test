package co.com.ceiba.mobile.pruebadeingreso.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.model.Post;
import co.com.ceiba.mobile.pruebadeingreso.rest.Api;
import co.com.ceiba.mobile.pruebadeingreso.rest.RetrofitClient;
import co.com.ceiba.mobile.pruebadeingreso.view.adapters.posts.PostsAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends Activity {
    private SharedPreferences preferences;
    private Gson gson;
    private Api jsonApi;
    private RecyclerView recycler;
    private List<Post> posts;
    private Dialog progress;
    private TextView userName, userPhone,userEmail;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //Init sharedPreferences
        preferences = getSharedPreferences("DATA", MODE_PRIVATE);

        //Init Gson to save objects
        gson = new Gson();


        //Init Api Interface
        jsonApi = RetrofitClient.getRetrofitInstance().create(Api.class);

        //Init recycler
        recycler = findViewById(R.id.recyclerViewPostsResults);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        //Init views of user detail
        userName = findViewById(R.id.name);
        userPhone = findViewById(R.id.phone);
        userEmail = findViewById(R.id.email);

        //Init progress
        progress = new Dialog(this);
        progress.setContentView(R.layout.progress);
        progress.setCancelable(false);
        Objects.requireNonNull(progress.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String id = String.valueOf(extras.getInt("id"));
            userName.setText(extras.getString("name"));
            userPhone.setText(extras.getString("phone"));
            userEmail.setText(extras.getString("email"));

            String json = preferences.getString("posts/"+id, null);
            if (json != null){
                Type type = new TypeToken<List<Post>>(){}.getType();
                posts = gson.fromJson(json, type);
                printRecycler();
            }else {
                progress.show();
                getPosts(id);
            }
        }else {
            Toast.makeText(this,"An error occurred passing activity", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void printRecycler() {
        //Pass Adapter to Recycler
        PostsAdapter postsAdapter = new PostsAdapter(PostActivity.this, posts);
        recycler.setAdapter(postsAdapter);
    }

    private void getPosts(final String userId){
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        final Call<List<Post>> postsCall = jsonApi.getPosts(params);

        postsCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    Log.d("ERROR ON POSTS ", "onResponse: "+response.code());
                }else {
                    posts = response.body();
                    Log.d("CORRECT ON POSTS ", "onResponse: "+ response.code());

                    //Save data on sharedPreferences
                    String json = gson.toJson(posts);
                    preferences.edit().putString("posts/"+userId, json).apply();

                    printRecycler();
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Failure petition on Posts", Toast.LENGTH_LONG).show();
                Log.d("FAILURE", "onFailure: "+t.getMessage());
                progress.dismiss();
            }
        });
    }

}
