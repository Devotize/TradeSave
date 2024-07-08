package com.tradesave.list.app

import android.app.Application
import com.tradesave.list.controller.GlobalStateController

class TradeSaveApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalStateController.create(this)
    }

}