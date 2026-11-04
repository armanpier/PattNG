package com.v2ray.ang.ui.compose

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalIsTv = compositionLocalOf { false }

@Composable
fun Modifier.tvFocusableItem(
    shape: Shape = RoundedCornerShape(10.dp),
    borderWidth: Dp = 2.dp,
    focusedBorderColor: Color = MaterialTheme.colorScheme.secondary,
    focusedContainerColor: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.14f),
    onContextKey: (() -> Unit)? = null,
): Modifier {
    var isFocused by remember { mutableStateOf(false) }
    return this
        .onFocusChanged { state -> isFocused = state.isFocused }
        .then(
            if (onContextKey != null) {
                Modifier.onPreviewKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown) {
                        when (event.nativeKeyEvent.keyCode) {
                            KeyEvent.KEYCODE_MENU,
                            KeyEvent.KEYCODE_INFO,
                            KeyEvent.KEYCODE_BUTTON_X,
                            KeyEvent.KEYCODE_PROG_BLUE -> {
                                onContextKey()
                                true
                            }
                            else -> false
                        }
                    } else {
                        false
                    }
                }
            } else {
                Modifier
            }
        )
        .clip(shape)
        .then(
            if (isFocused) {
                Modifier
                    .background(focusedContainerColor, shape)
                    .border(borderWidth, focusedBorderColor, shape)
            } else {
                Modifier
            }
        )
}
