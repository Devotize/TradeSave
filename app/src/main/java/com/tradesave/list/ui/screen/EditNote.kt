package com.tradesave.list.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.tradesave.list.R
import com.tradesave.list.controller.ChangeNote
import com.tradesave.list.controller.Event
import com.tradesave.list.ui.theme.InputDefaultWidth
import com.tradesave.list.ui.theme.LocalTradeSaveColors
import com.tradesave.list.ui.theme.LocalTradeSaveTypography
import com.tradesave.list.ui.theme.Padding12
import com.tradesave.list.ui.theme.Padding14
import com.tradesave.list.ui.theme.Padding16
import com.tradesave.list.ui.theme.Padding8
import com.tradesave.list.ui.theme.Radius12
import com.tradesave.list.ui.theme.Radius16
import com.tradesave.list.utils.clickableNoRipple

@Composable
fun EditNoteScreen(
    modifier: Modifier,
    note: String,
    goBack: () -> Unit,
    sendEvent: (event: Event) -> Unit
) {
    val typography = LocalTradeSaveTypography.current
    val colors = LocalTradeSaveColors.current
    val focusRequester = remember { FocusRequester() }
    Box(
        modifier = modifier,
    ) {
        var noteText by remember { mutableStateOf(note) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding16)
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
                modifier = Modifier.padding(top = Padding16),
                text = stringResource(R.string.note_text),
                style = typography.b2Medium,
                color = colors.grey,
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Padding12),
                shape = RoundedCornerShape(
                    Radius16
                ),
                colors = CardDefaults.cardColors(containerColor = colors.upperSurface),
            ) {
                val focusManager = LocalFocusManager.current

                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Padding16)
                        .widthIn(max = InputDefaultWidth)
                        .focusRequester(focusRequester),
                    value = noteText,
                    onValueChange = {
                        noteText = it.take(900)
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    textStyle = typography.b1Medium.copy(
                        color = colors.textWhite,
                    ),
                    cursorBrush = SolidColor(colors.primary)
                ) {
                    it.invoke()
                }
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        }
        Button(
            modifier = Modifier
                .imePadding()
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(Padding16),
            onClick = {
                sendEvent.invoke(ChangeNote(noteText.trim().ifEmpty { null }))
                goBack.invoke()
            },
            contentPadding = PaddingValues(Padding14),
            shape = RoundedCornerShape(Radius12),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                disabledContainerColor = colors.altSurface
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(start = Padding8),
                text = stringResource(R.string.save_text),
                style = typography.b1Medium,
                color = colors.textWhite
            )
        }
    }
}