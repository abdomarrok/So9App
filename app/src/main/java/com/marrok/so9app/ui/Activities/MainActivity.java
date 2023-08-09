package com.marrok.so9app.ui.Activities;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.marrok.so9app.R;
import com.marrok.so9app.data.remote.InfoApi;
import com.marrok.so9app.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String URL_API = InfoApi.API_URL;
    private Button getStartedBtn;
    private boolean isloggedin=false;
    RetrofitClient retrofitClient;
     Retrofit retrofit;
    private void initView() {
        Log.d(TAG, "initView: ");
        getStartedBtn = (Button) findViewById(R.id.get_started);
        retrofit = new  Retrofit.Builder()
                .baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitClient = retrofit.create(RetrofitClient.class);

    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started mainActivity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (isLoggedIn()) {
            startDashboardActivity();
        } else {
            setOnClickListner();
        }

    }

    private void setOnClickListner() {
        Log.d(TAG, "setOnClickListner: ");
        getStartedBtn.setOnClickListener(view -> {
            if (isLoggedIn()) {
                startDashboardActivity();
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }


        });
    }


    private boolean isLoggedIn() {

        SharedPreferences authPrefs = getSharedPreferences("authPrefs", MODE_PRIVATE);
        String token = authPrefs.getString("token", "");
        if(token.isEmpty()){
            return false;
        }else{
            Log.d(TAG, "isLoggedIn:  saved token"+token);
            String authToken= "token "+token;
            Log.d(TAG, "isLoggedIn: authToken"+authToken);
            Call<String> checkTokenCall = retrofitClient.checkTokenValidity(authToken);
            checkTokenCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String tokenCheckResponse = response.body();
                        String message = tokenCheckResponse;
                        Log.d(TAG, "onResponse: token validation response body"+response.body());
                        isloggedin = true;
                    }else{
                        Log.d(TAG, "onResponse: token validation failed");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "onFailure: Main failed request"+t.getMessage());
                }
            });
            return isloggedin;


        }
    }

    private void startDashboardActivity() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        finish();
    }



}