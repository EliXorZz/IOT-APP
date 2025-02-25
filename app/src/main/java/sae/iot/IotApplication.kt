package sae.iot

import android.app.Application
import sae.iot.data.AppContainer
import sae.iot.data.DefaultAppContainer

class IotApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}