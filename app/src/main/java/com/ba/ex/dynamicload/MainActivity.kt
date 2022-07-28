package com.ba.ex.dynamicload

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.InvocationTargetException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        byDex()
//        byInstall()
    }

    fun byDex() {
        val fileApkDir = File(filesDir.canonicalPath, "ex")
        if (!fileApkDir.exists()) {
            fileApkDir.mkdirs()
        }

        val lib = File(fileApkDir, "lib/" + "arm64-v8a")
        Log.d(">>>", "lib path = ${lib.path}")
        if (!lib.exists()) {
            lib.mkdirs()
        }

        val apk = File(filesDir.canonicalPath,"ex/test.apk")
        Log.d(">>>", "apk path = ${apk.absolutePath}, ${apk.exists()}")

        val localClassLoader = ClassLoader.getSystemClassLoader()
        val load = DexClassLoader(apk.absolutePath, fileApkDir.absolutePath, lib.absolutePath, localClassLoader)
        invoke(load)
    }

    fun byInstall() {
        val context = createPackageContext(
            "com.babyte.banativecrash",
            CONTEXT_INCLUDE_CODE or CONTEXT_IGNORE_SECURITY
        )
        invoke(context.classLoader)
    }

    //测试获取类，然后执行方法
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

    //测试获取资源
    fun invoke(remoteContext: Context, loader: ClassLoader) {
        try {
            val clazz = loader.loadClass("com.manamana.iot.padiot.ui.widget.views.FlowBackButton")
            Log.e(">>>", clazz.name)
            val layout_flow_back_button = remoteContext.getResources()
                .getIdentifier("layout_flow_back_button", "layout", remoteContext.packageName)
            val btn_back_system_press = remoteContext.getResources()
                .getIdentifier("btn_back_system_press", "drawable", remoteContext.packageName)
            val btn_back_system_nor = remoteContext.getResources()
                .getIdentifier("btn_back_system_nor", "drawable", remoteContext.packageName)

            Log.e(">>>", "$layout_flow_back_button, $btn_back_system_press, $btn_back_system_nor")

            val initFlowBack = clazz.getMethod(
                "initFlowBack",
                Context::class.java,
                Int::class.java,
                Int::class.java,
                Int::class.java
            )
            val floatBtn = clazz.newInstance()
            initFlowBack.invoke(
                floatBtn,
                remoteContext,
                layout_flow_back_button,
                btn_back_system_press,
                btn_back_system_nor
            )
            val showFlowBack = clazz.getMethod("showFlowBack", Context::class.java, Int::class.java)
            val size_7a = remoteContext.getResources()
                .getIdentifier("size_7a", "dimen", remoteContext.packageName)
            showFlowBack.invoke(floatBtn,remoteContext,size_7a)
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