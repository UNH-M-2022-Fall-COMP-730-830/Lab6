package com.example.weather.network;

public interface NetworkRequestCallback<T> {
    void onResult(T data);
    void onError(Exception e);
}
