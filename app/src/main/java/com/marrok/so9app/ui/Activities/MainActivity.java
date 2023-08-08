package com.marrok.so9app.ui.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.marrok.so9app.R;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getStartedBtn = (Button) findViewById(R.id.get_started);
        getStartedBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));

        });
    }

}