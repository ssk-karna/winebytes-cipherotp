# CipherOTP

CipherOTP is a lightweight, customizable Android library for handling OTP (One Time Password) inputs with a clean and secure UI. Designed with flexibility and UX in mind, it helps developers quickly integrate secure OTP entry components into their apps.

## Features

- Customizable number of OTP boxes (4 to 8)
- Customizable border color via XML and programmatically
- Automatic focus movement between boxes
- Support for manual OTP input and auto-fill
- Seamless handling of deletion and backspacing
- Extensible for future OTP-related features

## Screenshots

*Coming soon*

## Installation

### Using JitPack

### Step 1 : Add the JitPack repository to your root `build.gradle`:
````gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
`````

### Step 2 : Add the dependency :
````gradle
dependencies {
    implementation 'com.github.ssk-karna:winebytes-cipherotp:1.1.0'
}
````


## Usage
### XML

````xml
<com.winebytes.cipherotp.OtpBoxView
    android:id="@+id/otpBox"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:otpBoxCount="6"
    app:boxBorderColor="@color/otp_border"
    app:boxCornerRadius="8dp"
/>
````

### Kotlin
````kotlin
val otpBox = findViewById<OtpBoxView>(R.id.otpBox)
otpBox.setOtpBoxCount(6)
otpBox.setBorderColor(Color.BLUE)
````

## Public API Reference

````kotlin
fun setOtpBoxCount(@IntRange(from = 4, to = 8) count: Int)
fun setBorderColor(@ColorInt color: Int)
fun getOtp(): String
fun setOtp(value: String)

````

