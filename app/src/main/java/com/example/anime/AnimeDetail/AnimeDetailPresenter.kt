package com.example.anime.Presenter

import com.example.anime.AnimeDetail.Model.AnimeDetailResponse
import com.example.anime.Network.ApiClient
import com.example.anime.AnimeDetail.View.AnimeDetailView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnimeDetailPresenter(private var view: AnimeDetailView?) {

    fun loadAnimeDetails(animeId: Int) {
        view?.showLoading()

        ApiClient.apiService.getAnimeDetails(animeId).enqueue(object : Callback<AnimeDetailResponse> {
            override fun onResponse(call: Call<AnimeDetailResponse>, response: Response<AnimeDetailResponse>) {
                view?.hideLoading()
                if (response.isSuccessful) {
                    response.body()?.data?.let { anime ->
                        view?.displayAnimeDetails(anime)
                    } ?: view?.showError("Empty response")
                } else {
                    view?.showError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AnimeDetailResponse>, t: Throwable) {
                view?.hideLoading()
                view?.showError("Network failure: ${t.message}")
            }
        })
    }

    fun onDestroy() {
        view = null
    }
}
