package com.tradesave.list.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tradesave.list.controller.DeleteTrade
import com.tradesave.list.controller.GlobalStateController
import com.tradesave.list.controller.SetTradeToEdit
import com.tradesave.list.domain.data.DashboardInfo
import com.tradesave.list.screens.Screen
import com.tradesave.list.ui.screen.DashboardScreen
import com.tradesave.list.ui.screen.EditNoteScreen
import com.tradesave.list.ui.screen.EditTrade
import com.tradesave.list.ui.screen.SettingsScreen
import com.tradesave.list.ui.screen.TradeDetailsScreen
import com.tradesave.list.ui.theme.LocalTradeSaveColors
import com.tradesave.list.ui.theme.TradeSaveTheme
import com.tradesave.list.utils.composableAnimated

class MainActivity : ComponentActivity() {

    private val controller by lazy {
        GlobalStateController.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TradeSaveTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LocalTradeSaveColors.current.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        modifier = Modifier,
                        navController = navController,
                        startDestination = Screen.Companion.Dashboard.route,
                    ) {
                        composableAnimated(
                            Screen.Companion.Dashboard.route,
                        ) {
                            val info =
                                controller.dashboardInfoStream.collectAsState(DashboardInfo.empty())
                            val trades =
                                controller.savedTradesStream.collectAsState(emptyList())
                            DashboardScreen(
                                modifier = Modifier.fillMaxSize(),
                                info = info.value,
                                savedTrades = trades.value,
                                onAddTradeClick = {
                                    controller.createNewTrade()
                                    navController.navigate(Screen.Companion.EditTrade.route)
                                },
                                onTradeClick = { id ->
                                    navController.navigate(
                                        Screen.Companion.TradeDetails.createNavRoute(
                                            id
                                        )
                                    )
                                },
                                onSettingsClick = {
                                    navController.navigate(Screen.Companion.Settings.route)
                                }
                            )
                        }
                        composableAnimated(
                            Screen.Companion.EditTrade.route,
                        ) {
                            val tradeSave = controller.editTrade.collectAsState()
                            EditTrade(
                                modifier = Modifier.fillMaxSize(),
                                goBack = { navController.popBackStack() },
                                tradeSave = tradeSave.value,
                                sendEvent = {
                                    controller.processEvent(it)
                                },
                                goToEditNote = { navController.navigate(Screen.Companion.EditNote.route) }
                            )
                        }
                        composableAnimated(
                            Screen.Companion.Settings.route,
                        ) {
                            SettingsScreen(
                                modifier = Modifier.fillMaxSize(),
                                traderId = controller.traderId.toString(),
                                goBack = { navController.popBackStack() },
                                sendEvent = {
                                    controller.processEvent(it)
                                }
                            )
                        }
                        composableAnimated(
                            Screen.Companion.TradeDetails.route,
                            arguments = listOf(
                                navArgument(Screen.Companion.TradeDetails.TRADE_ID_KEY) {
                                    type = NavType.StringType
                                }
                            ),
                        ) { backStackEntry ->
                            val tradeId =
                                backStackEntry.arguments?.getString(Screen.Companion.TradeDetails.TRADE_ID_KEY)
                                    .orEmpty()
                            val saveTrade = controller.getTradeSave(tradeId).collectAsState(null)
                            saveTrade.value?.let {
                                TradeDetailsScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    tradeSave = it,
                                    goBack = { navController.popBackStack() },
                                    onEditClick = { _ ->
                                        controller.processEvent(SetTradeToEdit(it))
                                        navController.navigate(Screen.Companion.EditTrade.route)
                                    },
                                    onDeleteClick = { id ->
                                        navController.popBackStack()
                                        controller.processEvent(DeleteTrade(id))
                                    }
                                )
                            }

                        }
                        composableAnimated(Screen.Companion.EditNote.route) {
                            val tradeSave = controller.editTrade.collectAsState()
                            EditNoteScreen(
                                modifier = Modifier.fillMaxSize(),
                                note = tradeSave.value?.note.orEmpty(),
                                goBack = { navController.popBackStack() },
                                sendEvent = {
                                    controller.processEvent(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}