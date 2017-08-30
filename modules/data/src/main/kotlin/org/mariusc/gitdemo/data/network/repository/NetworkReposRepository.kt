package org.mariusc.gitdemo.data.network.repository

import org.mariusc.gitdemo.data.network.IGitApiService
import org.mariusc.gitdemo.data.network.model.ReposPage
import org.mariusc.gitdemo.data.network.repository.parse.ReposPageParser

import io.reactivex.Observable
import java.io.InputStream

/**
 * Created by MConstantin on 5/1/2017.
 */

class NetworkReposRepository(private val apiService: IGitApiService, private val pageParser: ReposPageParser) {

    internal fun fetchRepositories(since: String): Observable<ReposPage> {
        val input:InputStream = System.`in`
        return apiService
                .fetchRepositories(since)
                .map { pageParser.parse(it, since) }

    }

}
