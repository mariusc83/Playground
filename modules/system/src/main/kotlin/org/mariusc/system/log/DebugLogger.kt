package org.mariusc.system.log

import android.util.Log

/**
 * Created by MConstantin on 4/27/2017.
 */

class DebugLogger : ILogger {

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun d(tag: String, throwable: Throwable, message: String) {
        Log.d(tag, message, throwable)
    }

    override fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun w(tag: String, throwable: Throwable, message: String) {
        Log.w(tag, message, throwable)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun e(tag: String, throwable: Throwable, message: String) {
        Log.e(tag, message, throwable)
    }

}
