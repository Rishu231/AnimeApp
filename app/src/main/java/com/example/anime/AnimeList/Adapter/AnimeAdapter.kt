package com.example.anime.AnimeList.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.anime.AnimeList.Model.Anime
import com.example.anime.R
import com.example.anime.databinding.ItemAnimeBinding
import com.squareup.picasso.Picasso

class AnimeAdapter(
    private var animeList: MutableList<Anime>, private val onItemClick: (Anime) -> Unit) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    fun addItems(newItems: List<Anime>) {
        val startPosition = animeList.size
        animeList.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(animeList[position])
    }

    override fun getItemCount(): Int = animeList.size

    inner class AnimeViewHolder(private val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(animeList[position])
                }
            }
        }

        fun bind(anime: Anime) {
            binding.tvTitle.text = anime.title
            binding.tvEpisodes.text = "Episodes: ${anime.episodes ?: "Unknown"}"

            // Set rating
            val score = anime.score ?: 0.0
            binding.tvScore.text = "$score"

            // Load image with Picasso
            Picasso.get()
                .load(anime.images.jpg.image_url)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(binding.ivPoster)
        }
    }
}
