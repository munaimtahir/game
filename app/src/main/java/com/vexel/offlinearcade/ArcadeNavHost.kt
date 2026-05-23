package com.vexel.offlinearcade

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vexel.offlinearcade.core.model.ArcadeFeedback
import com.vexel.offlinearcade.core.model.ArcadeSnapshot
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.feature.challenges.ChallengesScreen
import com.vexel.offlinearcade.feature.home.HomeScreen
import com.vexel.offlinearcade.feature.settings.SettingsScreen
import com.vexel.offlinearcade.feature.stats.StatsScreen
import com.vexel.offlinearcade.game.lanedrift.LaneDriftDetailScreen
import com.vexel.offlinearcade.game.lanedrift.LaneDriftScreen
import com.vexel.offlinearcade.game.pulseorbit.PulseOrbitDetailScreen
import com.vexel.offlinearcade.game.pulseorbit.PulseOrbitScreen
import com.vexel.offlinearcade.game.stackdrop.StackDropDetailScreen
import com.vexel.offlinearcade.game.stackdrop.StackDropScreen
import com.vexel.offlinearcade.game.brickvolley.BrickVolleyScreen
import com.vexel.offlinearcade.game.brickvolley.BrickVolleyDetailScreen
import com.vexel.offlinearcade.game.loopsnake.LoopSnakeDetailScreen
import com.vexel.offlinearcade.game.loopsnake.LoopSnakeScreen
import com.vexel.offlinearcade.game.shielddash.ShieldDashDetailScreen
import com.vexel.offlinearcade.game.shielddash.ShieldDashScreen
import com.vexel.offlinearcade.game.gravityflip.GravityFlipDetailScreen
import com.vexel.offlinearcade.game.gravityflip.GravityFlipScreen

@Composable
fun ArcadeNavHost(
    navController: NavHostController,
    snapshot: ArcadeSnapshot,
    feedback: ArcadeFeedback,
    onToggleSound: (Boolean) -> Unit,
    onToggleMusic: (Boolean) -> Unit,
    onToggleVibration: (Boolean) -> Unit,
    onToggleReducedEffects: (Boolean) -> Unit,
    onToggleHighContrast: (Boolean) -> Unit,
    onUnlockTheme: (String) -> Unit,
    onSelectTheme: (String) -> Unit,
    onRecordRun: (RunResult) -> Unit,
) {
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(
                profile = snapshot.profile,
                stats = snapshot.stats,
                todayChallenges = snapshot.challenges,
                onPulseOrbit = { navController.navigate(Routes.PulseOrbitDetail) },
                onLaneDrift = { navController.navigate(Routes.LaneDriftDetail) },
                onStackDrop = { navController.navigate(Routes.StackDropDetail) },
                onBrickVolley = { navController.navigate(Routes.BrickVolleyDetail) },
                onLoopSnake = { navController.navigate(Routes.LoopSnakeDetail) },
                onShieldDash = { navController.navigate(Routes.ShieldDashDetail) },
                onGravityFlip = { navController.navigate(Routes.GravityFlipDetail) },
                onChallenges = { navController.navigate(Routes.Challenges) },
                onStats = { navController.navigate(Routes.Stats) },
                onSettings = { navController.navigate(Routes.Settings) },
            )
        }
        
        composable(Routes.PulseOrbitDetail) {
            PulseOrbitDetailScreen(
                stats = snapshot.statsByGame[GameId.PULSE_ORBIT],
                onPlay = { navController.navigate(Routes.PulseOrbitGame) },
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.PulseOrbitGame) {
            PulseOrbitScreen(
                stats = snapshot.statsByGame[GameId.PULSE_ORBIT],
                settings = snapshot.settings,
                feedback = feedback,
                onRunComplete = onRecordRun,
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.LaneDriftDetail) {
            LaneDriftDetailScreen(
                stats = snapshot.statsByGame[GameId.LANE_DRIFT],
                onPlay = { navController.navigate(Routes.LaneDriftGame) },
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.LaneDriftGame) {
            LaneDriftScreen(
                stats = snapshot.statsByGame[GameId.LANE_DRIFT],
                settings = snapshot.settings,
                feedback = feedback,
                onRunComplete = onRecordRun,
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.StackDropDetail) {
            StackDropDetailScreen(
                stats = snapshot.statsByGame[GameId.STACK_DROP],
                onPlay = { navController.navigate(Routes.StackDropGame) },
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.StackDropGame) {
            StackDropScreen(
                stats = snapshot.statsByGame[GameId.STACK_DROP],
                settings = snapshot.settings,
                feedback = feedback,
                onRunComplete = onRecordRun,
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.BrickVolleyDetail) {
            BrickVolleyDetailScreen(
                stats = snapshot.statsByGame[GameId.BRICK_VOLLEY],
                onPlay = { navController.navigate(Routes.BrickVolleyGame) },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.BrickVolleyGame) {
            BrickVolleyScreen(
                stats = snapshot.statsByGame[GameId.BRICK_VOLLEY],
                settings = snapshot.settings,
                feedback = feedback,
                onRunComplete = onRecordRun,
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.LoopSnakeDetail) {
            LoopSnakeDetailScreen(
                stats = snapshot.statsByGame[GameId.LOOP_SNAKE],
                onPlay = { navController.navigate(Routes.LoopSnakeGame) },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.LoopSnakeGame) {
            LoopSnakeScreen(
                stats = snapshot.statsByGame[GameId.LOOP_SNAKE],
                settings = snapshot.settings,
                feedback = feedback,
                onRunComplete = onRecordRun,
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.ShieldDashDetail) {
            ShieldDashDetailScreen(
                stats = snapshot.statsByGame[GameId.SHIELD_DASH],
                onPlay = { navController.navigate(Routes.ShieldDashGame) },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.ShieldDashGame) {
            ShieldDashScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.GravityFlipDetail) {
            GravityFlipDetailScreen(
                stats = snapshot.statsByGame[GameId.GRAVITY_FLIP],
                onPlay = { navController.navigate(Routes.GravityFlipGame) },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.GravityFlipGame) {
            GravityFlipScreen(
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.Challenges) {
            ChallengesScreen(challenges = snapshot.challenges, onBack = { navController.popBackStack() })
        }
        composable(Routes.Stats) {
            StatsScreen(stats = snapshot.stats, onBack = { navController.popBackStack() })
        }
        composable(Routes.Settings) {
            SettingsScreen(
                settings = snapshot.settings,
                themes = snapshot.themes,
                selectedThemeId = snapshot.profile.selectedThemeId,
                premiumUnlocked = snapshot.profile.premiumUnlocked,
                onToggleSound = onToggleSound,
                onToggleMusic = onToggleMusic,
                onToggleVibration = onToggleVibration,
                onToggleReducedEffects = onToggleReducedEffects,
                onToggleHighContrast = onToggleHighContrast,
                onSelectTheme = onSelectTheme,
                onUnlockTheme = onUnlockTheme,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
