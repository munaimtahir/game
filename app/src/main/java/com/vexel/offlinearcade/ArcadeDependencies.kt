package com.vexel.arcadetrio

import android.content.Context
import androidx.room.Room
import com.vexel.offlinearcade.core.common.ArcadeDispatchers
import com.vexel.offlinearcade.core.data.ArcadeDatabase
import com.vexel.offlinearcade.core.data.MIGRATION_4_5
import com.vexel.offlinearcade.core.data.OfflineArcadeRepository
import com.vexel.offlinearcade.core.data.SharedPreferencesSettingsStore
import com.vexel.offlinearcade.core.data.SharedPreferencesLocalDayService
import com.vexel.offlinearcade.monetization.ArcadeAdPolicy
import com.vexel.offlinearcade.monetization.BillingManager
import com.vexel.offlinearcade.monetization.ConnectivityMonitor
import com.vexel.offlinearcade.monetization.MonetizationPreferences
import com.vexel.offlinearcade.monetization.PlayBillingManager

object ArcadeDependencies {
    @Volatile
    private var repository: OfflineArcadeRepository? = null
    @Volatile
    private var billingManager: BillingManager? = null
    @Volatile
    private var monetizationPreferences: MonetizationPreferences? = null
    @Volatile
    private var connectivityMonitor: ConnectivityMonitor? = null
    @Volatile
    private var adPolicy: ArcadeAdPolicy? = null

    fun repository(context: Context): OfflineArcadeRepository {
        return repository ?: synchronized(this) {
            repository ?: OfflineArcadeRepository(
                database = Room.databaseBuilder(
                    context.applicationContext,
                    ArcadeDatabase::class.java,
                    "offline-arcade.db",
                ).addMigrations(MIGRATION_4_5).build(),
                preferences = SharedPreferencesSettingsStore(
                    context.applicationContext.getSharedPreferences(
                        "offline_arcade_preferences",
                        Context.MODE_PRIVATE,
                    ),
                ),
                localDayService = SharedPreferencesLocalDayService(
                    context.applicationContext.getSharedPreferences(
                        "offline_arcade_day_state",
                        Context.MODE_PRIVATE,
                    ),
                ),
                dispatchers = ArcadeDispatchers(),
            ).also { repository = it }
        }
    }

    fun monetizationPreferences(context: Context): MonetizationPreferences {
        return monetizationPreferences ?: synchronized(this) {
            monetizationPreferences ?: MonetizationPreferences(
                context.applicationContext.getSharedPreferences(
                    "offline_arcade_monetization",
                    Context.MODE_PRIVATE,
                ),
            ).also { monetizationPreferences = it }
        }
    }

    fun billingManager(context: Context): BillingManager {
        return billingManager ?: synchronized(this) {
            billingManager ?: PlayBillingManager(
                context = context.applicationContext,
                repository = repository(context),
                preferences = monetizationPreferences(context),
                premiumProductId = BuildConfig.PLAY_PREMIUM_PRODUCT_ID,
            ).also { billingManager = it }
        }
    }

    fun connectivityMonitor(context: Context): ConnectivityMonitor {
        return connectivityMonitor ?: synchronized(this) {
            connectivityMonitor ?: ConnectivityMonitor(context.applicationContext).also {
                connectivityMonitor = it
            }
        }
    }

    fun adPolicy(): ArcadeAdPolicy {
        return adPolicy ?: synchronized(this) {
            adPolicy ?: ArcadeAdPolicy().also { adPolicy = it }
        }
    }
}
