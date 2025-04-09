package com.example.anime.Network

import com.example.anime.AnimeDetail.Model.AnimeDetailResponse
import com.example.anime.AnimeList.Model.AnimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("top/anime")
    fun getTopAnime(): Call<AnimeResponse>

    @GET("top/anime")
    fun getSecondPage(@Query("page") page: Int): Call<AnimeResponse>

    @GET("anime/{id}")
    fun getAnimeDetails(@Path("id") id: Int): Call<AnimeDetailResponse>
}
