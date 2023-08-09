package com.marrok.so9app.ui.Activities;



import androidx.appcompat.app.AppCompatActivity;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marrok.so9app.R;
import com.marrok.so9app.data.remote.InfoApi;
import com.marrok.so9app.data.remote.RetrofitClient;
import com.marrok.so9app.ui.Models.TokenCheckResponse;
import com.marrok.so9app.ui.Models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String URL_API = InfoApi.API_URL;
    TextView register,forgot_pass;
    private EditText username,password;
    private Button signInBtn;
    private ProgressBar progressBar;
    RetrofitClient retrofitClient;
    private boolean isloggedin=false;
    Retrofit retrofit;

    private void initView() {
        forgot_pass=findViewById(R.id.Forgot_pass);
        signInBtn=findViewById(R.id.btnLogin);
        username=findViewById(R.id.rectangle4_username_login);
        password=findViewById(R.id.rectangle4_password_login);
        register=findViewById(R.id.Register_txt);
        progressBar=findViewById(R.id.progress_bar);
        retrofit = new  Retrofit.Builder()
                .baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitClient = retrofit.create(RetrofitClient.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        progressBar.setVisibility(View.GONE);
        if (isLoggedIn()) {
            startDashboardActivity();
        } else {
            setOnClickListner();
        }
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
                    Log.d(TAG, "onFailure:Login failed request"+t.getMessage());
                }
            });
            return isloggedin;


    }
    }


    private void startDashboardActivity() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        finish();
    }
    private void setOnClickListner() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogIn();
            }
        });

    }
    private void userLogIn() {
        String mail=username.getText().toString().trim();
        String pass=password.getText().toString().trim();

        if(pass.length()<6){
            password.setError("min pass length is 6");
            password.requestFocus();
            return;

        }

        if(mail.isEmpty()){
            username.setError("username is required");
            username.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            password.setError("password is required");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);







        User user = new User(mail, pass);


        Call<User> loginUser= retrofitClient.LoginUser(user);
        Log.d(TAG, "userLogIn: url "+URL_API);
        loginUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    User userResponse = response.body();
                    String token = userResponse.getToken();

                    // Save the token in SharedPreferences
                    SharedPreferences authPrefs = getSharedPreferences("authPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = authPrefs.edit();
                    editor.putString("token", token);
                    editor.apply();
                    // You can now use the token and user details as needed
                    Log.d(TAG, "Token: " + userResponse.getToken());
                    Log.d(TAG, "Username: " + userResponse.getUser().getUsername());
                    Log.d(TAG, "Email: " + userResponse.getUser().getEmail());

                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    startDashboardActivity();

                } else {
                    // Handle the case when the response is not successful (e.g., invalid credentials)
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: login failed "+response.body());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(LoginActivity.this, "failed connection problem", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });









    }



}