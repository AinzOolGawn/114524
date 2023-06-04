package me.rhunk.snapenhance

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import me.rhunk.snapenhance.bridge.AbstractBridgeClient
import me.rhunk.snapenhance.bridge.client.RootBridgeClient
import me.rhunk.snapenhance.bridge.client.ServiceBridgeClient
import me.rhunk.snapenhance.data.SnapClassCache
import me.rhunk.snapenhance.hook.HookStage
import me.rhunk.snapenhance.hook.Hooker
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class SnapEnhance {
    companion object {
        lateinit var classLoader: ClassLoader
        val classCache: SnapClassCache by lazy {
            SnapClassCache(classLoader)
        }
    }
    private val appContext = ModContext()

    init {
        Hooker.hook(Application::class.java, "attach", HookStage.BEFORE) { param ->
            appContext.androidContext = param.arg<Context>(0).also {
                classLoader = it.classLoader
            }
            appContext.bridgeClient = provideBridgeClient()

            appContext.bridgeClient.apply {
                this.context = appContext
                start { bridgeResult ->
                    if (!bridgeResult) {
                        Logger.xposedLog("Cannot connect to bridge service")
                        appContext.restartApp()
                        return@start
                    }
                    runCatching {
                        init()
                    }.onFailure {
                        Logger.xposedLog("Failed to initialize", it)
                    }
                }
            }
        }

        Hooker.hook(Activity::class.java, "onCreate", HookStage.AFTER) {
            val activity = it.thisObject() as Activity
            if (!activity.packageName.equals(Constants.SNAPCHAT_PACKAGE_NAME)) return@hook
            val isMainActivityNotNull = appContext.mainActivity != null
            appContext.mainActivity = activity
            if (isMainActivityNotNull || !appContext.mappings.areMappingsLoaded) return@hook
            onActivityCreate()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun provideBridgeClient(): AbstractBridgeClient {
        //unsafe way for Android 9 devices
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            return RootBridgeClient()
        }
        return ServiceBridgeClient()
    }

    @OptIn(ExperimentalTime::class)
    private fun init() {
        measureTime {
            with(appContext) {
                translation.init()
                config.init()
                mappings.init()
                //if mappings aren't loaded, we can't initialize features
                if (!mappings.areMappingsLoaded) return
                features.init()
            }
        }.also { time ->
            Logger.debug("initialized in $time")
        }
    }

    private fun onActivityCreate() {
        with(appContext) {
            features.onActivityCreate()
            actionManager.init()
        }
    }
}