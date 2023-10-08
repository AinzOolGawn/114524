package me.rhunk.snapenhance.data

import me.rhunk.snapenhance.core.util.protobuf.ProtoReader

enum class MessageState {
    PREPARING, SENDING, COMMITTED, FAILED, CANCELING
}

enum class NotificationType (
    val key: String,
    val isIncoming: Boolean = false,
    val associatedOutgoingContentType: ContentType? = null,
) {
    SCREENSHOT("chat_screenshot", true, ContentType.STATUS_CONVERSATION_CAPTURE_SCREENSHOT),
    SCREEN_RECORD("chat_screen_record", true, ContentType.STATUS_CONVERSATION_CAPTURE_RECORD),
    CAMERA_ROLL_SAVE("camera_roll_save", true, ContentType.STATUS_SAVE_TO_CAMERA_ROLL),
    SNAP("snap",true),
    CHAT("chat",true),
    CHAT_REPLY("chat_reply",true),
    TYPING("typing", true),
    STORIES("stories",true),
    INITIATE_AUDIO("initiate_audio",true),
    ABANDON_AUDIO("abandon_audio", false, ContentType.STATUS_CALL_MISSED_AUDIO),
    INITIATE_VIDEO("initiate_video",true),
    ABANDON_VIDEO("abandon_video", false, ContentType.STATUS_CALL_MISSED_VIDEO);

    companion object {
        fun getIncomingValues(): List<NotificationType> {
            return entries.filter { it.isIncoming }.toList()
        }

        fun getOutgoingValues(): List<NotificationType> {
            return entries.filter { it.associatedOutgoingContentType != null }.toList()
        }

        fun fromContentType(contentType: ContentType): NotificationType? {
            return entries.firstOrNull { it.associatedOutgoingContentType == contentType }
        }
    }
}

enum class ContentType(val id: Int) {
    UNKNOWN(-1),
    SNAP(0),
    CHAT(1),
    EXTERNAL_MEDIA(2),
    SHARE(3),
    NOTE(4),
    STICKER(5),
    STATUS(6),
    LOCATION(7),
    STATUS_SAVE_TO_CAMERA_ROLL(8),
    STATUS_CONVERSATION_CAPTURE_SCREENSHOT(9),
    STATUS_CONVERSATION_CAPTURE_RECORD(10),
    STATUS_CALL_MISSED_VIDEO(11),
    STATUS_CALL_MISSED_AUDIO(12),
    LIVE_LOCATION_SHARE(13),
    CREATIVE_TOOL_ITEM(14),
    FAMILY_CENTER_INVITE(15),
    FAMILY_CENTER_ACCEPT(16),
    FAMILY_CENTER_LEAVE(17),
    STATUS_PLUS_GIFT(18);

    companion object {
        fun fromId(i: Int): ContentType {
            return entries.firstOrNull { it.id == i } ?: UNKNOWN
        }

        fun fromMessageContainer(protoReader: ProtoReader?): ContentType? {
            if (protoReader == null) return null
            return protoReader.run {
                when {
                    contains(8) -> STATUS
                    contains(2) -> CHAT
                    contains(11) -> SNAP
                    contains(6) -> NOTE
                    contains(3) -> EXTERNAL_MEDIA
                    contains(4) -> STICKER
                    contains(5) -> SHARE
                    contains(7) -> EXTERNAL_MEDIA // story replies
                    else -> null
                }
            }
        }
    }
}

enum class PlayableSnapState {
    NOTDOWNLOADED, DOWNLOADING, DOWNLOADFAILED, PLAYABLE, VIEWEDREPLAYABLE, PLAYING, VIEWEDNOTREPLAYABLE
}

enum class MetricsMessageMediaType {
    NO_MEDIA,
    IMAGE,
    VIDEO,
    VIDEO_NO_SOUND,
    GIF,
    DERIVED_FROM_MESSAGE_TYPE,
    REACTION
}

enum class MetricsMessageType {
    TEXT,
    STICKER,
    CUSTOM_STICKER,
    SNAP,
    AUDIO_NOTE,
    MEDIA,
    BATCHED_MEDIA,
    MISSED_AUDIO_CALL,
    MISSED_VIDEO_CALL,
    JOINED_CALL,
    LEFT_CALL,
    SNAPCHATTER,
    LOCATION_SHARE,
    LOCATION_REQUEST,
    SCREENSHOT,
    SCREEN_RECORDING,
    GAME_CLOSED,
    STORY_SHARE,
    MAP_DROP_SHARE,
    MAP_STORY_SHARE,
    MAP_STORY_SNAP_SHARE,
    MAP_HEAT_SNAP_SHARE,
    MAP_SCREENSHOT_SHARE,
    MEMORIES_STORY,
    SEARCH_STORY_SHARE,
    SEARCH_STORY_SNAP_SHARE,
    DISCOVER_SHARE,
    SHAZAM_SHARE,
    SAVE_TO_CAMERA_ROLL,
    GAME_SCORE_SHARE,
    SNAP_PRO_PROFILE_SHARE,
    SNAP_PRO_SNAP_SHARE,
    CANVAS_APP_SHARE,
    AD_SHARE,
    STORY_REPLY,
    SPOTLIGHT_STORY_SHARE,
    CAMEO,
    MEMOJI,
    BITMOJI_OUTFIT_SHARE,
    LIVE_LOCATION_SHARE,
    CREATIVE_TOOL_ITEM,
    SNAP_KIT_INVITE_SHARE,
    QUOTE_REPLY_SHARE,
    BLOOPS_STORY_SHARE,
    SNAP_PRO_SAVED_STORY_SHARE,
    PLACE_PROFILE_SHARE,
    PLACE_STORY_SHARE,
    SAVED_STORY_SHARE
}
enum class MediaReferenceType {
    UNASSIGNED, OVERLAY, IMAGE, VIDEO, ASSET_BUNDLE, AUDIO, ANIMATED_IMAGE, FONT, WEB_VIEW_CONTENT, VIDEO_NO_AUDIO
}

enum class FriendLinkType(val value: Int, val shortName: String) {
    MUTUAL(0, "mutual"),
    OUTGOING(1, "outgoing"),
    BLOCKED(2, "blocked"),
    DELETED(3, "deleted"),
    FOLLOWING(4, "following"),
    SUGGESTED(5, "suggested"),
    INCOMING(6, "incoming"),
    INCOMING_FOLLOWER(7, "incoming_follower");

    companion object {
        fun fromValue(value: Int): FriendLinkType {
            return entries.firstOrNull { it.value == value } ?: MUTUAL
        }
    }
}