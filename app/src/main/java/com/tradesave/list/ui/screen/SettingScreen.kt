package com.tradesave.list.ui.screen

import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tradesave.list.R
import com.tradesave.list.controller.DeleteAllTrades
import com.tradesave.list.controller.Event
import com.tradesave.list.controller.WriteReview
import com.tradesave.list.ui.components.TradeSaveDivider
import com.tradesave.list.ui.theme.LocalTradeSaveColors
import com.tradesave.list.ui.theme.LocalTradeSaveTypography
import com.tradesave.list.ui.theme.Padding10
import com.tradesave.list.ui.theme.Padding16
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    modifier: Modifier,
    traderId: String,
    goBack: () -> Unit,
    sendEvent: (event: Event) -> Unit,
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        content = { contentPadding ->
            Column(
                modifier = modifier
                    .background(colors.background)
                    .padding(contentPadding)
                    .padding(horizontal = Padding16)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        modifier = Modifier,
                        onClick = {
                            goBack.invoke()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = colors.textWhite,
                            contentDescription = null,
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = Padding16),
                        text = stringResource(R.string.settings_text),
                        style = typography.b1Medium,
                        color = colors.textWhite,
                    )
                }
                SettingsItem(
                    modifier = Modifier.fillMaxWidth(),
                    icon = painterResource(R.drawable.ic_profile),
                    title = stringResource(R.string.trader_text),
                    value = traderId,
                )
                TradeSaveDivider(Modifier.padding(vertical = Padding10))
                SettingsItem(
                    modifier = Modifier.fillMaxWidth(),
                    icon = painterResource(R.drawable.ic_mobile),
                    title = stringResource(R.string.app_version_text),
                    value = try {
                        context.packageManager
                            .getPackageInfo(context.packageName, 0).versionName
                    } catch (e: PackageManager.NameNotFoundException) {
                        "1.0"
                    },
                )
                TradeSaveDivider(Modifier.padding(vertical = Padding10))
                SettingsItem(
                    modifier = Modifier.fillMaxWidth(),
                    icon = painterResource(R.drawable.ic_review),
                    title = stringResource(R.string.write_review_text),
                    hasTrailingIcon = true,
                    value = null,
                    onClick = {
                        sendEvent.invoke(WriteReview)
                    }
                )
                TradeSaveDivider(Modifier.padding(vertical = Padding10))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            sendEvent.invoke(DeleteAllTrades)
                            scope.launch {
                                snackbarHostState.showSnackbar(context.getString(R.string.all_stats_deleted_text))
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = colors.red,
                    )
                    Text(
                        modifier = Modifier.padding(Padding16),
                        text = stringResource(R.string.delete_all_trades_text),
                        style = typography.b1Medium,
                        color = colors.red,
                    )
                }
            }
        }
    )

}

@Composable
private fun SettingsItem(
    modifier: Modifier,
    icon: Painter,
    title: String,
    value: String?,
    hasTrailingIcon: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    Row(
        modifier = modifier
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = colors.primary,
        )
        Column(
            modifier = Modifier
                .padding(Padding16)
        ) {
            Text(
                text = title,
                style = typography.b1Medium,
                color = colors.textWhite,
            )
            value?.let {
                Text(
                    text = value,
                    style = typography.b2Regular,
                    color = colors.grey,
                )
            }
        }
        if (hasTrailingIcon) {
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.grey
            )
        }
    }
}