package me.rhunk.snapenhance.features.impl.extras

import me.rhunk.snapenhance.Logger
import me.rhunk.snapenhance.config.ConfigProperty
import me.rhunk.snapenhance.config.impl.ConfigStateValue
import me.rhunk.snapenhance.config.impl.ConfigStringValue
import me.rhunk.snapenhance.features.Feature
import me.rhunk.snapenhance.features.FeatureLoadParams
import me.rhunk.snapenhance.hook.HookStage
import me.rhunk.snapenhance.hook.Hooker

class MediaQualityLevelOverride : Feature("MediaQualityLevelOverride", loadParams = FeatureLoadParams.INIT_SYNC) {
    override fun init() {
        //TODO: Finish this
        /*
        val configStringValue = ConfigProperty.OVERRIDE_MEDIA_QUALITY.valueContainer as ConfigStringValue
        val override: Boolean = (configStringValue?.value == "0")
        Logger.xposedLog("OVERRIDE_MEDIA_QUALITY value is $configStringValue will fall to $override")
         */
        val enumQualityLevel = context.mappings.getMappedClass("enums", "QualityLevel")

        Hooker.hook(context.mappings.getMappedClass("MediaQualityLevelProvider"),
            context.mappings.getMappedValue("MediaQualityLevelProviderMethod"),
            HookStage.BEFORE,
            {context.config.bool(ConfigProperty.OVERRIDE_MEDIA_QUALITY)}
        ) { param ->
            val currentCompressionLevel = enumQualityLevel.enumConstants
                .firstOrNull { it.toString() == context.config.state(ConfigProperty.MEDIA_QUALITY_LEVEL)} ?: run {
                context.longToast("Invalid media quality level: ${context.config.state(ConfigProperty.MEDIA_QUALITY_LEVEL)}")
                return@hook
            }
            param.setResult(currentCompressionLevel)
        }
    }
}