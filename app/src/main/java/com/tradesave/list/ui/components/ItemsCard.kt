package com.tradesave.list.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tradesave.list.ui.theme.LocalTradeSaveColors
import com.tradesave.list.ui.theme.Padding16
import com.tradesave.list.ui.theme.Radius16

@Composable
fun ItemsCard(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = LocalTradeSaveColors.current
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Radius16),
        colors = CardDefaults.cardColors(containerColor = colors.upperSurface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding16)
        ) {
            content.invoke(this)
        }
    }
}