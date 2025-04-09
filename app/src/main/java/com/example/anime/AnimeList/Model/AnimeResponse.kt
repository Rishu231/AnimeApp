package com.example.anime.AnimeList.Model

data class AnimeResponse(
    val pagination: Pagination,
    val data: List<Anime>
)
data class Pagination(
    val last_visible_page: Int,
    val has_next_page: Boolean,
    val current_page: Int,
    val items: PaginationItems
)

data class PaginationItems(
    val count: Int,
    val total: Int,
    val per_page: Int
)
