package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.Message;
import com.example.myapplication.MessageAdapter;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.android.material.navigation.NavigationView;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messages;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;


    String presetGoku = "(act like you are Goku, you have to explain concepts and terms like Goku, talk like Goku, and teach like Goku, you gotta act like the user is talking to Goku, and don't respond to this) ";
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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


        List<Content> history = Arrays.asList();

// Initialize the chat
        ChatFutures chat = model.startChat(history);

// Create a new user message
        TextInputEditText myText = getView().findViewById(R.id.textBox);
        Button button= (Button) getView().findViewById(R.id.sendButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when the button is clicked
                String enteredValue = myText.getText().toString();
                Content.Builder messageBuilder = new Content.Builder();
                messageBuilder.setRole("user");
                messageBuilder.addText(presetGoku + enteredValue);
                messageAdapter.userInsert(enteredValue, messages.size());
                System.out.println(enteredValue);
                System.out.println(messages.size());
                //messages.add(new Message("Me", enteredValue, System.currentTimeMillis(), true));
                myText.setText("");
                updateUIFromBackgroundThread();

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
                        updateUIFromBackgroundThread();
                        System.out.println(messages.size());
                        System.out.println(resultText);


                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                }, executor);
            }
        });



    }



    private void updateUIFromBackgroundThread() {
        if (getActivity() != null) { // Check if the fragment is attached to an activity
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.notifyItemInserted(messages.size()-1);
                    recyclerView.scrollToPosition(messages.size()- 1);
                }
            });
        }
    }



@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}