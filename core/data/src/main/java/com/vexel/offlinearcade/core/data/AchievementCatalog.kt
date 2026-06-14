package com.vexel.offlinearcade.core.data

import com.vexel.offlinearcade.core.model.AchievementGroup
import com.vexel.offlinearcade.core.model.AchievementProgress
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.GameStats
import com.vexel.offlinearcade.core.model.PlayerProfile

object AchievementCatalog {
    fun progressFor(
        stats: List<GameStats>,
        profile: PlayerProfile,
    ): List<AchievementProgress> {
        val byGame = stats.associateBy { it.gameId }
        val pulse = byGame[GameId.PULSE_ORBIT] ?: GameStats(GameId.PULSE_ORBIT)
        val lane = byGame[GameId.LANE_DRIFT] ?: GameStats(GameId.LANE_DRIFT)
        val stack = byGame[GameId.STACK_DROP] ?: GameStats(GameId.STACK_DROP)
        val totalRuns = stats.sumOf { it.sessionsPlayed }
        val gamesPlayed = stats.count { it.sessionsPlayed > 0 }

        return listOf(
            achievement("pulse_first_pass", AchievementGroup.PULSE_ORBIT, "First Pass", "Complete a Pulse Orbit run.", pulse.sessionsPlayed, 1),
            achievement("pulse_combo_5", AchievementGroup.PULSE_ORBIT, "Combo 5", "Reach a 5x combo.", pulse.bestCombo, 5),
            achievement("pulse_combo_10", AchievementGroup.PULSE_ORBIT, "Combo 10", "Reach a 10x combo.", pulse.bestCombo, 10),
            achievement("pulse_score_50", AchievementGroup.PULSE_ORBIT, "Score 50", "Score 50 points.", pulse.highScore, 50),
            achievement("pulse_score_100", AchievementGroup.PULSE_ORBIT, "Score 100", "Score 100 points.", pulse.highScore, 100),
            achievement("pulse_runs_25", AchievementGroup.PULSE_ORBIT, "25 Total Runs", "Play Pulse Orbit 25 times.", pulse.sessionsPlayed, 25),
            achievement("lane_first_shard", AchievementGroup.LANE_DRIFT, "First Shard", "Collect your first shard.", lane.totalPickups, 1),
            achievement("lane_shards_25", AchievementGroup.LANE_DRIFT, "25 Shards Collected", "Collect 25 shards across runs.", lane.totalPickups, 25),
            achievement("lane_shards_100", AchievementGroup.LANE_DRIFT, "100 Shards Collected", "Collect 100 shards across runs.", lane.totalPickups, 100),
            achievement("lane_score_500", AchievementGroup.LANE_DRIFT, "500m Distance", "Reach a 500 score run.", lane.highScore, 500),
            achievement("lane_score_1000", AchievementGroup.LANE_DRIFT, "1000m Distance", "Reach a 1000 score run.", lane.highScore, 1000),
            achievement("lane_runs_25", AchievementGroup.LANE_DRIFT, "25 Total Runs", "Play Lane Drift 25 times.", lane.sessionsPlayed, 25),
            achievement("stack_first_line", AchievementGroup.STACK_DROP, "First Line Clear", "Clear your first line.", stack.bestLines, 1),
            achievement("stack_lines_5", AchievementGroup.STACK_DROP, "5 Lines Cleared", "Clear 5 lines in one run.", stack.bestLines, 5),
            achievement("stack_lines_10", AchievementGroup.STACK_DROP, "10 Lines Cleared", "Clear 10 lines in one run.", stack.bestLines, 10),
            achievement("stack_score_1000", AchievementGroup.STACK_DROP, "Score 1000", "Score 1000 points.", stack.highScore, 1000),
            achievement("stack_score_2500", AchievementGroup.STACK_DROP, "Score 2500", "Score 2500 points.", stack.highScore, 2500),
            achievement("stack_runs_25", AchievementGroup.STACK_DROP, "25 Total Runs", "Play Stack Drop 25 times.", stack.sessionsPlayed, 25),
            achievement("global_all_games", AchievementGroup.GLOBAL, "Play All 3 Games", "Try every MVP game.", gamesPlayed, 3),
            achievement("global_daily_1", AchievementGroup.GLOBAL, "Complete First Daily Challenge", "Finish one daily challenge.", profile.completedDailyChallenges, 1),
            achievement("global_daily_3", AchievementGroup.GLOBAL, "Complete 3 Daily Challenges", "Finish three daily challenges.", profile.completedDailyChallenges, 3),
            achievement("global_streak_3", AchievementGroup.GLOBAL, "3-Day Streak", "Play on three consecutive days.", profile.bestStreakDays, 3),
            achievement("global_streak_7", AchievementGroup.GLOBAL, "7-Day Streak", "Play on seven consecutive days.", profile.bestStreakDays, 7),
            achievement("global_runs_50", AchievementGroup.GLOBAL, "50 Total Runs", "Play 50 total runs.", totalRuns, 50),
        )
    }

    private fun achievement(
        id: String,
        group: AchievementGroup,
        title: String,
        description: String,
        current: Int,
        target: Int,
    ) = AchievementProgress(
        achievementId = id,
        group = group,
        title = title,
        description = description,
        currentProgress = current.coerceAtMost(target),
        targetValue = target,
    )
}
