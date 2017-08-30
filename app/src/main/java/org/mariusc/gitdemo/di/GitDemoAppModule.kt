package org.mariusc.gitdemo.di

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.mariusc.gitdemo.BuildConfig
import org.mariusc.gitdemo.data.network.IGitApiService
import org.mariusc.gitdemo.data.network.OkHttpClientWithCacheFactory
import org.mariusc.gitdemo.data.network.parse.AutoValueAdapterFactory
import org.mariusc.gitdemo.di.qualifier.AppContext
import org.mariusc.system.RxSystem
import org.mariusc.system.connectivity.ConnectivityState
import org.mariusc.system.log.DebugLogger
import org.mariusc.system.log.ILogger
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by MConstantin on 5/19/2017.
 */
@Module
class GitDemoAppModule(@AppContext val context:Context, val gitApiRestRoot: String) {

    @Singleton
    @Provides
    fun provideAppContext(): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideLogger(): ILogger {
        return if (BuildConfig.DEBUG) DebugLogger() else ILogger.NULL
    }

    @Provides
    fun provideConnectivityStateObservable(context: Context): Observable<ConnectivityState> {
        return RxSystem.connectivityStateObservable(context).share()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(context: Context,
                            connectivityStateObservable: Observable<ConnectivityState>,
                            logger: ILogger): OkHttpClient {
        return OkHttpClientWithCacheFactory(context,
                connectivityStateObservable).logger(logger).build()
    }

    @Singleton
    @Provides
    fun provideGitApiService(okHttpClient: OkHttpClient): IGitApiService {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(gitApiRestRoot)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(IGitApiService::class.java)
    }

}