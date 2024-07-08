package com.tradesave.list.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tradesave.list.R
import com.tradesave.list.domain.data.DashboardInfo
import com.tradesave.list.domain.data.TradeSave
import com.tradesave.list.ui.components.TradeCardFull
import com.tradesave.list.ui.theme.LocalTradeSaveColors
import com.tradesave.list.ui.theme.LocalTradeSaveTypography
import com.tradesave.list.ui.theme.Padding12
import com.tradesave.list.ui.theme.Padding16
import com.tradesave.list.ui.theme.Padding24
import com.tradesave.list.ui.theme.Padding32
import com.tradesave.list.ui.theme.Padding38
import com.tradesave.list.ui.theme.Padding8
import com.tradesave.list.ui.theme.Radius12
import com.tradesave.list.utils.PercentsFormat
import com.tradesave.list.utils.toLocalString
import java.math.BigDecimal
import kotlin.math.abs

@Composable
fun DashboardScreen(
    modifier: Modifier,
    info: DashboardInfo,
    savedTrades: List<TradeSave>,
    onAddTradeClick: () -> Unit,
    onTradeClick: (id: String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    Box(modifier = modifier.padding(horizontal = Padding16)) {
        Column(modifier = Modifier.padding(top = Padding32)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.dashboard_text),
                    style = typography.largeTitle,
                    color = colors.textWhite,
                )
                IconButton(
                    onClick = onSettingsClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = colors.textWhite,
                    )
                }

            }
            Info(modifier = Modifier.padding(top = Padding16), info)
            LazyColumn(
                modifier = Modifier.padding(top = Padding24),
                verticalArrangement = Arrangement.spacedBy(Padding16, Alignment.Top)
            ) {
                items(savedTrades) {
                    TradeCardFull(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        tradeSave = it,
                        onClick = {
                            onTradeClick.invoke(it.id)
                        }
                    )
                }
                item {
                    Spacer(Modifier.height(Padding38 * 2))
                }
            }
        }
        if (savedTrades.isEmpty()) {
            EmptyBlock(modifier = Modifier.align(Alignment.Center))
        }
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = Padding16),
            onClick = {
                onAddTradeClick.invoke()
            },
            contentPadding = PaddingValues(Padding12),
            shape = RoundedCornerShape(Radius12),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = colors.textWhite
            )
            Text(
                modifier = Modifier.padding(start = Padding8),
                text = stringResource(R.string.add_new_trade_text),
                style = typography.b1Medium,
                color = colors.textWhite
            )
        }
    }
}

@Composable
private fun Info(
    modifier: Modifier,
    info: DashboardInfo,
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.net_profit_text),
            style = typography.b2Medium,
            color = colors.grey,
        )
        Spacer(Modifier.height(Padding8))
        val sign = remember {
            when {
                info.todayPercents > 0.0 -> "+"
                info.todayPercents < 0.0 -> "-"
                else -> ""
            }
        }
        val overallText =
            if (info.overall != BigDecimal.ZERO) {
                info.overall.toLocalString()
            } else {
                PercentsFormat.format(info.overall)
            }
        Text(
            text = "${sign}${overallText} USD",
            style = typography.h2Bold,
            color = colors.textWhite
        )
        Spacer(Modifier.height(Padding8))
        val statusColor =
            when {
                info.todayPercents > 0.0 -> colors.green
                info.todayPercents < 0.0 -> colors.red
                else -> colors.grey
            }

        val percentsText =
            PercentsFormat.format((abs(info.todayPercents)))
        Row {
            Text(
                text = "${percentsText}%",
                style = typography.b2Regular,
                color = statusColor
            )
            Text(
                text = " ${stringResource(R.string.today_text)}",
                style = typography.b2Regular,
                color = colors.grey
            )
        }

    }
}

@Composable
private fun EmptyBlock(modifier: Modifier) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_box_trades),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colors.grey)
        )
        Text(
            modifier = Modifier.padding(top = Padding8),
            text = stringResource(R.string.no_trades_text),
            style = typography.b1Medium,
            color = colors.textWhite
        )
    }
}