package me.rhunk.snapenhance.features.impl.ui.menus.impl

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import me.rhunk.snapenhance.Logger
import me.rhunk.snapenhance.config.ConfigProperty
import me.rhunk.snapenhance.data.ContentType
import me.rhunk.snapenhance.data.wrapper.impl.FriendActionButton
import me.rhunk.snapenhance.database.objects.ConversationMessage
import me.rhunk.snapenhance.database.objects.FriendInfo
import me.rhunk.snapenhance.database.objects.UserConversationLink
import me.rhunk.snapenhance.features.impl.Messaging
import me.rhunk.snapenhance.features.impl.downloader.AntiAutoDownload
import me.rhunk.snapenhance.features.impl.spying.StealthMode
import me.rhunk.snapenhance.features.impl.tweaks.AntiAutoSave
import me.rhunk.snapenhance.features.impl.ui.menus.AbstractMenu
import me.rhunk.snapenhance.features.impl.ui.menus.ViewAppearanceHelper.applyTheme
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FriendFeedInfoMenu : AbstractMenu() {
    private fun getImageDrawable(url: String): Drawable {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connect()
        val input = connection.inputStream
        return BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeStream(input))
    }

    private fun formatDate(timestamp: Long): String? {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(Date(timestamp))
    }

    private fun showProfileInfo(profile: FriendInfo) {
        var icon: Drawable? = null
        try {
            if (profile.bitmojiSelfieId != null && profile.bitmojiAvatarId != null) {
                icon = getImageDrawable(
                    "https://sdk.bitmoji.com/render/panel/" + profile.bitmojiSelfieId
                        .toString() + "-" + profile.bitmojiAvatarId
                        .toString() + "-v1.webp?transparent=1&scale=0"
                )
            }
        } catch (e: Throwable) {
            Logger.xposedLog(e)
        }
        val finalIcon = icon
        context.runOnUiThread {
            val addedTimestamp: Long = profile.addedTimestamp.coerceAtLeast(profile.reverseAddedTimestamp)
            val builder = AlertDialog.Builder(context.mainActivity)
            builder.setIcon(finalIcon)
            builder.setTitle(profile.displayName)

            val birthday = Calendar.getInstance()
            birthday[Calendar.MONTH] = (profile.birthday shr 32).toInt() - 1
            val message: String = """
                ${context.translation.get("profile_info.username")}: ${profile.username}
                ${context.translation.get("profile_info.display_name")}: ${profile.displayName}
                ${context.translation.get("profile_info.added_date")}: ${formatDate(addedTimestamp)}
                ${birthday.getDisplayName(
                    Calendar.MONTH,
                    Calendar.LONG,
                    context.translation.locale
                )?.let {
                    context.translation.get("profile_info.birthday")
                        .replace("{month}", it)
                        .replace("{day}", profile.birthday.toInt().toString())
                }
            }
            """.trimIndent()
            builder.setMessage(message)
            builder.setPositiveButton(
                "OK"
            ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            builder.show()
        }
    }

    private fun showPreview(userId: String?, conversationId: String, androidCtx: Context?) {
        //query message
        val messages: List<ConversationMessage>? = context.database.getMessagesFromConversationId(
            conversationId,
            context.config.int(ConfigProperty.MESSAGE_PREVIEW_LENGTH)
        )?.reversed()

        if (messages.isNullOrEmpty()) {
            Toast.makeText(androidCtx, "No messages found", Toast.LENGTH_SHORT).show()
            return
        }
        val participants: Map<String, FriendInfo> = context.database.getConversationParticipants(conversationId)!!
            .map { context.database.getFriendInfo(it)!! }
            .associateBy { it.userId!! }
        
        val messageBuilder = StringBuilder()

        messages.forEach{ message: ConversationMessage ->
            val sender: FriendInfo? = participants[message.sender_id]

            var messageString: String = message.getMessageAsString() ?: ContentType.fromId(message.content_type).name

            if (message.content_type == ContentType.SNAP.id) {
                val readTimeStamp: Long = message.read_timestamp
                messageString = "\uD83D\uDFE5" //red square
                if (readTimeStamp > 0) {
                    messageString += " \uD83D\uDC40 " //eyes
                    messageString += DateFormat.getDateTimeInstance(
                        DateFormat.SHORT,
                        DateFormat.SHORT
                    ).format(Date(readTimeStamp))
                }
            }

            var displayUsername = sender?.displayName ?: sender?.usernameForSorting?: context.translation.get("conversation_preview.unknown_user")

            if (displayUsername.length > 12) {
                displayUsername = displayUsername.substring(0, 13) + "... "
            }

            messageBuilder.append(displayUsername).append(": ").append(messageString).append("\n")
        }

        val targetPerson: FriendInfo? =
            if (userId == null) null else participants[userId]

        targetPerson?.streakExpirationTimestamp?.takeIf { it > 0 }?.let {
            val timeSecondDiff = ((it - System.currentTimeMillis()) / 1000 / 60).toInt()
            messageBuilder.append("\n\n")
                .append("\uD83D\uDD25 ") //fire emoji
                .append(context.translation.get("conversation_preview.streak_expiration").format(
                    timeSecondDiff / 60 / 24,
                    timeSecondDiff / 60 % 24,
                    timeSecondDiff % 60
                ))
        }

        //alert dialog
        val builder = AlertDialog.Builder(context.mainActivity)
        builder.setTitle(context.translation.get("conversation_preview.title"))
        builder.setMessage(messageBuilder.toString())
        builder.setPositiveButton(
            "OK"
        ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        targetPerson?.let {
            builder.setNegativeButton(context.translation.get("modal_option.profile_info")) {_, _ ->
                context.executeAsync {
                    showProfileInfo(it)
                }
            }
        }
        builder.show()
    }

    private fun createToggleFeature(viewModel: View, viewConsumer: ((View) -> Unit), text: String, isChecked: () -> Boolean, toggle: (Boolean) -> Unit) {
        val switch = Switch(viewModel.context)
        switch.text = context.translation.get(text)
        switch.isChecked = isChecked()
        applyTheme(viewModel, switch)
        switch.setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean ->
            toggle(checked)
        }
        viewConsumer(switch)
    }

    private fun createEmojiDrawable(text: String, width: Int, height: Int, textSize: Float, disabled: Boolean = false): Drawable {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.textSize = textSize
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(text, width / 2f, height.toFloat() - paint.descent(), paint)
        if (disabled) {
            paint.color = Color.RED
            paint.strokeWidth = 5f
            canvas.drawLine(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }
        return BitmapDrawable(context.resources, bitmap)
    }

    @SuppressLint("SetTextI18n", "UseSwitchCompatOrMaterialCode", "DefaultLocale", "InflateParams",
        "DiscouragedApi", "ClickableViewAccessibility"
    )
    fun inject(viewModel: View, viewConsumer: ((View) -> Unit)) {
        val messaging = context.feature(Messaging::class)
        var focusedConversationTargetUser: String? = null
        val conversationId: String
        if (messaging.lastFetchConversationUserUUID != null) {
            focusedConversationTargetUser = messaging.lastFetchConversationUserUUID.toString()
            val conversation: UserConversationLink = context.database.getDMConversationIdFromUserId(focusedConversationTargetUser) ?: return
            conversationId = conversation.client_conversation_id!!.trim().lowercase()
        } else {
            conversationId = messaging.lastFetchConversationUUID.toString()
        }

        val friendFeedActionBar = LinearLayout(viewModel.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            applyTheme(viewModel, this)
            setOnTouchListener { _, _ -> true}
        }

        fun createActionButton(icon: String, isDisabled: Boolean? = null, onClick: (Boolean) -> Unit) {
            friendFeedActionBar.addView(LinearLayout(viewModel.context).apply {
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
                gravity = Gravity.CENTER
                setPadding(0, 20, 0, 20)
                isClickable = false

                var isLineThrough = isDisabled ?: false
                FriendActionButton.new(viewModel.context).apply {
                    fun setLineThrough(value: Boolean) {
                        setIconDrawable(createEmojiDrawable(icon, 60, 60, 50f, if (isDisabled == null) false else value))
                    }
                    setLineThrough(isLineThrough)
                    instanceNonNull().apply {
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                            setMargins(0, 60, 0, 60)
                        }
                        setOnClickListener {
                            isLineThrough = !isLineThrough
                            onClick(isLineThrough)
                            setLineThrough(isLineThrough)
                        }
                    }

                }.also { addView(it.instanceNonNull()) }

            })
        }


        if (context.config.bool(ConfigProperty.DOWNLOAD_BLACKLIST)) {
            run {
                val userId = context.database.getFriendFeedInfoByConversationId(conversationId)?.friendUserId ?: return@run
                //toolbox
                createActionButton("\uD83E\uDDF0", isDisabled = !context.feature(AntiAutoDownload::class).isUserIgnored(userId) ) {
                    context.feature(AntiAutoDownload::class).setUserIgnored(userId, it)
                }
            }
        }


        if (context.config.bool(ConfigProperty.ANTI_AUTO_SAVE)) {
            //diskette
            createActionButton("\uD83D\uDCBE", isDisabled = !context.feature(AntiAutoSave::class).isConversationIgnored(conversationId) ) {
                context.feature(AntiAutoSave::class).setConversationIgnored(conversationId, it)
            }
        }


        //eyes
        createActionButton("\uD83D\uDC7B", isDisabled = !context.feature(StealthMode::class).isStealth(conversationId)) { isChecked ->
            context.feature(StealthMode::class).setStealth(
                conversationId,
                !isChecked
            )
        }

        //user
        createActionButton("\uD83D\uDC64") {
            showPreview(
                focusedConversationTargetUser,
                conversationId,
                viewModel.context
            )
        }

        viewConsumer(friendFeedActionBar)

        /*

        //preview button
        val previewButton = Button(viewModel.context)
        previewButton.text = context.translation.get("friend_menu_option.preview")
        applyTheme(viewModel, previewButton)
        val finalFocusedConversationTargetUser = focusedConversationTargetUser
        previewButton.setOnClickListener {
            showPreview(
                finalFocusedConversationTargetUser,
                conversationId,
                previewButton.context
            )
        }

        //stealth switch
        val stealthSwitch = Switch(viewModel.context)
        stealthSwitch.text = context.translation.get("friend_menu_option.stealth_mode")
        stealthSwitch.isChecked = context.feature(StealthMode::class).isStealth(conversationId)
        applyTheme(viewModel, stealthSwitch)
        stealthSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            context.feature(StealthMode::class).setStealth(
                conversationId,
                isChecked
            )
        }

        run {
            val userId = context.database.getFriendFeedInfoByConversationId(conversationId)?.friendUserId ?: return@run
            if (context.config.bool(ConfigProperty.DOWNLOAD_BLACKLIST)) {
                createToggleFeature(viewModel,
                    viewConsumer,
                    "friend_menu_option.anti_auto_download",
                    { context.feature(AntiAutoDownload::class).isUserIgnored(userId) },
                    { context.feature(AntiAutoDownload::class).setUserIgnored(userId, it) }
                )
            }

            if (context.config.bool(ConfigProperty.ANTI_AUTO_SAVE)) {
                createToggleFeature(viewModel,
                    viewConsumer,
                    "friend_menu_option.anti_auto_save",
                    { context.feature(AntiAutoSave::class).isConversationIgnored(conversationId) },
                    { context.feature(AntiAutoSave::class).setConversationIgnored(conversationId, it) }
                )
            }
        }

        viewConsumer(stealthSwitch)
        viewConsumer(previewButton)*/
    }
}