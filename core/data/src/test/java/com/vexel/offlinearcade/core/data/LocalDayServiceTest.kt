package com.vexel.offlinearcade.core.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.vexel.offlinearcade.core.common.InstantProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.Instant
import java.time.ZoneId

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LocalDayServiceTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.getSharedPreferences("test_local_day", Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test
    fun sameDayReturnsCurrentLocalDay() {
        val service = createService(
            instant = "2026-07-14T10:15:30Z",
            zoneId = "UTC",
        )

        val day = service.currentDay()

        assertEquals(20_648L, day.epochDay)
        assertEquals("UTC", day.zoneId)
    }

    @Test
    fun timezoneChangeUsesLocalDate() {
        val service = createService(
            instant = "2026-07-14T23:30:00Z",
            zoneId = "Asia/Tokyo",
        )

        val day = service.currentDay()

        assertEquals(20_649L, day.epochDay)
        assertEquals("Asia/Tokyo", day.zoneId)
    }

    @Test
    fun backwardClockDoesNotRollObservedDayBack() {
        val prefs = context.getSharedPreferences("test_local_day", Context.MODE_PRIVATE)
        val first = SharedPreferencesLocalDayService(
            sharedPreferences = prefs,
            instantProvider = InstantProvider { Instant.parse("2026-07-15T10:00:00Z") },
            zoneIdProvider = { ZoneId.of("UTC") },
        )
        val second = SharedPreferencesLocalDayService(
            sharedPreferences = prefs,
            instantProvider = InstantProvider { Instant.parse("2026-07-14T09:00:00Z") },
            zoneIdProvider = { ZoneId.of("UTC") },
        )

        first.currentDay()
        val resolved = second.currentDay()

        assertEquals(20_649L, resolved.epochDay)
    }

    @Test
    fun forwardClockAdvancesObservedDay() {
        val prefs = context.getSharedPreferences("test_local_day", Context.MODE_PRIVATE)
        val first = SharedPreferencesLocalDayService(
            sharedPreferences = prefs,
            instantProvider = InstantProvider { Instant.parse("2026-07-14T09:00:00Z") },
            zoneIdProvider = { ZoneId.of("UTC") },
        )
        val second = SharedPreferencesLocalDayService(
            sharedPreferences = prefs,
            instantProvider = InstantProvider { Instant.parse("2026-07-16T09:00:00Z") },
            zoneIdProvider = { ZoneId.of("UTC") },
        )

        first.currentDay()
        val resolved = second.currentDay()

        assertEquals(20_650L, resolved.epochDay)
    }

    private fun createService(
        instant: String,
        zoneId: String,
    ): SharedPreferencesLocalDayService {
        return SharedPreferencesLocalDayService(
            sharedPreferences = context.getSharedPreferences("test_local_day", Context.MODE_PRIVATE),
            instantProvider = InstantProvider { Instant.parse(instant) },
            zoneIdProvider = { ZoneId.of(zoneId) },
        )
    }
}
