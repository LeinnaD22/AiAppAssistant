package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.java.ChatFutures;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.GenerateContentResponse;
import com.google.firebase.ai.type.GenerativeBackend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
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

        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messages = new ArrayList<>();
        // Add sample messages or load from a data source

        messageAdapter = new MessageAdapter(messages);
        recyclerView.setAdapter(messageAdapter);

// Initialize the Gemini Developer API backend service
// Create a `GenerativeModel` instance with a model that supports your use case
        GenerativeModel ai = FirebaseAI.getInstance(GenerativeBackend.googleAI())
                .generativeModel("gemini-2.5-flash");

// Use the GenerativeModelFutures Java compatibility layer which offers
// support for ListenableFuture and Publisher APIs
        GenerativeModelFutures model = GenerativeModelFutures.from(ai);


// (optional) Create previous chat history for context
        Content.Builder userContentBuilder = new Content.Builder();


        Content.Builder modelContentBuilder = new Content.Builder();
        modelContentBuilder.setRole("model");
        modelContentBuilder.addText("Great to meet you. What would you like to know?");
        Content modelContent = userContentBuilder.build();

        List<Content> history = Arrays.asList(modelContent);

// Initialize the chat
        ChatFutures chat = model.startChat(history);

// Create a new user message
        TextInputEditText myText = findViewById(R.id.textBox);
        Button button= (Button) findViewById(R.id.sendButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when the button is clicked
                String enteredValue = myText.getText().toString();
                Content.Builder messageBuilder = new Content.Builder();
                messageBuilder.setRole("user");
                messageBuilder.addText(enteredValue);
                messageAdapter.userInsert(enteredValue, messages.size());
                System.out.println(enteredValue);
                System.out.println(messages.size());
                //messages.add(new Message("Me", enteredValue, System.currentTimeMillis(), true));
                myText.setText("");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update UI elements here
                        messageAdapter.notifyItemInserted(messages.size()-1);
                        recyclerView.scrollToPosition(messages.size()- 1);
                    }
                });

                Content message = messageBuilder.build();


// Send the message
                ListenableFuture<GenerateContentResponse> response = chat.sendMessage(message);

                ExecutorService executor = Executors.newFixedThreadPool(5);
                Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        String resultText = result.getText();
                        //messages.add(new Message("Gemini", resultText, System.currentTimeMillis(), false));
                        messageAdapter.aiInsert(resultText, messages.size());
                        System.out.println(messages.size());
                        System.out.println(resultText);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI elements here
                                messageAdapter.notifyItemInserted(messages.size()-1);
                                recyclerView.scrollToPosition(messages.size()- 1);
                            }
                        });

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                }, executor);
            }
        });



    }

}