package com.pdfdomain.api;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.pdfdomain.Book;
import com.pdfdomain.DetailsActivity;

public class ApiClient {

    private static final String BASE_URL = "https://pdfdomain.com/api/";

    private static ApiClient instance;
    private ApiInterface apiInterface;

    private ApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }


    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }



    public Call<List<Book>> getBooksJSON(Map map) {
        return apiInterface.getBooksJSON(map);
    }

    public Call<String> getRandomBooks(String pageCount) {
        return apiInterface.getRandomBooks(pageCount);
    }



    public Call<DetailsActivity.ParseData> getDownloadUrlJSON(String md5) {
        return apiInterface.getDownloadUrlJSON(md5);
    }

    public Call<JsonObject> getUpdateJSON(){
        return apiInterface.getUpdateJSON();
    }

}