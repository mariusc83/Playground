package org.mariusc.gitdemo.data.network.repository.parse

import org.mariusc.gitdemo.data.network.model.RepoModel
import org.mariusc.gitdemo.data.network.model.ReposPage
import org.mariusc.gitdemo.data.network.model.ReposPageInfo

import retrofit2.Response

/**
 * Created by MConstantin on 4/28/2017.
 */

class ReposPageParser {

    fun parse(response: Response<List<RepoModel>>, currentSince: String = ""): ReposPage {
        val data = response.body()
        val headersMap = UrlLinkHeadersParserUtils.parse(response.headers())
        val nextUrl = headersMap[UrlLinkHeadersParserUtils.NEXT]
        val hasNext: Boolean
        val nextSince: String
        if (nextUrl != null) {
            hasNext = true;
            nextSince = UrlLinkHeadersParserUtils.sinceFromUrl(nextUrl)
        } else {
            val lastUrl = headersMap[UrlLinkHeadersParserUtils.LAST]
            if (lastUrl != null) {
                hasNext = true;
                nextSince = UrlLinkHeadersParserUtils.sinceFromUrl(lastUrl)
            } else {
                hasNext = false
                nextSince = ""
            }
        }
        val pageInfo = ReposPageInfo(currentSince, nextSince, hasNext)
        return ReposPage(pageInfo.hasNext, isFirstPage(pageInfo), pageInfo).apply {
            this.data = data
        }
    }

    private fun isFirstPage(pageInfo: ReposPageInfo): Boolean {
        return pageInfo.currentSince.isEmpty() || pageInfo.currentSince == "0"
    }
}
