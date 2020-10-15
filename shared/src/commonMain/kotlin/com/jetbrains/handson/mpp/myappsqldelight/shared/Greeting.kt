package com.jetbrains.handson.mpp.myappsqldelight.shared


class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}
