package com.ai.assistant.ui.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ai.assistant.R;
import com.ai.assistant.ai.ResponseCallback;
import com.ai.assistant.recyclerview.Message;
import com.ai.assistant.recyclerview.MessageAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.GenerateContentResponse;
import com.google.firebase.ai.type.GenerativeBackend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class fragment_home extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private Executor mainExecutor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messages = new ArrayList<>();

        messageAdapter = new MessageAdapter(requireContext() ,messages);
        recyclerView.setAdapter(messageAdapter);

        GenerativeModel ai = FirebaseAI.getInstance(GenerativeBackend.googleAI())
                .generativeModel("gemini-2.5-flash");
        GenerativeModelFutures model = GenerativeModelFutures.from(ai);

        TextInputEditText userText = view.findViewById(R.id.userText);
        Button button = view.findViewById(R.id.sendButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String userString = userText.getText().toString();
                userText.setText("");
                userText.clearFocus();
                messages.add(new Message("Me", userString, System.currentTimeMillis(), true));
                messageAdapter.notifyItemInserted(messages.size() - 1);
                Content prompt = new Content.Builder()
                        .addText(userString)
                        .build();
                generateText(prompt, model, new ResponseCallback() {
                    @Override
                    public void onSuccess(String text) {
                        messages.add(new Message("Gemini", text, System.currentTimeMillis(), false));
                        messageAdapter.notifyItemInserted(messages.size() - 1);
                    }

                    @Override
                    public void onFailure(String error) {
                        messages.add(new Message("Gemini", error, System.currentTimeMillis(), false));
                        messageAdapter.notifyItemInserted(messages.size() - 1);
                    }
                });
            }
        });

        return view;
    }

    public void generateText(Content prompt, GenerativeModelFutures model, ResponseCallback callback) {
        ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
        Executor executor = ContextCompat.getMainExecutor(requireContext());
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                callback.onSuccess(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                callback.onFailure(t.getMessage());
            }
        }, executor);
    }
}