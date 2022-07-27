package com.ba.ex.dynamicload.utils

import android.os.Build
import android.text.TextUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

object ABIUtils {
    fun isIaDevice(): Boolean {
        val abi = getDeviceAbi()
        return abi == "x86" || abi == "x86_64"
    }

    fun is64bitDevice(): Boolean {
        val abi = getDeviceAbi()
        return abi == "arm64-v8a" || abi == "x86_64"
    }

    fun is64bitApp(): Boolean {
        val abi = getRuntimeAbi()
        return abi == "arm64-v8a" || abi == "x86_64"
    }

    fun getABI(): String {
        val runtime = getRuntimeAbi()
        if (TextUtils.isEmpty(runtime)) {
            return getDeviceAbi()
        }
        return runtime
    }

    fun getRuntimeAbi(): String {
        var abiType = ""
        for (abi in Build.SUPPORTED_ABIS) {
            val abiLow = abi.lowercase(Locale.US)
            abiType = when (abiLow) {
                "armeabi", "armeabi-v7a" -> "armeabi-v7a"
                "arm64-v8a" -> "arm64-v8a"
                "x86" -> "x86"
                "x86_64" -> "x86_64"
                else -> null
            } ?: break
        }

        // The value may be ARM on Houdini devices.
        if (abiType == "armeabi-v7a") {
            if (isIaDevice()) {
                abiType = "x86"
            }
        } else if (abiType == "arm64-v8a") {
            if (isIaDevice()) {
                abiType = "x86_64"
            }
        }
        return abiType
    }

    fun getDeviceAbi(): String {
        var abiType = ""
        try {
            val process = Runtime.getRuntime().exec("getprop ro.product.cpu.abi")
            val ir = InputStreamReader(process.inputStream)
            val input = BufferedReader(ir)
            abiType = input.readLine().lowercase(Locale.US)
            input.close()
            ir.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return abiType
    }
}