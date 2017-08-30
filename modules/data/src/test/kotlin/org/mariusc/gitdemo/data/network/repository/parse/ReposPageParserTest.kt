package org.mariusc.gitdemo.data.network.repository.parse

import org.mariusc.gitdemo.data.network.model.RepoModel

import okhttp3.Headers
import retrofit2.Response


import org.assertj.core.api.Java6Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mariusc.gitdemo.data.mock

@RunWith(JUnitPlatform::class)
class ReposPageParserTest : Spek({
    val mockData: List<RepoModel> = mock()
    var reposPageParser: ReposPageParser? = null

    beforeGroup {
        reposPageParser = ReposPageParser()
    }

    on("first page request") {
        val nextSince = "1479";
        val nextUrl = "https://api.github.com/repositories?since=" + nextSince
        val firstUrl = "https://api.github.com/repositories{?since}"
        val header = "Link:<" + nextUrl + ">; rel=\"next\"," +
                " <" + firstUrl + ">; rel=\"first\""
        val mockHeaders = Headers.Builder().add(header).build()
        val response = Response.success<List<RepoModel>>(mockData, mockHeaders)
        val currentSince = "0"

        it("will return a valid page") {
            val (hasMore, isFirst, pageInfo, data) = reposPageParser!!.parse(response, currentSince)

            // then
            assertThat(data).isEqualTo(mockData)
            assertThat(hasMore).isTrue()
            assertThat(isFirst).isTrue()
            assertThat(pageInfo.nextSince).isEqualTo(nextSince)
            assertThat(pageInfo.currentSince).isEqualTo(currentSince)
        }
    }

    on("before the last page request") {
        val nextSince = "2333"
        val nextUrl = "https://api.github.com/repositories?since=" + nextSince
        val firstUrl = "https://api.github.com/repositories{?since}"
        val header = "Link:<" + nextUrl + ">; rel=\"last\"," +
                " <" + firstUrl + ">; rel=\"first\""

        val mockHeaders = Headers.Builder().add(header).build()
        val response = Response.success<List<RepoModel>>(mockData, mockHeaders)
        val currentSince = "1479"

        it("will return a valid page") {
            // when
            val (hasMore, isFirst, pageInfo, data) = reposPageParser!!.parse(response, currentSince)


            // then
            assertThat(data).isEqualTo(mockData)
            assertThat(hasMore).isTrue()
            assertThat(isFirst).isFalse()
            assertThat(pageInfo.nextSince).isEqualTo(nextSince)
            assertThat(pageInfo.currentSince).isEqualTo(currentSince)
        }
    }

    on("last page request"){
        val firstUrl = "https://api.github.com/repositories{?since}"
        val header = "Link:<$firstUrl>; rel=\"first\""
        val mockHeaders = Headers.Builder().add(header).build()
        val response = Response.success<List<RepoModel>>(mockData, mockHeaders)
        val currentSince = "2333"

        it("will return a valid page"){
            // when
            val (hasMore, isFirst, pageInfo, data) = reposPageParser!!.parse(response, currentSince)


            // then
            assertThat(data).isEqualTo(mockData)
            assertThat(hasMore).isFalse()
            assertThat(isFirst).isFalse()
            assertThat(pageInfo.nextSince).isEmpty()
            assertThat(pageInfo.currentSince).isEqualTo(currentSince)
        }
    }
})