package org.mariusc.gitdemo.data.network.repository.parse

import android.support.v4.util.ArrayMap
import java.util.regex.Pattern

import okhttp3.Headers

/**
 * Created by MConstantin on 4/28/2017.
 */

/* package */
internal object UrlLinkHeadersParserUtils {

    val HEADER_NAME = "Link"
    val NEXT = "next"
    val FIRST = "first"
    val PREVIOUS = "prev"
    val LAST = "last"

    private val URL_PATTERN = Pattern.compile("<(.*)>")
    private val SINCE_PATTERN = Pattern.compile("since=(.*)")
    private val URL_REL_PATTERN = Pattern.compile("rel=\"(.*)\"")

    /* package */
    fun parse(headers: Headers): Map<String, String> {
        val parsedHeaders = mutableMapOf<String, String>()
        val linkHeader = headers.get(HEADER_NAME)
        linkHeader.takeIf { it.isNotEmpty() }?.let {
            it.split(",".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .forEach {
                        it.split(";".toRegex())
                                .dropLastWhile { it.isEmpty() }
                                .take(2)
                                .takeIf {
                                    it.size >= 2
                                }
                                ?.let {
                                    val entrySection = it.get(0).trim()
                                    val urlMatcher = URL_PATTERN.matcher(entrySection)
                                    if (urlMatcher.matches()) {
                                        val url = urlMatcher.group(1)
                                        val entrySection1 = it.get(1).trim()
                                        val urlRelMatcher = URL_REL_PATTERN.matcher(entrySection1)
                                        if (urlRelMatcher.matches()) {
                                            val rel = urlRelMatcher.group(1)
                                            parsedHeaders.put(rel, url)
                                        }
                                    }
                                }
                    }

        }


        return parsedHeaders
    }

    fun consumeHeaderValue(headers:List<String>){

    }

    fun sinceFromUrl(nextUrl: String): String {
        val matcher = SINCE_PATTERN.matcher(nextUrl.trim { it <= ' ' })
        if (matcher.find()) {
            return matcher.group(1)
        }
        return ""
    }

}
