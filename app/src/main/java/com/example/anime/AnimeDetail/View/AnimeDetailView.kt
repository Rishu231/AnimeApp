package com.example.anime.AnimeDetail.View

import com.example.anime.AnimeList.Model.Anime

interface AnimeDetailView {
    fun showLoading()
    fun hideLoading()
    fun displayAnimeDetails(anime: Anime)
    fun showError(message: String)
    fun playTrailer(embedUrl: String)
    fun showPoster(imageUrl: String)
}
