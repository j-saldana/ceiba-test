package co.com.ceiba.mobile.pruebadeingreso.rest;

import java.util.List;
import java.util.Map;

import co.com.ceiba.mobile.pruebadeingreso.model.Post;
import co.com.ceiba.mobile.pruebadeingreso.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import static co.com.ceiba.mobile.pruebadeingreso.rest.Endpoints.GET_POST_USER;
import static co.com.ceiba.mobile.pruebadeingreso.rest.Endpoints.GET_USERS;

public interface Api {

    @GET(GET_USERS)
    Call<List<User>> getUsers();

    @GET(GET_POST_USER)
    Call<List<Post>> getPosts(@QueryMap Map<String, String> params);

}
