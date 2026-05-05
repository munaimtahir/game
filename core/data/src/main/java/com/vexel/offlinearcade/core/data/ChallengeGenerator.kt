package com.vexel.offlinearcade.core.data

import com.vexel.offlinearcade.core.model.ChallengeMetric
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.GameId
import kotlin.random.Random

object DailyChallengeGenerator {
    fun generate(epochDay: Long): List<DailyChallenge> {
        val random = Random(epochDay * 31_337L)
        val pulseTarget = 12 + random.nextInt(8)
        val pulseReward = 25 + random.nextInt(11)
        val laneTarget = 6 + random.nextInt(5)
        val laneReward = 25 + random.nextInt(11)
        val stackTarget = 4 + random.nextInt(4)
        val stackReward = 30 + random.nextInt(11)
        val bundleTarget = 3 + random.nextInt(2)
        val bundleReward = 50 + random.nextInt(21)
        
        return listOf(
            DailyChallenge(
                challengeId = "pulse-$epochDay",
                epochDay = epochDay,
                gameId = GameId.PULSE_ORBIT,
                title = "Pulse Window",
                description = "Score $pulseTarget in Pulse Orbit.",
                metric = ChallengeMetric.PULSE_SCORE,
                targetValue = pulseTarget,
                rewardCoins = pulseReward,
            ),
            DailyChallenge(
                challengeId = "lane-$epochDay",
                epochDay = epochDay,
                gameId = GameId.LANE_DRIFT,
                title = "Shards Run",
                description = "Collect $laneTarget pickups in Lane Drift.",
                metric = ChallengeMetric.LANE_PICKUPS,
                targetValue = laneTarget,
                rewardCoins = laneReward,
            ),
            DailyChallenge(
                challengeId = "stack-$epochDay",
                epochDay = epochDay,
                gameId = GameId.STACK_DROP,
                title = "Clean Stack",
                description = "Clear $stackTarget lines in Stack Drop.",
                metric = ChallengeMetric.STACK_LINES,
                targetValue = stackTarget,
                rewardCoins = stackReward,
            ),
            DailyChallenge(
                challengeId = "bundle-$epochDay",
                epochDay = epochDay,
                gameId = null,
                title = "Arcade Circuit",
                description = "Finish $bundleTarget arcade runs today.",
                metric = ChallengeMetric.ARCADE_RUNS,
                targetValue = bundleTarget,
                rewardCoins = bundleReward,
            ),
        )
    }
}
