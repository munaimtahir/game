package com.vexel.arcadetrio

import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MainActivityStartupTest {
    @Test
    fun appStartsWithoutDevice() {
        // Avoid driving the full resume pipeline here (Compose recomposition can be expensive under Robolectric).
        val controller = Robolectric.buildActivity(MainActivity::class.java).create()
        val activity = controller.get()
        assertNotNull(activity)
        controller.destroy()
    }
}
