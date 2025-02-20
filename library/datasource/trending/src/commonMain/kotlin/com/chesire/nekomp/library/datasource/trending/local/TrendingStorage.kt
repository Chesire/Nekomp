package com.chesire.nekomp.library.datasource.trending.local

import com.chesire.nekomp.library.datasource.trending.TrendingItem

// TODO: Change this to a database
class TrendingStorage {

    var trendingAnime: List<TrendingItem> = emptyList()
        private set
    var trendingManga: List<TrendingItem> = emptyList()
        private set
    var topRatedAnime: List<TrendingItem> = emptyList()
        private set
    var topRatedManga: List<TrendingItem> = emptyList()
        private set
    var mostPopularAnime: List<TrendingItem> = emptyList()
        private set
    var mostPopularManga: List<TrendingItem> = emptyList()
        private set

    fun setTrendingAnime(newTrending: List<TrendingItem>) {
        trendingAnime = newTrending
    }

    fun setTrendingManga(newTrending: List<TrendingItem>) {
        trendingManga = newTrending
    }

    fun setTopRatedAnime(newTopRated: List<TrendingItem>) {
        topRatedAnime = newTopRated
    }

    fun setTopRatedManga(newTopRated: List<TrendingItem>) {
        topRatedManga = newTopRated
    }

    fun setMostPopularAnime(newMostPopular: List<TrendingItem>) {
        mostPopularAnime = newMostPopular
    }

    fun setMostPopularManga(newMostPopular: List<TrendingItem>) {
        mostPopularManga = newMostPopular
    }
}
