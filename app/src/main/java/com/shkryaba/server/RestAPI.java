package com.shkryaba.server;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestAPI {
    @GET("users")
    Call<List<RetrofitModel>> loadUsers();
    @GET("users/{user}")
    Call<RetrofitModel> loadUser(@Path("user") String user);
    @GET("users/{user}/repos")
    Call<List<UserReposModel>> loadUserRepo(@Path("user") String user);

}
