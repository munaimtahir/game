package com.vexel.offlinearcade.core.data

import com.vexel.offlinearcade.core.model.ArcadeSnapshot
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.core.model.SettingsState
import kotlinx.coroutines.flow.Flow

interface ArcadeRepository {
    val snapshot: Flow<ArcadeSnapshot>
    fun challengesForDay(epochDay: Long): Flow<List<DailyChallenge>>
    suspend fun updateSettings(transform: (SettingsState) -> SettingsState)
    suspend fun recordRun(result: RunResult)
    suspend fun purchaseTheme(themeId: String): Boolean
    suspend fun selectTheme(themeId: String)
    suspend fun purchaseSkin(skinId: String): Boolean
    suspend fun selectSkin(skinId: String, gameId: com.vexel.offlinearcade.core.model.GameId)
    suspend fun setPremiumUnlocked(unlocked: Boolean)
}
