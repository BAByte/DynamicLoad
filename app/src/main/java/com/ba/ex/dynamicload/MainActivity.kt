package com.ba.ex.dynamicload

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.manamana.iot.dynamicload.R
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.InvocationTargetException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun byDex() {
        val fileApkDir = File(filesDir.absolutePath, "ex")
        if (!fileApkDir.exists()) {
            fileApkDir.mkdirs()
        }

        val lib = File(fileApkDir, "lib/" + "arm64-v8a")
        Log.d(">>>", "lib path = ${lib.absolutePath}")
        if (!lib.exists()) {
            lib.mkdirs()
        }

        val apk = fileApkDir.absolutePath + "/test.apk"
        val localClassLoader = ClassLoader.getSystemClassLoader()
        val load = DexClassLoader(apk, fileApkDir.absolutePath, lib.absolutePath, localClassLoader)
        invoke(load)

    }

    fun byInstall() {
        val context = createPackageContext(
            "com.babyte.banativecrash",
            CONTEXT_INCLUDE_CODE or CONTEXT_IGNORE_SECURITY
        )
        invoke(context.classLoader)
    }

    fun invoke(loader: ClassLoader) {
        try {
            val clazz = loader.loadClass("com.babyte.banativecrash.Test")
            Log.e(">>>", clazz.name)
            val mt = clazz.getMethod("getInt")
            val i = mt.invoke(clazz.newInstance()) as Int
            Log.e(">>>", i.toString() + "")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }
}