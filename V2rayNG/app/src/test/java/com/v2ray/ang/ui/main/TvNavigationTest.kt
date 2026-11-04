package com.v2ray.ang.ui.main

import android.view.KeyEvent
import com.v2ray.ang.util.TvRemoteAction
import com.v2ray.ang.util.TvUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class TvNavigationTest {
    @Test
    fun mediaAndColorRemoteButtonsMapToServiceAndPingActions() {
        for (keyCode in listOf(
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
            KeyEvent.KEYCODE_MEDIA_PLAY,
            KeyEvent.KEYCODE_MEDIA_STOP,
            KeyEvent.KEYCODE_BUTTON_Y,
            KeyEvent.KEYCODE_PROG_GREEN,
        )) {
            assertEquals(TvRemoteAction.ToggleService, TvUtil.resolveRemoteKeyAction(keyCode))
        }
        assertEquals(TvRemoteAction.TestCurrentPing, TvUtil.resolveRemoteKeyAction(KeyEvent.KEYCODE_CAPTIONS))
        assertEquals(TvRemoteAction.TestCurrentPing, TvUtil.resolveRemoteKeyAction(KeyEvent.KEYCODE_PROG_YELLOW))
    }

    @Test
    fun channelAndShoulderButtonsCycleGroupsOnlyWhenSearchIsClosed() {
        assertEquals(TvRemoteAction.PreviousGroup, TvUtil.resolveRemoteKeyAction(KeyEvent.KEYCODE_CHANNEL_UP, isSearchActive = false))
        assertEquals(TvRemoteAction.PreviousGroup, TvUtil.resolveRemoteKeyAction(KeyEvent.KEYCODE_BUTTON_L1, isSearchActive = false))
        assertEquals(TvRemoteAction.NextGroup, TvUtil.resolveRemoteKeyAction(KeyEvent.KEYCODE_CHANNEL_DOWN, isSearchActive = false))
        assertEquals(TvRemoteAction.NextGroup, TvUtil.resolveRemoteKeyAction(KeyEvent.KEYCODE_BUTTON_R1, isSearchActive = false))

        assertNull(TvUtil.resolveRemoteKeyAction(KeyEvent.KEYCODE_CHANNEL_UP, isSearchActive = true))
        assertNull(TvUtil.resolveRemoteKeyAction(KeyEvent.KEYCODE_CHANNEL_DOWN, isSearchActive = true))
        assertNull(TvUtil.resolveRemoteKeyAction(KeyEvent.KEYCODE_BUTTON_B, isSearchActive = true))
    }

    @Test
    fun groupSteppingClampsToValidBounds() {
        assertEquals(0, TvUtil.stepGroupIndex(currentIndex = 0, groupCount = 0, delta = 1))
        assertEquals(0, TvUtil.stepGroupIndex(currentIndex = 0, groupCount = 3, delta = -1))
        assertEquals(1, TvUtil.stepGroupIndex(currentIndex = 0, groupCount = 3, delta = 1))
        assertEquals(2, TvUtil.stepGroupIndex(currentIndex = 2, groupCount = 3, delta = 1))
    }

    @Test
    fun doubleColumnDefaultFollowsTvModeUntilUserOverridesIt() {
        assertEquals(true, TvUtil.resolveDoubleColumnDefault(storedSetting = null, isTvDevice = true))
        assertEquals(false, TvUtil.resolveDoubleColumnDefault(storedSetting = null, isTvDevice = false))
        assertEquals(false, TvUtil.resolveDoubleColumnDefault(storedSetting = false, isTvDevice = true))
        assertEquals(true, TvUtil.resolveDoubleColumnDefault(storedSetting = true, isTvDevice = false))
    }
}
