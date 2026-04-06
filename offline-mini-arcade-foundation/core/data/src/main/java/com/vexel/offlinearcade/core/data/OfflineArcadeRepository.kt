package com.vexel.offlinearcade.core.data

import com.vexel.offlinearcade.core.model.*

class OfflineArcadeRepository : ArcadeRepository {
    override suspend fun getProfile(): PlayerProfile = PlayerProfile()
    override suspend fun getAllGameStats(): List<GameStats> = GameId.entries.map { GameStats(gameId = it) }
    override suspend fun getChallengesForDay(epochDay: Long): List<DailyChallenge> = emptyList()
    override suspend fun updateGameStats(stats: GameStats) {}
    override suspend fun addCoins(amount: Int) {}
    override suspend fun setTheme(themeId: String) {}
}
