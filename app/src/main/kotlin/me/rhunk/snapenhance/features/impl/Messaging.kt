package me.rhunk.snapenhance.features.impl

import me.rhunk.snapenhance.config.ConfigProperty
import me.rhunk.snapenhance.data.wrapper.impl.SnapUUID
import me.rhunk.snapenhance.features.Feature
import me.rhunk.snapenhance.features.FeatureLoadParams
import me.rhunk.snapenhance.hook.HookStage
import me.rhunk.snapenhance.hook.Hooker
import me.rhunk.snapenhance.hook.hook
import me.rhunk.snapenhance.util.getObjectField

class Messaging : Feature("Messaging", loadParams = FeatureLoadParams.ACTIVITY_CREATE_SYNC or FeatureLoadParams.INIT_ASYNC or FeatureLoadParams.INIT_SYNC) {
    lateinit var conversationManager: Any

    var openedConversationUUID: SnapUUID? = null
    var lastFetchConversationUserUUID: SnapUUID? = null
    var lastFetchConversationUUID: SnapUUID? = null
    var lastFetchGroupConversationUUID: SnapUUID? = null
    var lastFocusedMessageId: Long = -1

    override fun init() {
        Hooker.hookConstructor(context.classCache.conversationManager, HookStage.BEFORE) {
            conversationManager = it.thisObject()
        }
    }

    override fun onActivityCreate() {
        context.mappings.getMappedObjectNullable("FriendsFeedEventDispatcher")?.let {
            val friendsFeedEventDispatcherMappings = it as Map<*, *>
            findClass(friendsFeedEventDispatcherMappings["class"].toString()).hook("onItemLongPress", HookStage.BEFORE) { param ->
                val viewItemContainer = param.arg<Any>(0)
                val viewItem = viewItemContainer.getObjectField(friendsFeedEventDispatcherMappings["viewModelField"].toString()) ?: return@hook
                val friendViewItem = viewItem.getObjectField(friendsFeedEventDispatcherMappings["friendViewField"].toString()) ?: return@hook
                val conversationId = friendViewItem.getObjectField(friendsFeedEventDispatcherMappings["conversationIdField"].toString()).toString()
                lastFetchGroupConversationUUID = SnapUUID.fromString(conversationId)
            }
        }

        context.mappings.getMappedClass("callbacks", "GetOneOnOneConversationIdsCallback").hook("onSuccess", HookStage.BEFORE) { param ->
            val userIdToConversation = (param.arg<ArrayList<*>>(0))
                .takeIf { it.isNotEmpty() }
                ?.get(0) ?: return@hook

            lastFetchConversationUUID = SnapUUID(userIdToConversation.getObjectField("mConversationId"))
            lastFetchConversationUserUUID = SnapUUID(userIdToConversation.getObjectField("mUserId"))
        }

        with(context.classCache.conversationManager) {
            Hooker.hook(this, "enterConversation", HookStage.BEFORE) {
                openedConversationUUID = SnapUUID(it.arg(0))
            }

            Hooker.hook(this, "exitConversation", HookStage.BEFORE) {
                openedConversationUUID = null
            }
        }

    }

    override fun asyncInit() {
        arrayOf("activate", "deactivate", "processTypingActivity").forEach { hook ->
            Hooker.hook(context.classCache.presenceSession, hook, HookStage.BEFORE, { context.config.bool(ConfigProperty.HIDE_BITMOJI_PRESENCE) }) {
                it.setResult(null)
            }
        }

        //get last opened snap for media downloader
        Hooker.hook(context.classCache.snapManager, "onSnapInteraction", HookStage.BEFORE) { param ->
            openedConversationUUID = SnapUUID(param.arg(1))
            lastFocusedMessageId = param.arg(2)
        }

        Hooker.hook(context.classCache.conversationManager, "fetchMessage", HookStage.BEFORE) { param ->
            lastFetchConversationUserUUID = SnapUUID((param.arg(0) as Any))
            lastFocusedMessageId = param.arg(1)
        }

        Hooker.hook(context.classCache.conversationManager, "sendTypingNotification", HookStage.BEFORE,
            {context.config.bool(ConfigProperty.HIDE_TYPING_NOTIFICATION)}) {
            it.setResult(null)
        }
    }
}