package com.vexel.offlinearcade.core.data

import com.vexel.offlinearcade.core.model.*

interface ArcadeRepository {
    suspend fun getProfile(): PlayerProfile
    suspend fun getAllGameStats(): List<GameStats>
    suspend fun getChallengesForDay(epochDay: Long): List<DailyChallenge>
    suspend fun updateGameStats(stats: GameStats)
    suspend fun addCoins(amount: Int)
    suspend fun setTheme(themeId: String)
}
