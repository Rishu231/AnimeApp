package com.example.anime.AnimeList.View

import com.example.anime.AnimeList.Model.Anime

interface AnimeListView {
    fun showLoading()
    fun hideLoading()
    fun showAnimeList(animeList: List<Anime>)
    fun addAnimeList(animeList: List<Anime>)
    fun showError(message: String)
}
