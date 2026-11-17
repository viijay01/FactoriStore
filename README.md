# factori app generator (proof)

Turns a factori storefront into its own installable Android app.

Input: store name, storefront URL, icon (PNG). Output: `output/<slug>.apk`.

## Requirements
- Android Studio (includes Android SDK + JDK 17)
- Node.js 18+

## Run
1. Open this folder in Android Studio once and let Gradle sync finish.
2. In the Android Studio Terminal:
```
node generate.js "Zolara" https://zolara.in icons/zolara.png
node generate.js "GKP Vastra" https://gkpvastra.com icons/gkpvastra.png
```
3. Install `output/zolara.apk` and `output/gkpvastra.apk` on a phone.

## What makes each app unique
- `applicationId` = `com.factori.store.<slug>` (permanent after Play Store launch)
- App name and icon
- Store URL the app opens

## Inside the app
- Native WebView (not an iframe): JavaScript, DOM storage and persistent cookies so the store's OTP login survives restarts
- Back button navigates back inside the store
- Links outside the store (WhatsApp, phone, email, other sites) open in the right app
- Demo build: UPI/intent payment links show "Payment gateway not integrated in this demo"

## Not in this proof (production plan)
- Unique signing key per store (proof uses Android debug signing)
- AAB output for Play Store upload (`bundleRelease`)
- Hosted build queue, push notifications, app-user tracking
