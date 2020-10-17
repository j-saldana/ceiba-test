package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.model.Posts;
import co.com.ceiba.mobile.pruebadeingreso.model.Users;
import co.com.ceiba.mobile.pruebadeingreso.rest.Api;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {
    private Api jsonApi;
    private List<Users> users;
    private List<Posts> posts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initServices();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getUsers();
        getPosts();
    }

    private void initServices(){
        //Init retrofit builder
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Init interfaceApi
        jsonApi = retrofit.create(Api.class);
    }

    private void getUsers(){
        Call<List<Users>> usersCall = jsonApi.getUsers();

        usersCall.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (!response.isSuccessful()){
                    Log.d("ERROR ", "onResponse: "+response.code());
                }else {
                    users = response.body();
                    Log.d("CORRECT ", "onResponse: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Log.d("FAILURE", "onFailure: "+t.getMessage());
            }
        });
    }

    private void getPosts(){
        Call<List<Posts>> postsCall = jsonApi.getPosts();

        postsCall.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                if (!response.isSuccessful()){
                    Log.d("ERROR ", "onResponse: "+response.code());
                }else {
                    posts = response.body();
                    Log.d("CORRECT ", "onResponse: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                Log.d("FAILURE", "onFailure: "+t.getMessage());
            }
        });
    }
}