package com.tradesave.list.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tradesave.list.R
import com.tradesave.list.controller.AddExit
import com.tradesave.list.controller.ChangeAssetName
import com.tradesave.list.controller.ChangeEntryDate
import com.tradesave.list.controller.ChangeEntryFees
import com.tradesave.list.controller.ChangeEntryPrice
import com.tradesave.list.controller.ChangeEntryQuantity
import com.tradesave.list.controller.ChangeEntryTime
import com.tradesave.list.controller.ChangeExitDate
import com.tradesave.list.controller.ChangeExitFees
import com.tradesave.list.controller.ChangeExitPrice
import com.tradesave.list.controller.ChangeExitQuantity
import com.tradesave.list.controller.ChangeExitTime
import com.tradesave.list.controller.ChangePositionType
import com.tradesave.list.controller.Event
import com.tradesave.list.controller.RemoveExit
import com.tradesave.list.controller.SaveTrade
import com.tradesave.list.domain.data.PositionType
import com.tradesave.list.domain.data.TradePoint
import com.tradesave.list.domain.data.TradeSave
import com.tradesave.list.ui.components.ItemsCard
import com.tradesave.list.ui.components.TimePickerDialog
import com.tradesave.list.ui.components.TradeSaveDivider
import com.tradesave.list.ui.theme.InputDefaultWidth
import com.tradesave.list.ui.theme.LocalTradeSaveColors
import com.tradesave.list.ui.theme.LocalTradeSaveTypography
import com.tradesave.list.ui.theme.Padding12
import com.tradesave.list.ui.theme.Padding14
import com.tradesave.list.ui.theme.Padding16
import com.tradesave.list.ui.theme.Padding2
import com.tradesave.list.ui.theme.Padding24
import com.tradesave.list.ui.theme.Padding32
import com.tradesave.list.ui.theme.Padding38
import com.tradesave.list.ui.theme.Padding4
import com.tradesave.list.ui.theme.Padding8
import com.tradesave.list.ui.theme.Radius10
import com.tradesave.list.ui.theme.Radius12
import com.tradesave.list.ui.theme.Radius16
import com.tradesave.list.ui.theme.Radius8
import com.tradesave.list.utils.Keyboard
import com.tradesave.list.utils.TradeSaveDateFormatter
import com.tradesave.list.utils.TradeSaveTimeFormatter
import com.tradesave.list.utils.clickableNoRipple
import com.tradesave.list.utils.keyboardAsState
import com.tradesave.list.utils.onFocusEnd
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditTrade(
    modifier: Modifier,
    goBack: () -> Unit,
    tradeSave: TradeSave?,
    sendEvent: (Event) -> Unit,
    goToEditNote: () -> Unit,
) {
    Box(modifier = modifier) {
        val typography = LocalTradeSaveTypography.current
        val colors = LocalTradeSaveColors.current
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            IconButton(
                modifier = Modifier.padding(start = Padding16),
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
            Spacer(Modifier.height(16.dp))
            tradeSave?.let { trade ->
                Column(
                    modifier = Modifier
                        .animateContentSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Padding16)
                ) {
                    PositionWithAssetSection(
                        modifier = Modifier.fillMaxWidth(),
                        positionType = trade.positionType,
                        assetName = trade.assetName,
                        sendEvent = sendEvent,
                    )

                    trade.entry?.let { entry ->
                        Text(
                            modifier = Modifier.padding(top = Padding24),
                            text = stringResource(R.string.entry_text),
                            style = typography.b2Medium,
                            color = colors.grey,
                        )
                        TradePointView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Padding12),
                            tradePoint = entry,
                            onChangeDate = {
                                sendEvent.invoke(ChangeEntryDate(it))
                            },
                            onChangeTime = {
                                sendEvent.invoke(ChangeEntryTime(it))
                            },
                            onChangePrice = {
                                sendEvent.invoke(ChangeEntryPrice(it))
                            },
                            onChangeFees = {
                                sendEvent.invoke(ChangeEntryFees(it))
                            },
                            onChangeQty = {
                                sendEvent.invoke(ChangeEntryQuantity(it))
                            }
                        )
                    }

                    if (trade.exit == null) {
                        Button(
                            modifier = Modifier
                                .padding(top = Padding16)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(Radius8),
                            onClick = {
                                sendEvent.invoke(AddExit)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.upperSurface,
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.add_exit_text),
                                style = typography.b2Medium,
                                color = colors.primary
                            )
                        }
                    }

                    trade.exit?.let { exit ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier.padding(top = Padding24),
                                text = stringResource(R.string.exit_text),
                                style = typography.b2Medium,
                                color = colors.grey,
                            )
                            Spacer(Modifier.weight(1f))
                            TextButton(
                                onClick = {
                                    sendEvent.invoke(RemoveExit)
                                }
                            ) {
                                Text(
                                    modifier = Modifier.padding(top = Padding24),
                                    text = stringResource(R.string.remove_text),
                                    style = typography.b2Medium,
                                    color = colors.red,
                                )
                            }
                        }
                        TradePointView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Padding12),
                            tradePoint = exit,
                            onChangeDate = {
                                sendEvent.invoke(ChangeExitDate(it))
                            },
                            onChangeTime = {
                                sendEvent.invoke(ChangeExitTime(it))
                            },
                            onChangePrice = {
                                sendEvent.invoke(ChangeExitPrice(it))
                            },
                            onChangeFees = {
                                sendEvent.invoke(ChangeExitFees(it))
                            },
                            onChangeQty = {
                                sendEvent.invoke(ChangeExitQuantity(it))
                            }
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Padding24),
                        shape = RoundedCornerShape(
                            topEnd = Radius16,
                            topStart = Radius16,
                            bottomEnd = if (trade.note == null) Radius16 else 0.dp,
                            bottomStart = if (trade.note == null) Radius16 else 0.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = colors.upperSurface),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Padding16),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.note_text),
                                style = typography.b1Medium,
                                color = colors.textWhite,
                            )
                            TextButton(
                                onClick = goToEditNote
                            ) {
                                Text(
                                    text = if (trade.note == null) {
                                        stringResource(R.string.add_text)
                                    } else {
                                        stringResource(R.string.edit_text)
                                    },
                                    style = typography.b1Medium,
                                    color = colors.primary,
                                )
                            }
                        }
                    }
                    trade.note?.let { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Padding2),
                            shape = RoundedCornerShape(
                                bottomEnd = Radius16,
                                bottomStart = Radius16,
                            ),
                            colors = CardDefaults.cardColors(containerColor = colors.upperSurface),
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Padding16),
                                text = note,
                                style = typography.b1Regular,
                                color = colors.grey,
                            )
                        }
                    }
                    Spacer(Modifier.height(Padding38 * 2))
                }
            }
        }
        val keyboard by keyboardAsState()
        if (keyboard == Keyboard.Closed) {
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(Padding16),
                onClick = {
                    sendEvent.invoke(SaveTrade)
                    goBack.invoke()
                },
                enabled = tradeSave?.isValidForSave() == true,
                contentPadding = PaddingValues(Padding14),
                shape = RoundedCornerShape(Radius12),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    disabledContainerColor = colors.altSurface
                )
            ) {
                Text(
                    modifier = Modifier
                        .alpha(
                            if (tradeSave?.isValidForSave() == true) 1f else .4f
                        )
                        .padding(start = Padding8),
                    text = stringResource(R.string.save_text),
                    style = typography.b1Medium,
                    color = colors.textWhite
                )
            }
        }
    }
}

@Composable
private fun PositionWithAssetSection(
    modifier: Modifier,
    positionType: PositionType,
    assetName: String,
    sendEvent: (Event) -> Unit,
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    ItemsCard(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.position_type_text),
                style = typography.b1Medium,
                color = colors.textWhite,
            )
            TabRow(
                modifier = Modifier
                    .width(100.dp)
                    .clip(RoundedCornerShape(Radius10))
                    .border(
                        width = 1.dp,
                        color = colors.textWhite.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(Radius10)
                    ),
                selectedTabIndex = PositionType.entries.indexOf(positionType),
                containerColor = colors.background,
                indicator = {

                },
                divider = {

                },
                tabs = {
                    PositionType.entries.forEach { type ->
                        Tab(
                            modifier = Modifier
                                .padding(Padding2)
                                .clip(RoundedCornerShape(Radius10)),
                            selected = positionType == PositionType.Long,
                            onClick = {
                                sendEvent.invoke(ChangePositionType(type))
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = if (type == positionType) colors.altSurface else Color.Transparent,
                                        shape = RoundedCornerShape(Radius10)
                                    ),
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = Padding8)
                                        .align(Alignment.Center),
                                    text = stringResource(type.stringRes),
                                    style = typography.caption,
                                    color = if (type == positionType) colors.textWhite else colors.grey
                                )
                            }
                        }
                    }
                }
            )
        }
        TradeSaveDivider(modifier = Modifier.padding(vertical = Padding12))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Padding4),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.asset_text),
                style = typography.b1Medium,
                color = colors.textWhite,
            )
            val focusManager = LocalFocusManager.current
            BasicTextField(
                modifier = Modifier.widthIn(max = InputDefaultWidth),
                value = assetName,
                onValueChange = {
                    sendEvent.invoke(ChangeAssetName(it))
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                textStyle = typography.b1Medium.copy(
                    color = colors.textWhite,
                    textAlign = TextAlign.End
                ),
                cursorBrush = SolidColor(colors.primary)
            ) {
                if (assetName.isEmpty()) {
                    Text(
                        text = stringResource(R.string.asset_name_hint),
                        style = typography.b1Medium,
                        color = colors.grey
                    )
                } else {
                    it.invoke()
                }
            }
        }
    }
}

@Composable
private fun TradePointView(
    modifier: Modifier,
    tradePoint: TradePoint,
    onChangeDate: (LocalDate) -> Unit,
    onChangeTime: (LocalTime) -> Unit,
    onChangePrice: (BigDecimal?) -> Unit,
    onChangeQty: (BigDecimal?) -> Unit,
    onChangeFees: (BigDecimal?) -> Unit,
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    ItemsCard(modifier = modifier) {
        // date picker
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            var calendarFlag by remember { mutableStateOf(false) }
            Text(
                text = stringResource(R.string.date_text),
                style = typography.b1Medium,
                color = colors.textWhite,
            )
            if (calendarFlag) {
                TradeSaveDatePicker(
                    modifier = Modifier.fillMaxSize(),
                    onDismiss = {
                        calendarFlag = false
                    },
                    onDatePicked = {
                        calendarFlag = false
                        onChangeDate.invoke(it)
                    }
                )
            }
            Text(
                modifier = Modifier.clickableNoRipple {
                    calendarFlag = true
                },
                text = tradePoint.date.format(TradeSaveDateFormatter),
                style = typography.b1Medium,
                color = colors.primary
            )
        }
        TradeSaveDivider(modifier = Modifier.padding(vertical = Padding12))
        // time picker
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            var timeFlag by remember { mutableStateOf(false) }
            Text(
                text = stringResource(R.string.time_text),
                style = typography.b1Medium,
                color = colors.textWhite,
            )
            if (timeFlag) {
                TradeSaveTimePicker(
                    onDismiss = {
                        timeFlag = false
                    },
                    onTimeSelected = {
                        timeFlag = false
                        onChangeTime.invoke(it)
                    },
                )
            }
            Text(
                modifier = Modifier.clickableNoRipple {
                    timeFlag = true
                },
                text = tradePoint.time.format(TradeSaveTimeFormatter),
                style = typography.b1Medium,
                color = colors.primary
            )
        }
        // price
        TradeSaveDivider(modifier = Modifier.padding(vertical = Padding12))
        TradePointInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Padding4),
            name = stringResource(R.string.price_text),
            value = tradePoint.price?.toString().orEmpty(),
            hint = "0.00 USD",
            onDone = {
                onChangePrice.invoke(it)
            },
            hasCurrency = true
        )
        TradeSaveDivider(Modifier.padding(vertical = Padding12))
        // qty
        TradePointInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Padding4),
            name = stringResource(R.string.quantity_text),
            value = tradePoint.quantity?.toString().orEmpty(),
            hint = "0.00",
            onDone = {
                onChangeQty.invoke(it)
            },
        )
        TradeSaveDivider(Modifier.padding(vertical = Padding12))
        // fees
        TradePointInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Padding4),
            name = stringResource(R.string.fees_text),
            value = tradePoint.fees?.toString().orEmpty(),
            hint = "0.00 USD",
            onDone = {
                onChangeFees.invoke(it)
            },
            hasCurrency = true
        )
    }
}

@Composable
private fun TradePointInputField(
    modifier: Modifier,
    name: String,
    value: String,
    hint: String,
    onDone: (newValue: BigDecimal?) -> Unit,
    hasCurrency: Boolean = false,
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = typography.b1Medium,
            color = colors.textWhite,
        )
        Spacer(Modifier.weight(1f))
        var textValue by remember { mutableStateOf(value) }
        val focusManager = LocalFocusManager.current
        BasicTextField(
            modifier = Modifier
                .onFocusEnd {
                    try {
                        val newValue =
                            if (textValue.isEmpty()) {
                                null
                            } else {
                                BigDecimal(
                                    textValue.replace(
                                        ',',
                                        '.',
                                    )
                                )
                            }
                        onDone.invoke(newValue)
                        textValue = newValue
                            ?.toString()
                            .orEmpty()
                    } catch (e: NumberFormatException) {
                        textValue = value
                        e.printStackTrace()
                    }
                }
                .widthIn(max = InputDefaultWidth),
            value = textValue,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            onValueChange = {
                textValue = it
            },
            singleLine = true,
            textStyle = typography.b1Medium.copy(
                color = colors.textWhite,
                textAlign = TextAlign.End
            ),
            cursorBrush = SolidColor(colors.primary)
        ) {
            if (textValue.isEmpty()) {
                Text(
                    text = hint,
                    style = typography.b1Medium,
                    color = colors.grey
                )
            } else {
                it.invoke()
            }
        }
        if (textValue.isNotEmpty() && hasCurrency) {
            Text(
                text = " USD",
                style = typography.b1Medium,
                color = colors.textWhite
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TradeSaveDatePicker(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onDatePicked: (LocalDate) -> Unit,
) {
    val state = rememberDatePickerState()
    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss.invoke()
                }
            ) {
                Text(text = stringResource(R.string.dismiss_text))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss.invoke()
                    state.selectedDateMillis?.let { timestamp ->
                        onDatePicked.invoke(
                            LocalDate.ofInstant(
                                Instant.ofEpochMilli(timestamp),
                                ZoneId.systemDefault()
                            )
                        )
                    }

                }
            ) {
                Text(text = stringResource(R.string.ok_text))
            }
        }
    ) {
        DatePicker(
            state = state,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TradeSaveTimePicker(
    onDismiss: () -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val state = rememberTimePickerState()
    val showingPicker = remember { mutableStateOf(true) }
    TimePickerDialog(
        onCancel = {
            onDismiss.invoke()
        },
        onConfirm = {
            onDismiss.invoke()
            onTimeSelected.invoke(
                LocalTime.of(state.hour, state.minute)
            )
        },
    ) {
        if (showingPicker.value && configuration.screenHeightDp > 400) {
            TimePicker(state = state)
        } else {
            TimeInput(state = state)
        }
    }

}


