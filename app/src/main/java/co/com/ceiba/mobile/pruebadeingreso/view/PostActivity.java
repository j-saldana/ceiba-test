package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.model.Post;
import co.com.ceiba.mobile.pruebadeingreso.rest.Api;
import co.com.ceiba.mobile.pruebadeingreso.rest.RetrofitClient;
import co.com.ceiba.mobile.pruebadeingreso.view.adapters.posts.PostsAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends Activity {
    private Api jsonApi;
    private RecyclerView recycler;
    private PostsAdapter postsAdapter;
    private List<Post> posts;

    private TextView userName, userPhone,userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        //Init Api Interface
        jsonApi = RetrofitClient.getRetrofitInstance().create(Api.class);

        //Init recycler
        recycler = findViewById(R.id.recyclerViewPostsResults);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        //Init views of user detail
        userName = findViewById(R.id.name);
        userPhone = findViewById(R.id.phone);
        userEmail = findViewById(R.id.email);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            userName.setText(extras.getString("name"));
            userPhone.setText(extras.getString("phone"));
            userEmail.setText(extras.getString("email"));
            getPosts(String.valueOf(extras.getInt("id")));
        }
    }

    private void getPosts(String userId){
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        Call<List<Post>> postsCall = jsonApi.getPosts(params);

        postsCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    Log.d("ERROR ", "onResponse: "+response.code());
                }else {
                    posts = response.body();
                    Log.d("CORRECT ", "onResponse: "+ response.code());
                    postsAdapter = new PostsAdapter(PostActivity.this, posts);
                    recycler.setAdapter(postsAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("FAILURE", "onFailure: "+t.getMessage());
            }
        });
    }

}
