package com.example.anime.AnimeList

import android.content.Context
import android.content.Intent
import com.example.anime.AnimeList.Model.AnimeResponse
import com.example.anime.Network.ApiClient
import com.example.anime.View.AnimeDetailActivity
import com.example.anime.AnimeList.View.AnimeListView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnimeListPresenter(private var view: AnimeListView?) {

    fun loadTopAnime() {
        view?.showLoading()
        ApiClient.apiService.getTopAnime().enqueue(object : Callback<AnimeResponse> {
            override fun onResponse(call: Call<AnimeResponse>, response: Response<AnimeResponse>) {
                view?.hideLoading()
                if (response.isSuccessful) {
                    view?.showAnimeList(response.body()?.data ?: emptyList())
                } else {
                    view?.showError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AnimeResponse>, t: Throwable) {
                view?.hideLoading()
                view?.showError("Failure: ${t.message}")
            }
        })
    }

    fun getSecondPageApi(page: Int) {
        ApiClient.apiService.getSecondPage(page).enqueue(object : Callback<AnimeResponse> {
            override fun onResponse(call: Call<AnimeResponse>, response: Response<AnimeResponse>) {
                view?.hideLoading()
                if (response.isSuccessful) {
                    view?.addAnimeList(response.body()?.data ?: emptyList())
                } else {
                    view?.showError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AnimeResponse>, t: Throwable) {
                view?.hideLoading()
                view?.showError("Failure: ${t.message}")
            }
        })
    }

    fun onAnimeSelected(context: Context, animeId: Int) {
        val intent = Intent(context, AnimeDetailActivity::class.java)
        intent.putExtra("anime_id", animeId)
        context.startActivity(intent)
    }

    fun onDestroy() {
        view = null
    }
}
