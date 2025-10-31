package com.example.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;


import com.example.myapplication.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    int chatMenuItemIdCounter = 100;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        setContentView(R.layout.activity_main);

        // Initialize the DrawerLayout, Toolbar, and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.drawer);

        // Create an ActionBarDrawerToggle to handle
        // the drawer's open/close state
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        // Add the toggle as a listener to the DrawerLayout
        drawerLayout.addDrawerListener(toggle);

        // Synchronize the toggle's state with the linked DrawerLayout
        toggle.syncState();

        // Set a listener for when an item in the NavigationView is selected
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // Called when an item in the NavigationView is selected.
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                // Handle the selected item based on its ID
                if (id == R.id.new_chat) { // ID of your static "New Chat" button/FAB
                    // If the trigger is from the menu itself
                    openNewChat();
                } else if (id >= 101) {
                    // Handle dynamically created chat items (IDs 101, 102, etc.)
                    // Implement logic to open the specific chat screen
                    openNewChat();
                    // String chatId = item.getIntent().getStringExtra("CHAT_ID");
                    // openChatScreen(chatId);
                }


                // Close the drawer after selection
                drawerLayout.closeDrawers();
                // Indicate that the item selection has been handled
                return true;
            }
        });




        // Add a callback to handle the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            // Called when the back button is pressed.
            @Override
            public void handleOnBackPressed() {
                // Check if the drawer is open
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // Close the drawer if it's open
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Finish the activity if the drawer is closed
                    finish();
                }
            }
        });




    }

    void addNewChatMenuItem() {
        Menu menu = navigationView.getMenu();

        // Increment counter for a unique ID
        chatMenuItemIdCounter++;
        String chatName = "Chat " + (chatMenuItemIdCounter - 100);

        // Dynamically add a new MenuItem to the 'menu'
        // Group ID, Item ID, Order, Title
        MenuItem newChatItem = menu.add(R.id.drawer, chatMenuItemIdCounter, Menu.NONE, chatName);
        navigationView.invalidate();
    }

    public void openNewChat(){
        // 1. Create a new instance of the HomeFragment
        HomeFragment newHomeFragment = new HomeFragment();

        // 2. Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 3. Start a new transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 4. Add the new fragment instance to the container
        //    R.id.fragment_container is the ID of the layout (e.g., FrameLayout)
        //    where you want the fragment to appear in your XML layout file.
        fragmentTransaction.add(R.id.fragment_container, newHomeFragment);

        // Optional: Add the transaction to the back stack so pressing the
        // 'Back' button will return to the previous fragment/state.
        fragmentTransaction.addToBackStack(null);

        // 5. Commit the transaction
        fragmentTransaction.commit();

    }

}