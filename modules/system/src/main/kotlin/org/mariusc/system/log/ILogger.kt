package org.mariusc.system.log

/**
 * Created by MConstantin on 4/27/2017.
 */

interface ILogger {

    fun d(tag: String, message: String)

    fun d(tag: String, throwable: Throwable, message: String)

    fun w(tag: String, message: String)

    fun w(tag: String, throwable: Throwable, message: String)

    fun e(tag: String, message: String)

    fun e(tag: String, throwable: Throwable, message: String)

    companion object {
        val NULL: ILogger = object : ILogger {
            override fun d(tag: String, message: String) {}

            override fun d(tag: String, t: Throwable, message: String) {}

            override fun w(tag: String, message: String) {}

            override fun w(tag: String, t: Throwable, message: String) {}

            override fun e(tag: String, message: String) {}

            override fun e(tag: String, t: Throwable, message: String) {}

        }
    }
}
