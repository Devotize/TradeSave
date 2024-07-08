package com.tradesave.list.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.tradesave.list.ui.theme.LocalTradeSaveColors

@Composable
fun TradeSaveDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth()
            .alpha(.1f),
        thickness = 1.dp,
        color = LocalTradeSaveColors.current.textWhite
    )
}