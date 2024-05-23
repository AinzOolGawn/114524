<div align="center">
  <img src="https://raw.githubusercontent.com/rhunk/SnapEnhance/main/app/src/main/res/mipmap-xxxhdpi/launcher_icon_foreground.png" height="250" />

  [![Build](https://img.shields.io/github/actions/workflow/status/rhunk/SnapEnhance/debug.yml?branch=dev&logo=github&label=Build)](https://github.com/rhunk/SnapEnhance/actions/workflows/android.yml?query=branch%3Amain+event%3Apush+is%3Acompleted) [![Total](https://shields.io/github/downloads/rhunk/SnapEnhance/total?logo=Bookmeter&label=Downloads&logoColor=Green&color=Green)](https://github.com/rhunk/snapenhance/releases) [![Translation status](https://hosted.weblate.org/widget/snapenhance/app/svg-badge.svg)](https://hosted.weblate.org/engage/snapenhance/)
  
# SnapEnhance
SnapEnhance is an Xposed mod that enhances your Snapchat experience.<br/><br/>
Please note that this project is currently in development, so bugs and crashes may occur. If you encounter any issues, we encourage you to report them. To do this simply visit our [issues](https://github.com/rhunk/SnapEnhance/issues) page and create an issue, make sure to follow the guidelines.
</div>

## Quick Start
Requirements:
- Rooted using `Magisk` or `KernelSU`
- `LSPosed` installed and fully functional

Although using this in an unrooted enviroment using something like `LSPatch` should be working fine, it is not recommended to do so, use at your own risk!

1. Install the module APK from either this [Github repo](https://github.com/rhunk/SnapEnhance/releases) or the [LSPosed repo](https://modules.lsposed.org/module/me.rhunk.snapenhance)
2. Turn on the module in `LSPosed` and make sure Snapchat is in scope
3. Force Stop Snapchat
4. Open the menu by clicking the [Settings Gear Icon](https://i.imgur.com/2grm8li.png)

## Download 
To Download the latest stable release, please visit the [Releases](https://github.com/rhunk/SnapEnhance/releases) page.<br/>
You can also download the latest debug build from the [Actions](https://github.com/rhunk/SnapEnhance/actions) section.<br/>
We no longer offer official `LSPatch` binaries for obvious reasons. However, you're welcome to patch them yourself, as they should theoretically work without any issues.

## Main Features
<details closed>
  <summary>Media Downloader</summary>
   
  - `Auto Download`
  - `Prevent Self Auto Download`
  - `Merge Overlays`
  - `Force Image Format`
  - `Force Voice Note Format`
  - `Download Profile Pictures`
  - `Opera Download Button`
  - `Chat Download Context Menu`
  - `Logging`
  - `Custom Path Format` 
</details>

<details closed>
  <summary>User Interface</summary>
  
  - `Friend Feed Menu Buttons` 
  - `Colors` (Theme Picker & Custom Colors)
  - `Friend Feed Message Preview` 
  - `Snap Preview`
  - `Bootstrap Override` (Default Home Tab & Persistent App Appearance)
  - `Enhance Friend Map Nametags`
  - `Prevent Message List Auto Scroll`
  - `Show Streak Expiration Info`
  - `Hide Friend Feed Entry`
  - `Hide Streak Restore`
  - `Hide Quick Add In Friend Feed`
  - `Hide Story Suggestions` 
  - `Hide UI Components` (Voice Record button, Call Buttons, ...)
  - `Opera Media Quick Info`
  - `Old Bitmoji Selfie` 
  - `Disable Spotlight` 
  - `Hide Settings Gear`
  - `Vertical Story Viewer` 
  - `Message Indicators` 
  - `Stealth Mode Indicator` 
  - `Edit Text Override`
</details>  

<details closed>
  <summary>Messaging</summary>
  
  - `Bypass Screenshot Detection` 
  - `Anonymous Story Viewing`
  - `Prevent Story Rewatch Indicator`
  - `Hide Peek-a-Peek`
  - `Hide Bitmoji Presence` 
  - `Hide Typing Notifications` 
  - `Unlimited Snap View Time`
  - `Auto Mark As Read` 
  - `Loop Media PlayBack`
  - `Disable Replay In FF`
  - `Half Swipe Notifier`
  - `Message Preview Length`
  - `Call Start Confirmation`
  - `Unlimited Conversation Pinning`
  - `Auto Save Messages` 
  - `Prevent Message Sending`
  - `Friend Mutation Notifier`
  - `Better Notifications` 
  - `Notifications Blacklist`
  - `Message Logger`
  - `Gallery Media Send Override`
  - `Strip Media Metadata`
  - `Bypass Message Retention Policy`
  - `Bypass Message Action Restrictions`
  - `Remove Groups Locked Status` 
 </details>

<details closed>
  <summary>Global</summary>
 
  - `Better Location`
  - `Snapchat Plus`
  - `Media Upload Quality`
  - `Disable Confirmation Dialogs`
  - `Disable Metrics`
  - `Disable Story Sections`
  - `Block Ads`
  - `Disable Custom Tabs`
  - `Disable Permission Request`
  - `Disable Memories Snap Feed`
  - `Spotlight Comments Username` 
  - `Bypass Video Length Restriction`
  - `Default Video Playback Rate`
  - `Video Playback Rate Slider`
  - `Disable Google Play Services Dialogs`
  - `Default Volume Controls`
  - `Hide Active Music`
  - `Disable Snap Splitting`
</details>

<details closed>
  <summary>Camera</summary>
  
  - `Disable Camera`
  - `Immersive Preview`
  - `Black Photos` 
  - `Custom Frame Rate` (Front & Back)
  - `HEVC Recording`
  - `Force Camera Source Encoding`
  - `Override Resolution` (Front & Back)
</details> 

<details closed>
  <summary>Experimental</summary>
  
  - `Native Hooks`
  - `Device Spoof`
  - `Convert Message Locally`
  - `New Chat Action Menu`
  - `Media File Picker`
  - `Story Logger`
  - `Call Recorder`
  - `Account Switcher`
  - `Edit Messages` 
  - `App Lock`
  - `Infinite Story Boost`
  - `My Eyes Only Passcode Bypass`
  - `No Friend Score Delay`
  - `Best Friend Pinning`
  - `End-to-End Encryption`
  - `Enable Hidden Snapchat Plus Features`
  - `Custom Streaks Expiration Format`
  - `Add Friend Source Spoof`
  - `Prevent Forced Logout`
</details>

## FAQ
<details>
  <summary>AI wallpapers and the Snapchat+ badge aren't working!</summary>
  
  - Yeah, they're server-sided and will probably never work.
</details>

<details>
  <summary>Can you add this feature, please?</summary>
  
  - Open an issue on our Github repo.
</details>

<details>
  <summary>When will this feature become available or finish?</summary>
  
  - At some point.
</details>

<details>
  <summary>Can I get banned with this?</summary>
  
  - Obviously, however, the risk is very low, and we have no reported cases of anyone ever getting banned while using the mod.
</details>

<details>
  <summary>Can I PM the developers?</summary>
  
  - No.
</details>

<details>
  <summary>This doesn't work!</summary>
  
  - Open an issue.
</details>

<details>
  <summary>My phone isn't rooted; how do I use this?</summary>
  
  - You can use `LSPatch` in combination with `SnapEnhance` to run this on an unrooted device, however this is unrecommended and not considered safe.
</details>

<details>
  <summary>Where can I download the latest stable build?</summary>
  
  - https://github.com/rhunk/snapenhance/releases
</details>

<details>
  <summary>Can I use HideMyApplist with this?</summary>
  
  - No, this will cause some severe issues, and the mod will not be able to inject.
</details>

<details>
  <summary>How can I translate SnapEnhance into my language?</summary>
  
  - We have a [Weblate](https://hosted.weblate.org/projects/snapenhance/app/) hosted repo, feel free to submit your translations there.
</details>

## Privacy
We do not collect any user information. However, please be aware that third-party libraries may collect data as described in their respective privacy policies.
<details>
  <summary>Permissions</summary>
  
  - [android.permission.INTERNET](https://developer.android.com/reference/android/Manifest.permission#INTERNET)
  - [android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS](https://developer.android.com/reference/android/Manifest.permission.html#REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
  - [android.permission.POST_NOTIFICATIONS](https://developer.android.com/reference/android/Manifest.permission.html#POST_NOTIFICATIONS)
  - [android.permission.SYSTEM_ALERT_WINDOW](https://developer.android.com/reference/android/Manifest.permission#SYSTEM_ALERT_WINDOW)
  - [android.permission.USE_BIOMETRIC](https://developer.android.com/reference/android/Manifest.permission#USE_BIOMETRIC)
</details>

<details>
  <summary>Third-party libraries used</summary>
  
  - [libxposed](https://github.com/libxposed/api)
  - [ffmpeg-kit-full-gpl](https://github.com/arthenica/ffmpeg-kit)
  - [osmdroid](https://github.com/osmdroid/osmdroid)
  - [coil](https://github.com/coil-kt/coil)
  - [Dobby](https://github.com/jmpews/Dobby)
  - [rhino](https://github.com/mozilla/rhino)
  - [rhino-android](https://github.com/F43nd1r/rhino-android)
  - [libsu](https://github.com/topjohnwu/libsu)
  - [colorpicker-compose](https://github.com/skydoves/colorpicker-compose)
</details>

## Contributors
Thanks to everyone involved including the [third-party libraries](https://github.com/rhunk/SnapEnhance?tab=readme-ov-file#privacy) used!
- [rathmerdominik](https://github.com/rathmerdominik)
- [Flole998](https://github.com/Flole998)
- [authorisation](https://github.com/authorisation/)
- [RevealedSoulEven](https://github.com/revealedsouleven)
- [iBasim](https://github.com/ibasim)
- [xerta555](https://github.com/xerta555)
- [TheVisual](https://github.com/TheVisual)
- [CanerKaraca23](https://github.com/CanerKaraca23)
- [bocajthomas](https://github.com/bocajthomas)
- [w451](https://github.com/w451)
- [GabeModz](https://github.com/Gabe-does-tech)
- [JWHC1](https://github.com/JWHC1)

## Donate
**@rhunk**
- LTC: LbBnT9GxgnFhwy891EdDKqGmpn7XtduBdE
- BCH: qpu57a05kqljjadvpgjc6t894apprvth9slvlj4vpj
- BTC: bc1qaqnfn6mauzhmx0e6kkenh2wh4r6js0vh5vel92
- ETH: 0x0760987491e9de53A73fd87F092Bd432a227Ee92
- XMR: 49W4Xp7QKdUdVw4otEctWZDC8gnW9nnhoiqMJzY6sKuZBD9xrh9BG4sjdE3r1Jt78j27R6x7522YUA9JVvh892CAJLeKaMu

**@authorisation**
- XMR: 82kPkAgG2zxQYnSdoFSWzvbSEtEP63NBDh9hgLqp6LgBhPNhZ4dDGv8gVFUEuUhDoi1U14ZgE71teJXo2eBe8iERRRmhcUW
