package com.example.anime

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anime.AnimeList.Adapter.AnimeAdapter
import com.example.anime.AnimeList.Model.Anime
import com.example.anime.AnimeList.AnimeListPresenter
import com.example.anime.AnimeList.View.AnimeListView
import com.example.anime.databinding.ActivityMainBinding  // Updated binding import

class MainActivity : AppCompatActivity(), AnimeListView {
    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: AnimeListPresenter
    private lateinit var animeAdapter: AnimeAdapter
    var isLoading = false
    var page = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = AnimeListPresenter(this)


        animeAdapter = AnimeAdapter(mutableListOf()) { anime ->
            presenter.onAnimeSelected(this, anime.mal_id)
        }


        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = animeAdapter
            setHasFixedSize(true)
        }


        presenter.loadTopAnime()


        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // Load more items only if not already loading and near the end
                if (!isLoading && lastVisibleItemPosition >= totalItemCount - 4) {
                    isLoading = true
                    binding.progressBar.visibility = View.VISIBLE
                    presenter.getSecondPageApi(page)
                }
            }
        })
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.tvError.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun showAnimeList(animeList: List<Anime>) {
        if (animeList.isEmpty()) {
            binding.tvError.visibility = View.VISIBLE
            binding.tvError.text = "No anime found"
        } else {
            // Only set the adapter once when data is first loaded
            if (animeAdapter.itemCount == 0) {
                animeAdapter = AnimeAdapter(animeList.toMutableList()) { anime ->
                    presenter.onAnimeSelected(this, anime.mal_id)
                }
                binding.recyclerView.adapter = animeAdapter
            }
        }
    }

    override fun addAnimeList(animeList: List<Anime>) {
        if (animeList.isEmpty()) {
            binding.tvError.visibility = View.VISIBLE
            binding.tvError.text = "No more anime found"
        } else {
            // Add new items to the existing adapter
            animeAdapter.addItems(animeList)
            isLoading = false
            ++page
        }
    }

    override fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.tvError.visibility = View.VISIBLE
        binding.tvError.text = message

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
