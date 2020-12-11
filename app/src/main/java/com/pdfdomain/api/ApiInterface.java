package com.pdfdomain.api;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import com.pdfdomain.Book;
import com.pdfdomain.DetailsActivity;

public interface ApiInterface {

    @GET("/api/")
    Call<String> getRandomBooks(@Query("page") String pageCount);
    //http://pdfdomain.com/api/?page=2
    /*
    http://berin.com/berinapp/first?a=time
    http = protocol - https
    berin.com = domain name
    /berinapp/ = path
    first = path
    a = query param
    time = query value
     */
    @GET("/api/")
    Call<List<Book>> getBooksJSON(@QueryMap Map<String, Object> map);

    @GET("/api/single/")
    Call<DetailsActivity.ParseData> getDownloadUrlJSON(@Path("md5") String md5);

    @GET("/api/update.jsp")
    Call<JsonObject> getUpdateJSON();
}