package org.mariusc.gitdemo.view.utils

import org.mariusc.gitdemo.provider.TextProvider

import org.mariusc.gitdemo.R
import java.io.IOException


/**
 * Created by MConstantin on 5/2/2017.
 */

open class ErrorResolver(private val textProvider: TextProvider) {

    open fun resolveError(throwable: Throwable): String {
        if (throwable is IOException) {
            return textProvider.getString(R.string.no_internet_connection)
        }

        return textProvider.getString(R.string.bad_request_or_server_error)
    }
}
