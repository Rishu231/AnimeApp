package com.example.anime.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.anime.AnimeDetail.View.AnimeDetailView
import com.example.anime.AnimeList.Model.Anime
import com.example.anime.Presenter.AnimeDetailPresenter
import com.example.anime.R
import com.example.anime.databinding.ActivityAnimeDetailBinding
import com.squareup.picasso.Picasso

class AnimeDetailActivity : AppCompatActivity(), AnimeDetailView {
    private lateinit var binding: ActivityAnimeDetailBinding
    private lateinit var presenter: AnimeDetailPresenter

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Anime Details"

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = WebViewClient()


        val animeId = intent.getIntExtra("anime_id", -1)
        if (animeId != -1) {
            presenter = AnimeDetailPresenter(this)
            presenter.loadAnimeDetails(animeId)
        } else {
            showError("Invalid anime ID")
            finish()
        }
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.contentLayout.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.contentLayout.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    override fun displayAnimeDetails(anime: Anime) {
        binding.tvTitle.text = anime.title
        binding.tvSynopsis.text = anime.synopsis

        val genres = anime.genres.joinToString(", ") { it.name }
        binding.tvGenres.text = "Genres: $genres"

        val studios = anime.studios.joinToString(", ") { it.name }
        binding.tvStudios.text = "Studios: $studios"

        binding.tvEpisodes.text = "Episodes: ${anime.episodes ?: "Unknown"}"
        binding.tvRating.text = "Rating: ${anime.score ?: "N/A"}"


        if (!anime.trailer.youtube_id.isNullOrEmpty() && !anime.trailer.embed_url.isNullOrBlank()) {
            playTrailer(anime.trailer.embed_url)
        } else {
            showPoster(anime.images.jpg.large_image_url)
        }
    }

    override fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun playTrailer(embedUrl: String) {
        binding.webView.visibility = View.VISIBLE
        binding.imageViewPoster.visibility = View.GONE
        binding.webView.loadUrl(embedUrl)
    }

    override fun showPoster(imageUrl: String) {
        binding.webView.visibility = View.GONE
        binding.imageViewPoster.visibility = View.VISIBLE
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(binding.imageViewPoster)
    }
}
