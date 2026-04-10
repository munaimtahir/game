package com.vexel.offlinearcade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.vexel.offlinearcade.core.ui.OfflineMiniArcadeTheme

@Composable
fun ArcadeApp() {
    val context = LocalContext.current
    val viewModel: ArcadeViewModel = viewModel(factory = ArcadeViewModel.factory(ArcadeDependencies.repository(context)))
    val snapshot by viewModel.snapshot.collectAsState()
    val navController = rememberNavController()
    val feedback = rememberArcadeFeedback(context = context, settings = snapshot.settings)

    OfflineMiniArcadeTheme(themeId = snapshot.profile.selectedThemeId) {
        ArcadeNavHost(
            navController = navController,
            snapshot = snapshot,
            feedback = feedback,
            onToggleSound = viewModel::toggleSound,
            onToggleMusic = viewModel::toggleMusic,
            onToggleVibration = viewModel::toggleVibration,
            onUnlockTheme = viewModel::unlockTheme,
            onSelectTheme = viewModel::selectTheme,
            onRecordRun = viewModel::recordRun,
        )
    }
}
