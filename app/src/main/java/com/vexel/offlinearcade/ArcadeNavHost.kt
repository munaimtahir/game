package com.vexel.arcadetrio

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vexel.offlinearcade.core.model.ArcadeFeedback
import com.vexel.offlinearcade.core.model.ArcadeSnapshot
import com.vexel.offlinearcade.core.model.GameId
import com.vexel.offlinearcade.core.model.RunResult
import com.vexel.offlinearcade.monetization.BillingUiState
import com.vexel.offlinearcade.monetization.MarketplaceAdBanner
import com.vexel.offlinearcade.feature.challenges.ChallengesScreen
import com.vexel.offlinearcade.feature.home.HomeScreen
import com.vexel.offlinearcade.feature.settings.SettingsScreen
import com.vexel.offlinearcade.feature.stats.StatsScreen
import com.vexel.offlinearcade.feature.marketplace.MarketplaceScreen
import com.vexel.offlinearcade.game.lanedrift.LaneDriftDetailScreen
import com.vexel.offlinearcade.game.lanedrift.LaneDriftDebugConfig
import com.vexel.offlinearcade.game.lanedrift.LaneDriftScreen
import com.vexel.offlinearcade.game.pulseorbit.PulseOrbitDetailScreen
import com.vexel.offlinearcade.game.pulseorbit.PulseOrbitScreen
import com.vexel.offlinearcade.game.stackdrop.StackDropDetailScreen
import com.vexel.offlinearcade.game.stackdrop.StackDropScreen

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
    onUnlockSkin: (String) -> Unit,
    onSelectSkin: (String, GameId) -> Unit,
    onRecordRun: (RunResult) -> Unit,
    onTutorialSeen: (GameId) -> Unit,
    billingState: BillingUiState,
    onBuyPremium: () -> Unit,
    onRestorePremium: () -> Unit,
    showMarketplaceAd: Boolean,
    onMarketplaceAdImpression: () -> Unit,
) {
    fun navigateToGame(gameId: GameId) {
        navController.navigate(
            when (gameId) {
                GameId.PULSE_ORBIT -> Routes.PulseOrbitGame
                GameId.LANE_DRIFT -> Routes.LaneDriftGame
                GameId.STACK_DROP -> Routes.StackDropGame
            },
        )
    }

    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(
                profile = snapshot.profile,
                stats = snapshot.stats,
                todayChallenges = snapshot.challenges,
                onPulseOrbit = { navController.navigate(Routes.PulseOrbitDetail) },
                onLaneDrift = { navController.navigate(Routes.LaneDriftDetail) },
                onStackDrop = { navController.navigate(Routes.StackDropDetail) },
                onChallenges = { navController.navigate(Routes.Challenges) },
                onStats = { navController.navigate(Routes.Stats) },
                onSettings = { navController.navigate(Routes.Settings) },
                onMarketplace = { navController.navigate(Routes.Marketplace) },
            )
        }
        
        composable(Routes.PulseOrbitDetail) {
            PulseOrbitDetailScreen(
                stats = snapshot.statsByGame[GameId.PULSE_ORBIT],
                coins = snapshot.profile.coins,
                streak = snapshot.profile.currentStreakDays,
                onPlay = { navController.navigate(Routes.PulseOrbitGame) },
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.PulseOrbitGame) {
            PulseOrbitScreen(
                stats = snapshot.statsByGame[GameId.PULSE_ORBIT],
                settings = snapshot.settings,
                equippedSkin = snapshot.profile.selectedPulseOrbitSkin,
                feedback = feedback,
                tutorialSeen = snapshot.profile.tutorialSeenPulseOrbit,
                onTutorialSeen = { onTutorialSeen(GameId.PULSE_ORBIT) },
                onRunComplete = onRecordRun,
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.LaneDriftDetail) {
            LaneDriftDetailScreen(
                stats = snapshot.statsByGame[GameId.LANE_DRIFT],
                coins = snapshot.profile.coins,
                streak = snapshot.profile.currentStreakDays,
                onPlay = { navController.navigate(Routes.LaneDriftGame) },
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.LaneDriftGame) {
            LaneDriftScreen(
                stats = snapshot.statsByGame[GameId.LANE_DRIFT],
                settings = snapshot.settings,
                feedback = feedback,
                tutorialSeen = snapshot.profile.tutorialSeenLaneDrift,
                onTutorialSeen = { onTutorialSeen(GameId.LANE_DRIFT) },
                onRunComplete = onRecordRun,
                onBack = { navController.popBackStack() },
                debugConfig = if (BuildConfig.DEBUG) {
                    LaneDriftDebugConfig()
                } else {
                    null
                },
            )
        }
        
        composable(Routes.StackDropDetail) {
            StackDropDetailScreen(
                stats = snapshot.statsByGame[GameId.STACK_DROP],
                coins = snapshot.profile.coins,
                streak = snapshot.profile.currentStreakDays,
                onPlay = { navController.navigate(Routes.StackDropGame) },
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.StackDropGame) {
            StackDropScreen(
                stats = snapshot.statsByGame[GameId.STACK_DROP],
                settings = snapshot.settings,
                feedback = feedback,
                tutorialSeen = snapshot.profile.tutorialSeenStackDrop,
                onTutorialSeen = { onTutorialSeen(GameId.STACK_DROP) },
                onRunComplete = onRecordRun,
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.Challenges) {
            ChallengesScreen(
                challenges = snapshot.challenges,
                coins = snapshot.profile.coins,
                streak = snapshot.profile.currentStreakDays,
                onPlayGame = ::navigateToGame,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.Stats) {
            StatsScreen(
                stats = snapshot.stats,
                profile = snapshot.profile,
                achievements = snapshot.achievements,
                coins = snapshot.profile.coins,
                streak = snapshot.profile.currentStreakDays,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.Settings) {
            SettingsScreen(
                settings = snapshot.settings,
                premiumUnlocked = snapshot.profile.premiumUnlocked,
                premiumProductAvailable = billingState.productAvailable,
                premiumPending = billingState.pendingPurchase,
                premiumStatusMessage = billingState.message,
                coins = snapshot.profile.coins,
                streak = snapshot.profile.currentStreakDays,
                onToggleSound = onToggleSound,
                onToggleMusic = onToggleMusic,
                onToggleVibration = onToggleVibration,
                onToggleReducedEffects = onToggleReducedEffects,
                onToggleHighContrast = onToggleHighContrast,
                onBuyPremium = onBuyPremium,
                onRestorePremium = onRestorePremium,
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.Marketplace) {
            MarketplaceScreen(
                coins = snapshot.profile.coins,
                streak = snapshot.profile.currentStreakDays,
                themes = snapshot.themes,
                skins = snapshot.skins,
                selectedThemeId = snapshot.profile.selectedThemeId,
                selectedPulseOrbitSkin = snapshot.profile.selectedPulseOrbitSkin,
                selectedLaneDriftSkin = snapshot.profile.selectedLaneDriftSkin,
                selectedStackDropSkin = snapshot.profile.selectedStackDropSkin,
                premiumUnlocked = snapshot.profile.premiumUnlocked,
                premiumProductAvailable = billingState.productAvailable,
                onBuyPremium = onBuyPremium,
                adSlot = if (showMarketplaceAd) {
                    {
                        MarketplaceAdBanner(
                            adUnitId = BuildConfig.ADMOB_MARKETPLACE_BANNER_AD_UNIT_ID,
                            onImpression = onMarketplaceAdImpression,
                        )
                    }
                } else {
                    null
                },
                onSelectTheme = onSelectTheme,
                onUnlockTheme = onUnlockTheme,
                onSelectSkin = onSelectSkin,
                onUnlockSkin = onUnlockSkin,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
