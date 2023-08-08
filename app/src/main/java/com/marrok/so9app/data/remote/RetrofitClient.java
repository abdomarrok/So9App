package com.marrok.so9app.data.remote;

import com.marrok.so9app.ui.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitClient {


    @POST("/signup/")
    Call<User> SignUpNew(@Body User user);

    @POST("/login/")
    Call<User> LoginUser(@Body User user);


}
