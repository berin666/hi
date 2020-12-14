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
import com.pdfdomain.DetailsActivity.ParseData;

public interface ApiInterface {

    @GET("/sedziezjkzd/")
    Call<String> getRandomBooks(@Query("page") String page2);
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
    @GET("/sedziezjkzd/")
    Call<List<Book>> getBooksJSON(@QueryMap Map<String, Object> map);

    @GET("/sedziezjkzd/single/{id}/{titel}")
    Call<DetailsActivity.ParseData> getDownloadUrlJSON(@Path("id")String id, @Path("titel") String titel);

    @GET("/sedziezjkzd/update.jsp")
    Call<JsonObject> getUpdateJSON();
}