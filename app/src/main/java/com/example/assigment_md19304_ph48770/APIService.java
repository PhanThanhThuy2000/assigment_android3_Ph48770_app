package com.example.assigment_md19304_ph48770;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    public String DOMAIN = "http://192.168.0.101:3000/";
    @GET("/api/list")
    Call<List<CarModel>> getCars();
    @POST("/api/create")
    Call<List<CarModel>> createCars(@Body CarModel xe);
    @DELETE("/api/delete/{id}")
    Call<List<CarModel>> delete(@Path("id") String id);

}
