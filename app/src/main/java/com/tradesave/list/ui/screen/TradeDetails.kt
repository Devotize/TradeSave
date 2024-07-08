package com.tradesave.list.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tradesave.list.R
import com.tradesave.list.domain.data.TradePoint
import com.tradesave.list.domain.data.TradeSave
import com.tradesave.list.ui.components.TradeCardMainSection
import com.tradesave.list.ui.components.TradeSaveDivider
import com.tradesave.list.ui.components.TradeSingleBottomTextSection
import com.tradesave.list.ui.theme.LocalTradeSaveColors
import com.tradesave.list.ui.theme.LocalTradeSaveTypography
import com.tradesave.list.ui.theme.Padding12
import com.tradesave.list.ui.theme.Padding14
import com.tradesave.list.ui.theme.Padding16
import com.tradesave.list.ui.theme.Padding24
import com.tradesave.list.ui.theme.Padding38
import com.tradesave.list.ui.theme.Padding8
import com.tradesave.list.ui.theme.Radius12
import com.tradesave.list.ui.theme.Radius20
import com.tradesave.list.utils.TradeSaveDateFormatter
import com.tradesave.list.utils.TradeSaveTimeFormatter
import com.tradesave.list.utils.toLocalString

@Composable
fun TradeDetailsScreen(
    modifier: Modifier,
    tradeSave: TradeSave,
    goBack: () -> Unit,
    onEditClick: (id: String) -> Unit,
    onDeleteClick: (id: String) -> Unit,
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    Box(modifier = modifier.padding(Padding16)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    modifier = modifier,
                    shape = RoundedCornerShape(Radius20),
                    colors = CardDefaults.cardColors(containerColor = colors.upperSurface)
                ) {
                    TradeCardMainSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Padding16),
                        tradeSave = tradeSave,
                    )
                }
                Text(
                    modifier = Modifier.padding(top = Padding24),
                    text = stringResource(R.string.entry_text),
                    style = typography.b2Medium,
                    color = colors.grey,
                )
                Card(
                    modifier = Modifier.padding(top = Padding12),
                    shape = RoundedCornerShape(Radius20),
                    colors = CardDefaults.cardColors(containerColor = colors.upperSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(Padding16)
                    ) {
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
                                tradeSave.entry?.date?.format(TradeSaveDateFormatter)
                            }
                            val time = remember {
                                tradeSave.entry?.time?.format(TradeSaveTimeFormatter)
                            }
                            Text(
                                text = "${date.orEmpty()}, ${time.orEmpty()}",
                                style = typography.caption,
                                color = colors.grey
                            )
                        }
                        TradeSaveDivider(
                            modifier = Modifier.padding(
                                top = Padding8,
                                bottom = Padding16
                            )
                        )
                        tradeSave.entry?.let {
                            TradePointBottomSection(
                                modifier = Modifier.fillMaxWidth(),
                                tradePoint = it,
                            )
                        }
                    }
                }
                tradeSave.exit?.let { exit ->
                    Text(
                        modifier = Modifier.padding(top = Padding24),
                        text = stringResource(R.string.exit_text),
                        style = typography.b2Medium,
                        color = colors.grey,
                    )
                    Card(
                        modifier = Modifier.padding(top = Padding12),
                        shape = RoundedCornerShape(Radius20),
                        colors = CardDefaults.cardColors(containerColor = colors.upperSurface)
                    ) {
                        Column(
                            modifier = Modifier.padding(Padding16)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = stringResource(R.string.last_update),
                                    style = typography.caption,
                                    color = colors.grey
                                )
                                Text(
                                    text = "${exit.date.format(TradeSaveDateFormatter)}, ${
                                        exit.time.format(
                                            TradeSaveTimeFormatter
                                        )
                                    }",
                                    style = typography.caption,
                                    color = colors.grey
                                )
                            }
                            TradeSaveDivider(
                                modifier = Modifier.padding(
                                    top = Padding8,
                                    bottom = Padding16
                                )
                            )
                            TradePointBottomSection(
                                modifier = Modifier.fillMaxWidth(),
                                tradePoint = exit,
                            )
                        }
                    }
                }
                tradeSave.note?.let { note ->
                    Text(
                        modifier = Modifier.padding(top = Padding24),
                        text = stringResource(R.string.note_text),
                        style = typography.b2Medium,
                        color = colors.grey,
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(Radius20),
                        colors = CardDefaults.cardColors(containerColor = colors.upperSurface)
                    ) {
                        Text(
                            modifier = Modifier.padding(Padding16),
                            text = note,
                            style = typography.b2Regular,
                            color = colors.grey,
                        )
                    }
                }
                Spacer(Modifier.height(Padding38 * 3))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(Padding12)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = colors.altSurface),
                shape = RoundedCornerShape(Radius12),
                contentPadding = PaddingValues(Padding14),
                onClick = {
                    onEditClick.invoke(tradeSave.id)
                }
            ) {
                Text(
                    text = stringResource(R.string.edit_text),
                    style = typography.b2Medium,
                    color = colors.primary,
                )
            }
            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = colors.altSurface),
                shape = RoundedCornerShape(Radius12),
                contentPadding = PaddingValues(Padding14),
                onClick = {
                    onDeleteClick.invoke(tradeSave.id)
                }
            ) {
                Text(
                    text = stringResource(R.string.delete_text),
                    style = typography.b2Medium,
                    color = colors.red,
                )
            }
        }
    }
}

@Composable
private fun TradePointBottomSection(
    modifier: Modifier,
    tradePoint: TradePoint,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tradePoint.price?.let { price ->
            TradeSingleBottomTextSection(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.price_text),
                value = price.toLocalString(),
            )
        }
        tradePoint.quantity?.let {
            TradeSingleBottomTextSection(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.qty_text),
                value = tradePoint.quantity.toLocalString(),
            )
        }
        Spacer(Modifier.weight(1f))
    }
}