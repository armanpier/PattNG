package com.v2ray.ang.util

import android.app.UiModeManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.view.KeyEvent

enum class TvRemoteAction {
    ToggleService,
    TestCurrentPing,
    PreviousGroup,
    NextGroup,
    MoveToBack,
}

object TvUtil {
    fun isTv(context: Context): Boolean {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
        if (uiModeManager?.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
            return true
        }
        val pm = context.packageManager
        return pm.hasSystemFeature(PackageManager.FEATURE_LEANBACK) ||
            pm.hasSystemFeature(PackageManager.FEATURE_TELEVISION)
    }

    fun resolveDoubleColumnDefault(storedSetting: Boolean?, isTvDevice: Boolean): Boolean =
        storedSetting ?: isTvDevice

    fun resolveRemoteKeyAction(keyCode: Int, isSearchActive: Boolean = false): TvRemoteAction? =
        when (keyCode) {
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
            KeyEvent.KEYCODE_MEDIA_PLAY,
            KeyEvent.KEYCODE_MEDIA_STOP,
            KeyEvent.KEYCODE_BUTTON_Y,
            KeyEvent.KEYCODE_PROG_GREEN -> TvRemoteAction.ToggleService

            KeyEvent.KEYCODE_CAPTIONS,
            KeyEvent.KEYCODE_PROG_YELLOW -> TvRemoteAction.TestCurrentPing

            KeyEvent.KEYCODE_PAGE_UP,
            KeyEvent.KEYCODE_CHANNEL_UP,
            KeyEvent.KEYCODE_BUTTON_L1 -> if (isSearchActive) null else TvRemoteAction.PreviousGroup

            KeyEvent.KEYCODE_PAGE_DOWN,
            KeyEvent.KEYCODE_CHANNEL_DOWN,
            KeyEvent.KEYCODE_BUTTON_R1 -> if (isSearchActive) null else TvRemoteAction.NextGroup

            KeyEvent.KEYCODE_BUTTON_B -> if (isSearchActive) null else TvRemoteAction.MoveToBack

            else -> null
        }

    fun stepGroupIndex(currentIndex: Int, groupCount: Int, delta: Int): Int {
        if (groupCount <= 0) return 0
        return (currentIndex + delta).coerceIn(0, groupCount - 1)
    }
}
