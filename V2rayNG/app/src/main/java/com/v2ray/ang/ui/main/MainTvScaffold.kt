package com.v2ray.ang.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.v2ray.ang.R
import com.v2ray.ang.ui.compose.AppDivider
import com.v2ray.ang.ui.compose.colorFabActive
import com.v2ray.ang.ui.compose.colorFabInactiveDark
import com.v2ray.ang.ui.compose.colorFabInactiveLight
import com.v2ray.ang.ui.compose.tvFocusableItem
import com.v2ray.ang.ui.compose.verticalScrollbar

private val tvPrimaryDestinations = listOf(
    MainDestination.Subscriptions,
    MainDestination.PerAppProxy,
    MainDestination.Routing,
    MainDestination.UserAssets,
    MainDestination.Settings,
)

private val tvAllDestinations = tvPrimaryDestinations + listOf(
    MainDestination.Promotion,
    MainDestination.Logcat,
    MainDestination.CheckUpdate,
    MainDestination.BackupRestore,
    MainDestination.About,
)

/**
 * 10-foot Android TV Leanback layout replacing the touch-oriented ModalNavigationDrawer
 * and floating bottom FAB with a persistent D-pad navigable left control rail and
 * overscan-safe server workspace.
 */
@Composable
fun MainTvScaffold(
    displayText: String,
    isRunning: Boolean,
    isDarkTheme: Boolean,
    topBar: @Composable () -> Unit,
    onAction: (MainAction) -> Unit,
    onNavigate: (MainDestination) -> Unit,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        MainTvSideRail(
            displayText = displayText,
            isRunning = isRunning,
            isDarkTheme = isDarkTheme,
            onAction = onAction,
            onNavigate = onNavigate,
        )
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            topBar()
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                content()
            }
        }
    }
}

@Composable
private fun MainTvSideRail(
    displayText: String,
    isRunning: Boolean,
    isDarkTheme: Boolean,
    onAction: (MainAction) -> Unit,
    onNavigate: (MainDestination) -> Unit,
) {
    val railScrollState = rememberScrollState()
    val statusAccent = if (isRunning) {
        colorFabActive
    } else if (isDarkTheme) {
        colorFabInactiveDark
    } else {
        colorFabInactiveLight
    }

    Surface(
        modifier = Modifier
            .width(268.dp)
            .fillMaxHeight(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(railScrollState)
                .verticalScrollbar(railScrollState)
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_pattng_logo),
                    contentDescription = null,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Primary D-Pad Connect / Disconnect Tile
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .tvFocusableItem(
                        shape = RoundedCornerShape(14.dp),
                        onContextKey = { onAction(MainAction.TestCurrentServer) }
                    )
                    .clickable { onAction(MainAction.ToggleService) },
                shape = RoundedCornerShape(14.dp),
                color = if (isRunning) {
                    colorFabActive.copy(alpha = 0.16f)
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHigh
                },
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isRunning) colorFabActive else MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(statusAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = if (isRunning) {
                                painterResource(R.drawable.ic_stop_24dp)
                            } else {
                                painterResource(R.drawable.ic_play_24dp)
                            },
                            contentDescription = stringResource(
                                if (isRunning) R.string.acc_stop else R.string.acc_start
                            ),
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(
                                if (isRunning) R.string.acc_stop else R.string.acc_start
                            ),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = displayText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.semantics {
                                contentDescription = displayText
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick TV Action Strip (Test Current Ping, Real Ping All, Locate Selected, Update Subs)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TvQuickActionIconButton(
                    iconRes = R.drawable.ic_flash_on_24dp,
                    contentDescription = stringResource(R.string.connection_test_pending),
                    onClick = { onAction(MainAction.TestCurrentServer) }
                )
                TvQuickActionIconButton(
                    iconRes = R.drawable.ic_fab_check,
                    contentDescription = stringResource(R.string.title_real_ping_all_server),
                    onClick = { onAction(MainAction.TestRealAllServers) }
                )
                TvQuickActionIconButton(
                    iconRes = R.drawable.ic_outline_filter_alt_24,
                    contentDescription = stringResource(R.string.title_sort_by_test_results),
                    onClick = { onAction(MainAction.SortByTestResults) }
                )
                TvQuickActionIconButton(
                    iconRes = R.drawable.ic_refresh_24dp,
                    contentDescription = stringResource(R.string.title_sub_update),
                    onClick = { onAction(MainAction.UpdateSubscriptions) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            AppDivider()
            Spacer(modifier = Modifier.height(6.dp))

            tvAllDestinations.forEachIndexed { index, item ->
                if (index == tvPrimaryDestinations.size) {
                    Spacer(modifier = Modifier.height(4.dp))
                    AppDivider()
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .tvFocusableItem(shape = RoundedCornerShape(10.dp))
                        .clickable { onNavigate(item) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(item.iconRes),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = stringResource(item.labelRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun TvQuickActionIconButton(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .tvFocusableItem(shape = RoundedCornerShape(10.dp))
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(22.dp)
        )
    }
}
