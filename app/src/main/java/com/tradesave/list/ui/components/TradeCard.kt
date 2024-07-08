package com.tradesave.list.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tradesave.list.R
import com.tradesave.list.domain.data.PositionType
import com.tradesave.list.domain.data.Status
import com.tradesave.list.domain.data.TradeSave
import com.tradesave.list.ui.theme.LocalTradeSaveColors
import com.tradesave.list.ui.theme.LocalTradeSaveTypography
import com.tradesave.list.ui.theme.Padding16
import com.tradesave.list.ui.theme.Padding4
import com.tradesave.list.ui.theme.Radius20
import com.tradesave.list.utils.PercentsFormat
import com.tradesave.list.utils.TradeSaveDateFormatter
import com.tradesave.list.utils.TradeSaveTimeFormatter
import com.tradesave.list.utils.toLocalString
import java.math.BigDecimal
import kotlin.math.abs

@Composable
fun TradeCardFull(
    modifier: Modifier,
    tradeSave: TradeSave,
    onClick: (() -> Unit),
) {
    val colors = LocalTradeSaveColors.current
    Card(
        modifier = modifier,
        onClick = { onClick.invoke() },
        shape = RoundedCornerShape(Radius20),
        border = BorderStroke(1.dp, Color.White.copy(alpha = .04f)),
        colors = CardDefaults.cardColors(containerColor = colors.upperSurface)
    ) {
        Column(
            modifier = Modifier.padding(Padding16)
        ) {
            TradeCardHeaderSection(
                modifier = Modifier.fillMaxWidth(),
                tradeSave = tradeSave,
            )
            TradeCardMainSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Padding16),
                tradeSave = tradeSave,
            )
            TradeSaveDivider(Modifier.padding(vertical = Padding16))
            TradeCardBottomSection(modifier = Modifier.fillMaxWidth(), tradeSave = tradeSave)
        }
    }
}

@Composable
fun TradeSingleBottomTextSection(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    Column(modifier = modifier) {
        Text(
            text = title,
            style = typography.caption,
            color = colors.grey,
        )
        Text(
            text = value,
            style = typography.caption,
            color = colors.textWhite,
        )
    }
}

@Composable
fun TradeCardBottomSection(
    modifier: Modifier,
    tradeSave: TradeSave,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tradeSave.entry?.price?.let { price ->
            TradeSingleBottomTextSection(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.entry_text),
                value = price.toLocalString(),
            )
        }
        tradeSave.exit?.price?.let { price ->
            TradeSingleBottomTextSection(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.exit_text),
                value = price.toLocalString(),
            )
        }
        val qty = remember {
            tradeSave.exit?.quantity ?: tradeSave.entry?.quantity
        }
        qty?.let {
            TradeSingleBottomTextSection(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.qty_text),
                value = qty.toLocalString(),
            )
        }
        if (tradeSave.exit?.price == null) {
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
fun TradeCardHeaderSection(
    modifier: Modifier,
    tradeSave: TradeSave,
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.last_update),
            style = typography.caption,
            color = colors.grey
        )
        val date = remember {
            tradeSave.exit?.date?.format(TradeSaveDateFormatter)
                ?: tradeSave.entry?.date?.format(TradeSaveDateFormatter)
        }
        val time = remember {
            tradeSave.exit?.time?.format(TradeSaveTimeFormatter)
                ?: tradeSave.entry?.time?.format(TradeSaveTimeFormatter)
        }
        Text(
            text = "${date.orEmpty()}, ${time.orEmpty()}",
            style = typography.caption,
            color = colors.grey
        )
    }

}

@Composable
fun TradeCardMainSection(
    modifier: Modifier,
    tradeSave: TradeSave,
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .background(color = colors.altSurface, shape = CircleShape),
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(Padding16),
                painter = painterResource(
                    if (tradeSave.positionType == PositionType.Long) {
                        R.drawable.ic_green_arrow
                    } else {
                        R.drawable.ic_red_arrow
                    }
                ),
                tint = if (tradeSave.positionType == PositionType.Long) colors.green else colors.red,
                contentDescription = null
            )
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Padding16),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = tradeSave.assetName,
                    style = typography.h2Bold,
                    color = colors.textWhite,
                )
                Text(
                    text = tradeSave.getEntryValue().toLocalString(),
                    style = typography.h2Bold,
                    color = colors.textWhite,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Padding16, top = Padding4),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(tradeSave.positionType.stringRes),
                    style = typography.b2Medium,
                    color = colors.textWhite,
                )
                val statusColor = remember {
                    when (tradeSave.getStatus()) {
                        Status.Loss -> colors.red
                        Status.Profit -> colors.green
                        Status.Undefined -> colors.grey
                    }
                }
                val sign = remember {
                    when (tradeSave.getStatus()) {
                        Status.Loss -> "-"
                        Status.Profit -> "+"
                        Status.Undefined -> ""
                    }
                }
                val percentsText = remember {
                    tradeSave.getPercents()?.let {
                        PercentsFormat.format(abs(it))
                    } ?: "0"
                }
                val profitText =
                    when(val profit = tradeSave.getProfit()) {
                        BigDecimal.ZERO -> PercentsFormat.format(profit)
                        else -> profit.abs().toLocalString()
                    }
                Text(
                    text = "${sign}${
                        profitText
                    } • $percentsText%",
                    style = typography.h2Bold,
                    color = statusColor,
                )
            }
        }
    }
}