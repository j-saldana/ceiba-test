package co.com.ceiba.mobile.pruebadeingreso.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.model.User;
import co.com.ceiba.mobile.pruebadeingreso.rest.Api;
import co.com.ceiba.mobile.pruebadeingreso.rest.RetrofitClient;
import co.com.ceiba.mobile.pruebadeingreso.view.adapters.users.UsersAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    private SharedPreferences preferences;
    private Gson gson;
    private Api jsonApi;
    private RecyclerView recycler;
    private UsersAdapter usersAdapter;
    private List<User> users;
    private Dialog progress;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init sharedPreferences
        preferences = getSharedPreferences("DATA", MODE_PRIVATE);

        //Init Gson to save objects
        gson = new Gson();

        //Init Api Interface
        jsonApi = RetrofitClient.getRetrofitInstance().create(Api.class);

        //Init recycler
        recycler = findViewById(R.id.recyclerViewSearchResults);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        //Init progress
        progress = new Dialog(this);
        progress.setContentView(R.layout.progress);
        progress.setCancelable(false);
        Objects.requireNonNull(progress.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //Inflate empty view on activity with gravity center and invisible
        RelativeLayout content = findViewById(R.id.content);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(Gravity.CENTER);
        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.empty_view, null);
        view.setVisibility(View.INVISIBLE);
        content.addView(view, params);

        //Init editText and put on text change listener
        EditText editText = findViewById(R.id.editTextSearch);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                usersAdapter.getFilter().filter(charSequence, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                        if (charSequence.toString().length() == 0){
                            view.setVisibility(View.INVISIBLE);
                        }else {
                            if (usersAdapter.getItemCount() > 0){
                                view.setVisibility(View.INVISIBLE);
                            }else {
                                view.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String json = preferences.getString("users", null);
        if (json != null){
            Type type = new TypeToken<List<User>>(){}.getType();
            users = gson.fromJson(json, type);
            printRecycler();
        }else {
            progress.show();
            getUsers();
        }
    }

    private void printRecycler() {
        //Pass Adapter to Recycler
        usersAdapter = new UsersAdapter(MainActivity.this, users);
        recycler.setAdapter(usersAdapter);
    }

    private void getUsers(){
        Call<List<User>> usersCall = jsonApi.getUsers();

        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()){
                    Log.d("ERROR ON USERS ", "onResponse: "+response.code());
                }else {
                    users = response.body();
                    Log.d("CORRECT ON USERS ", "onResponse: "+ response.code());

                    //Save data on sharedPreferences
                    String json = gson.toJson(users);
                    preferences.edit().putString("users", json).apply();

                    printRecycler();
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure petition on Users", Toast.LENGTH_LONG).show();
                Log.d("FAILURE", "onFailure: "+t.getMessage());
                progress.dismiss();
            }
        });
    }
}