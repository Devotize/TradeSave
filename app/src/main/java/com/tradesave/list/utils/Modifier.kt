package com.tradesave.list.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged

fun Modifier.clickableNoRipple(
    onClick: () -> Unit
) = this.then(
    Modifier.clickable(
        interactionSource = MutableInteractionSource(),
        indication = null,
        onClick = { onClick() }
    )
)

fun Modifier.onFocusEnd(
    action: () -> Unit
) = composed {
    var hadFocus by remember { mutableStateOf(false) }
    this
        .then(Modifier)
        .onFocusChanged { state ->
            when {
                state.hasFocus -> {
                    hadFocus = true
                }

                !state.hasFocus && hadFocus -> {
                    action.invoke()
                }
            }
        }
}
