package org.mariusc.gitdemo.data.network

import org.mariusc.gitdemo.data.network.model.RepoModel

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by MConstantin on 4/26/2017.
 */

interface IGitApiService {

    @GET("repositories")
    fun fetchRepositories(@Query("since") since: String): Observable<Response<List<RepoModel>>>

    @GET("search/repositories")
    fun searchRepositories(@Query("q") query: String, @Query("page") page: String): Observable<Response<List<RepoModel>>>
}
