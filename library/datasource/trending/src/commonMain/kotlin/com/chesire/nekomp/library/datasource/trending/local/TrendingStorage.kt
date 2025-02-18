package com.chesire.nekomp.library.datasource.trending.local

import com.chesire.nekomp.library.datasource.trending.TrendingItem

// TODO: Change this to a database
class TrendingStorage {

    var trendingAnime: List<TrendingItem> = emptyList()
        private set
    var trendingManga: List<TrendingItem> = emptyList()
        private set

    fun setTrendingAnime(newTrending: List<TrendingItem>) {
        trendingAnime = newTrending
    }

    fun setTrendingManga(newTrending: List<TrendingItem>) {
        trendingManga = newTrending
    }
}
