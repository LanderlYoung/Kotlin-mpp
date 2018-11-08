package io.github.landerlyoung.kotlin.mpp

import platform.UIKit.UIDevice

actual fun platformName(): String {
    return UIDevice.currentDevice.let {
        it.systemName() + " " + it.systemVersion
    }
}
