package com.marrok.so9app;

import static com.marrok.so9app.R.*;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final int NAV_HOME = R.id.navigation_home;

    private static final String TAG = "DashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: created");
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_dashboard);
        bottomNavigationView = findViewById(id.navigation);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id.fragment_container, new ProfileFragment());
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getTitle().toString()){
                    case "الرئيسية":
                        transaction.replace(id.fragment_container, new HomeFragment());
                        transaction.commit();
                        break;
                    case "إظافة إعلان":
                        Log.d(TAG, "onNavigationItemSelected: add case"+item.getTitle().toString());
                        transaction.replace(id.fragment_container, new AddAdsFragment());
                        transaction.commit();
                        break;
                    case "دردشاتي":
                        transaction.replace(id.fragment_container, new MyChatFragment());
                        transaction.commit();
                        break;
                    case "إعلاناتي":
                        transaction.replace(id.fragment_container, new MyAdsFragment());
                        transaction.commit();
                        break;
                    case "حسابي":
                        transaction.replace(id.fragment_container, new ProfileFragment());
                        transaction.commit();
                    default:
                        break;

                }
                item.setChecked(true);

                return false;
            }
        });

    }

}
