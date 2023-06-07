package me.rhunk.snapenhance.features.impl.experiments

import me.rhunk.snapenhance.features.Feature
import me.rhunk.snapenhance.features.FeatureLoadParams
import me.rhunk.snapenhance.hook.HookStage
import me.rhunk.snapenhance.hook.hookConstructor

class InfiniteStoryBoost : Feature("InfiniteStoryBoost", loadParams = FeatureLoadParams.ACTIVITY_CREATE_ASYNC) {
    override fun asyncOnActivityCreate() {
        val storyBoostStateClass = context.mappings.getMappedClass("StoryBoostStateClass")

        storyBoostStateClass.hookConstructor(HookStage.BEFORE) { param ->
            val startTimeMillis = param.arg<Long>(1)
            if (System.currentTimeMillis() - startTimeMillis > 604800000) {
                param.setArg(1, 0)
                param.setArg(2, 0)
            }
        }
    }
}