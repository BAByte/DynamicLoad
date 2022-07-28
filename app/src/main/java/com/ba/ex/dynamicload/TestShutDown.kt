package com.ba.ex.dynamicload

import android.content.Context
import android.os.PowerManager

class TestShutDown {
    fun shutdown(context:Context) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        PowerManager::class.java
            .getDeclaredMethod(
                "shutdown",
                Boolean::class.java,
                String::class.java,
                Boolean::class.java
            )
            .invoke(pm, false, null, true)
    }


}