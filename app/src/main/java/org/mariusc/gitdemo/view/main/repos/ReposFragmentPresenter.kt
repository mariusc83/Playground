package org.mariusc.gitdemo.view.main.repos

import org.mariusc.gitdemo.data.network.model.ReposPage
import org.mariusc.gitdemo.view.main.repos.model.GetAllPagesToSince
import org.mariusc.gitdemo.view.main.repos.model.GetReposPage
import org.mariusc.gitdemo.view.main.repos.model.ReposUIViewModel
import org.mariusc.gitdemo.view.model.BaseUIAction
import org.mariusc.gitdemo.view.utils.ErrorResolver
import org.mariusc.system.log.ILogger


import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function

/**
 * Created by MConstantin on 5/1/2017.
 */

class ReposFragmentPresenter(private val reposInteractor: ReposInteractor,
                             private val logger: ILogger,
                             private val errorResolver: ErrorResolver,
                             private val mainScheduler: Scheduler = AndroidSchedulers.mainThread()) {

    private var reposUIViewModelObservable: Observable<ReposUIViewModel>? = null


    fun subscribeActionProvider(actionProvider: Observable<out BaseUIAction>,
                                consumer: Consumer<ReposUIViewModel>,
                                errorConsumer: Consumer<Throwable>): Disposable {


        if (reposUIViewModelObservable == null) {
            reposUIViewModelObservable = actionProvider
                    .publish { baseUiActionObservable ->
                        Observable.merge(
                                baseUiActionObservable
                                        .ofType(GetReposPage::class.java)
                                        .compose(reposPageComposer),
                                baseUiActionObservable
                                        .ofType(GetAllPagesToSince::class.java)
                                        .compose(reposAllPagesComposer))
                    }
                    .share()

        }
        return reposUIViewModelObservable!!
                .observeOn(mainScheduler)
                .subscribe(consumer, errorConsumer)
    }

    private val reposPageComposer = ObservableTransformer<GetReposPage, ReposUIViewModel> { upstream ->
        upstream
                .concatMap { (since) ->
                    reposInteractor
                            .publicReposPage(since)
                            .observeOn(mainScheduler)
                            .map(Function<ReposPage, ReposUIViewModel> { reposPage ->
                                ReposUIViewModel(isSuccess = true, page = reposPage)
                            })
                            .onErrorReturn { throwable ->
                                throwable.message?.let {
                                    logger.e(TAG, it)
                                }
                                ReposUIViewModel(errorMessage = errorResolver.resolveError(throwable), page = ReposPage())
                            }
                            .startWith(ReposUIViewModel(inProgress = true, page = ReposPage()))
                }

    }

    private val reposAllPagesComposer = ObservableTransformer<GetAllPagesToSince, ReposUIViewModel> { upstream ->
        upstream
                .concatMap { (since) ->
                    reposInteractor
                            .allPublicReposPagesUpUntil(since)
                            .observeOn(mainScheduler)
                            .map(Function<ReposPage, ReposUIViewModel> { reposPage ->
                                ReposUIViewModel(isSuccess = true, page = reposPage)

                            })
                            .onErrorReturn { throwable ->
                                throwable.message?.let {
                                    logger.e(TAG, it)
                                }
                                ReposUIViewModel(errorMessage = errorResolver.resolveError(throwable), page = ReposPage())
                            }
                            .startWith(ReposUIViewModel(inProgress = true, page = ReposPage()))
                }

    }

    companion object {

        private val TAG = ReposFragmentPresenter::class.java.simpleName
    }


}
