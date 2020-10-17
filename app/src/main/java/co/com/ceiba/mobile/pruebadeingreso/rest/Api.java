package co.com.ceiba.mobile.pruebadeingreso.rest;

import java.util.List;

import co.com.ceiba.mobile.pruebadeingreso.model.Posts;
import co.com.ceiba.mobile.pruebadeingreso.model.Users;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("users")
    Call<List<Users>> getUsers();

    @GET("posts")
    Call<List<Posts>> getPosts();

}
