package com.tradesave.list.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val Background = Color(0xFF02050A)
private val Surface = Color(0xFF0B0D0F)
private val UpperSurface = Color(0xFF13161A)
private val AltSurface = Color(0xFF25292E)
private val Primary = Color(0xFF2B83FF)
private val Grey = Color(0xFF919DA9)
private val Green = Color(0xFF1ED77D)
private val Red = Color(0xFFBE3232)

class TradeSaveColors(
    val background: Color = Background,
    val surface: Color = Surface,
    val upperSurface: Color = UpperSurface,
    val altSurface: Color = AltSurface,
    val primary: Color = Primary,
    val grey: Color = Grey,
    val green: Color = Green,
    val red: Color = Red,
    val textWhite: Color = Color.White,
)

val LocalTradeSaveColors = staticCompositionLocalOf { TradeSaveColors() }