package me.rhunk.snapenhance.features.impl.extras

import me.rhunk.snapenhance.config.ConfigProperty
import me.rhunk.snapenhance.features.Feature
import me.rhunk.snapenhance.features.FeatureLoadParams
import me.rhunk.snapenhance.hook.HookStage
import me.rhunk.snapenhance.hook.Hooker

class LocationSpoofer: Feature("LocationSpoof", loadParams = FeatureLoadParams.ACTIVITY_CREATE_ASYNC) {
    override fun asyncOnActivityCreate() {
        if (!context.config.bool(ConfigProperty.LOCATION_SPOOF)) return
        val locationClass = android.location.Location::class.java
        val locationManagerClass = android.location.LocationManager::class.java

        Hooker.hook(locationClass, "getLatitude", HookStage.BEFORE) { hookAdapter ->
            hookAdapter.setResult(getLatitude())
        }

        Hooker.hook(locationClass, "getLongitude", HookStage.BEFORE) { hookAdapter ->
            hookAdapter.setResult(getLongitude())
        }

        Hooker.hook(locationClass, "getAccuracy", HookStage.BEFORE) { hookAdapter ->
            hookAdapter.setResult(getAccuracy())
        }

        //Might be redundant because it calls isProviderEnabledForUser which we also hook, meaning if isProviderEnabledForUser returns true this will also return true
        Hooker.hook(locationManagerClass, "isProviderEnabled", HookStage.BEFORE) { hookAdapter ->
            hookAdapter.setResult(true)
        }
        
        Hooker.hook(locationManagerClass, "isProviderEnabledForUser", HookStage.BEFORE) {hookAdapter ->
            hookAdapter.setResult(true)
        }
    }

    private fun getLatitude():Double {
        return context.config.string(ConfigProperty.LATITUDE).toDouble()
    }

    private fun getLongitude():Double {
        return context.config.string(ConfigProperty.LONGITUDE).toDouble()
    }

    private fun getAccuracy():Float {
        return 0.0f
    }
}