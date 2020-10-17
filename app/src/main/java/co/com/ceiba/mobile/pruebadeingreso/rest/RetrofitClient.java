package co.com.ceiba.mobile.pruebadeingreso.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static co.com.ceiba.mobile.pruebadeingreso.rest.Endpoints.URL_BASE;

public class RetrofitClient {
    private static Retrofit retrofit;
     public static Retrofit getRetrofitInstance(){
         //Init retrofit builder
         if (retrofit == null){
             retrofit = new Retrofit.Builder()
                     .baseUrl(URL_BASE)
                     .addConverterFactory(GsonConverterFactory.create())
                     .build();
         }
         return retrofit;
     }
}
