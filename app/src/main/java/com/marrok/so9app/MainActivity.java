package com.marrok.so9app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getStartedBtn = (Button) findViewById(R.id.get_started);
        getStartedBtn.setOnClickListener(view -> {

            Toast.makeText(this, "hhhhhhhh", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));

        });
    }

}