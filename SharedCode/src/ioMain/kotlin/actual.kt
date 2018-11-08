package io.github.landerlyoung.kotlin.mpp

import platform.UIKit.UIDevice

actual fun platformName(): String {
    return UIDevice.currentDevices.let {
        it.systenName() + " " + it.systemVersion
    }
}
