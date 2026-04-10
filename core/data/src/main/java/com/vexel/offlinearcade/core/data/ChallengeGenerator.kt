package com.vexel.offlinearcade.core.data

import com.vexel.offlinearcade.core.model.ChallengeMetric
import com.vexel.offlinearcade.core.model.DailyChallenge
import com.vexel.offlinearcade.core.model.GameId
import kotlin.random.Random

object DailyChallengeGenerator {
    fun generate(epochDay: Long): List<DailyChallenge> {
        val random = Random(epochDay * 31_337L)
        return listOf(
            DailyChallenge(
                challengeId = "pulse-$epochDay",
                epochDay = epochDay,
                gameId = GameId.PULSE_ORBIT,
                title = "Pulse Window",
                description = "Score ${12 + random.nextInt(8)} in Pulse Orbit.",
                metric = ChallengeMetric.PULSE_SCORE,
                targetValue = 12 + random.nextInt(8),
                rewardCoins = 25 + random.nextInt(11),
            ),
            DailyChallenge(
                challengeId = "lane-$epochDay",
                epochDay = epochDay,
                gameId = GameId.LANE_DRIFT,
                title = "Shards Run",
                description = "Collect ${6 + random.nextInt(5)} pickups in Lane Drift.",
                metric = ChallengeMetric.LANE_PICKUPS,
                targetValue = 6 + random.nextInt(5),
                rewardCoins = 25 + random.nextInt(11),
            ),
            DailyChallenge(
                challengeId = "stack-$epochDay",
                epochDay = epochDay,
                gameId = GameId.STACK_DROP,
                title = "Clean Stack",
                description = "Clear ${4 + random.nextInt(4)} lines in Stack Drop.",
                metric = ChallengeMetric.STACK_LINES,
                targetValue = 4 + random.nextInt(4),
                rewardCoins = 30 + random.nextInt(11),
            ),
            DailyChallenge(
                challengeId = "bundle-$epochDay",
                epochDay = epochDay,
                gameId = null,
                title = "Arcade Circuit",
                description = "Finish ${3 + random.nextInt(2)} arcade runs today.",
                metric = ChallengeMetric.ARCADE_RUNS,
                targetValue = 3 + random.nextInt(2),
                rewardCoins = 50 + random.nextInt(21),
            ),
        )
    }
}
