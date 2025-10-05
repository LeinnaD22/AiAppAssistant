package com.ai.assistant.ai;

public interface ResponseCallback {
    void onSuccess(String text);
    void onFailure(String error);
}
