package com.tradesave.list.screens

interface Screen {
    val route: String get() = this::class.java.simpleName

    companion object {
        object Dashboard : Screen
        object EditTrade : Screen
        object Settings : Screen
        object TradeDetails : Screen {
            const val TRADE_ID_KEY = "tradeId"

            override val route: String = "${super.route}/{${TRADE_ID_KEY}}"

            fun createNavRoute(tradeId: String) = "${super.route}/${tradeId}"
        }
        object EditNote : Screen
    }
}