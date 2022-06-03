package com.example.messenger.network;

import com.example.messenger.Message;
import com.example.messenger.MessagesResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MessageApi {
    @GET("/msgs")
    Call<MessagesResponse> getMessages();
}
