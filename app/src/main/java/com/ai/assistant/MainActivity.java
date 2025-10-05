package com.ai.assistant;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ai.assistant.databinding.ActivityMainBinding;
import com.ai.assistant.ui.fragments.fragment_dashboard;
import com.ai.assistant.ui.fragments.fragment_home;
import com.ai.assistant.ui.fragments.fragment_notifications;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView toolbarTitle;
    fragment_home homeFragment = new fragment_home();
    fragment_dashboard dashboardFragment = new fragment_dashboard();
    fragment_notifications notificationsFragment = new fragment_notifications();
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbarTitle = findViewById(R.id.toolbarText);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, homeFragment).commit();

        MenuItem initialItem = bottomNavigationView.getMenu().getItem(0);
        if (initialItem != null) {
            toolbarTitle.setText(initialItem.getTitle());
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                toolbarTitle.setText(item.getTitle());

                int itemId = item.getItemId();

                if (itemId == R.id.navHome) {
                    replaceFragment(homeFragment);
                } else if (itemId == R.id.navDashboard) {
                    replaceFragment(dashboardFragment);
                } else if (itemId == R.id.navNotification) {
                    replaceFragment(notificationsFragment);
                }
                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment);
        fragmentTransaction.commit();
    }
}