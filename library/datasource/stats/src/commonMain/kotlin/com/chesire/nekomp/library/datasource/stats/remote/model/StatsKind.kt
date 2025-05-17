package com.chesire.nekomp.library.datasource.stats.remote.model

internal enum class StatsKind(val apiString: String) {
    AnimeAmountConsumed("anime-amount-consumed"),
    MangaAmountConsumed("manga-amount-consumed")
    // AnimeActivityHistory("anime-activity-history"),  // NOT YET IMPLEMENTED
    // MangaActivityHistory("manga-activity-history"),  // NOT YET IMPLEMENTED
    // AnimeCategoryBreakdown("anime-category-breakdown"),  // NOT YET IMPLEMENTED
    // MangaCategoryBreakdown("manga-category-breakdown"),  // NOT YET IMPLEMENTED
    // AnimeFavoriteYear("anime-favorite-year"), // NOT YET IMPLEMENTED
    // MangaFavoriteYear("manga-favorite-year") // NOT YET IMPLEMENTED
}
