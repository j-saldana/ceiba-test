package co.com.ceiba.mobile.pruebadeingreso.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.model.User;
import co.com.ceiba.mobile.pruebadeingreso.rest.Api;
import co.com.ceiba.mobile.pruebadeingreso.rest.RetrofitClient;
import co.com.ceiba.mobile.pruebadeingreso.view.adapters.users.UsersAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    private Api jsonApi;
    private RecyclerView recycler;
    private UsersAdapter usersAdapter;
    private List<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Api Interface
        jsonApi = RetrofitClient.getRetrofitInstance().create(Api.class);

        //Init recycler
        recycler = findViewById(R.id.recyclerViewSearchResults);
        recycler.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        getUsers();
    }

    private void getUsers(){
        Call<List<User>> usersCall = jsonApi.getUsers();

        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()){
                    Log.d("ERROR ", "onResponse: "+response.code());
                }else {
                    users = response.body();
                    Log.d("CORRECT ", "onResponse: "+ response.code());

                    //Pass Adapter to Recycler
                    usersAdapter = new UsersAdapter(MainActivity.this, users);
                    recycler.setAdapter(usersAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("FAILURE", "onFailure: "+t.getMessage());
            }
        });
    }


}