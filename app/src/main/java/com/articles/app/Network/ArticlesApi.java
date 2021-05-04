package com.articles.app.Network;

import com.articles.app.Models.ArticlesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArticlesApi {
    @GET("news")
    Call<ArticlesResponse> getArticles (
      @Query("token") String token
    );
}
