package org.mariusc.gitdemo.view.main.repos

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.mariusc.gitdemo.data.network.repository.IReposRepository
import org.mariusc.gitdemo.view.main.repos.model.GetReposPage
import org.mariusc.gitdemo.view.main.repos.model.ReposUIViewModel
import org.mariusc.gitdemo.view.model.BaseUIAction
import org.mariusc.gitdemo.view.utils.ErrorResolver
import org.mariusc.system.connectivity.ConnectivityState
import org.mariusc.system.log.ILogger
import org.assertj.core.api.Java6Assertions.assertThat
import org.jetbrains.spek.api.dsl.context
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mariusc.gitdemo.data.network.model.ReposPage
import org.mariusc.gitdemo.rxInTestMode
import org.mariusc.gitdemo.rxOutFromTestMode
import org.mariusc.gitdemo.view.main.repos.model.GetAllPagesToSince
import kotlin.properties.Delegates

/**
 * Created by MConstantin on 7/17/2017.
 */
@RunWith(JUnitPlatform::class)
class KReposFragmentPresenterTest : Spek({

    beforeGroup {
        rxInTestMode()
    }

    given("A Functional FragmentPresenter") {
        val statePublishSubject: BehaviorSubject<ConnectivityState> = BehaviorSubject.createDefault(ConnectivityState(true))
        val mockRepository: IReposRepository = mock()
        val mockErrorResolver: ErrorResolver = mock()

        val mockLogger: ILogger = mock()
        val interactor: ReposInteractor = ReposInteractor(mockRepository, statePublishSubject)
        val presenter = ReposFragmentPresenter(interactor, mockLogger, mockErrorResolver, Schedulers.trampoline())
        val uiActionPublishSubject: PublishSubject<BaseUIAction> = PublishSubject.create()
        val error = RuntimeException("test")

        val onUIEventConsumer: Consumer<ReposUIViewModel> by lazy {
            mock<Consumer<ReposUIViewModel>>()
        }
        val onUIEventErrorConsumer: Consumer<Throwable> by lazy {
            mock<Consumer<Throwable>>()
        }
        var disposable: Disposable by Delegates.notNull<Disposable>()

        beforeEachTest {
            disposable = presenter.subscribeActionProvider(uiActionPublishSubject, onUIEventConsumer,
                    onUIEventErrorConsumer)
            whenever(mockErrorResolver.resolveError(any())).thenReturn(error.message)
        }

        context("when requesting the first page") {
            val page = ReposPage()

            it("will dispatch the right events when requesting first page") {
                whenever(mockRepository.publicRepositories(eq(""))).thenReturn(Single.just(page))
                // when
                uiActionPublishSubject.onNext(GetReposPage())

                // then
                argumentCaptor<ReposUIViewModel>().apply {
                    verify(onUIEventConsumer, times(2)).accept(capture())
                    assertThat(allValues.size).isEqualTo(2)
                    assertThat(firstValue.inProgress).isTrue()
                    assertThat(firstValue.isSuccess).isFalse()
                    assertThat(secondValue.inProgress).isFalse()
                    assertThat(secondValue.isSuccess).isTrue()
                    assertThat(secondValue.page).isEqualTo(page)

                }
            }

            it("will dispatch right events when encountering an error on upper stream") {
                whenever(mockRepository.publicRepositories(eq(""))).thenReturn(Single.error(error))
                // when
                uiActionPublishSubject.onNext(GetReposPage())

                // then
                argumentCaptor<ReposUIViewModel>().apply {
                    verify(onUIEventConsumer, times(2)).accept(capture())
                    assertThat(allValues.size).isEqualTo(2)
                    assertThat(firstValue.inProgress).isTrue()
                    assertThat(firstValue.isSuccess).isFalse()
                    assertThat(secondValue.errorMessage).isEqualTo("test")
                    assertThat(secondValue.inProgress).isFalse()
                    assertThat(secondValue.isSuccess).isFalse()
                }

            }


        }

        context("when requesting first and second page") {
            val page = ReposPage()

            it("will dispatch the right events") {
                whenever(mockRepository.publicRepositories(eq(""))).thenReturn(Single.just(page))
                whenever(mockRepository.publicRepositories(eq("100"))).thenReturn(Single.just(page))
                // when
                uiActionPublishSubject.onNext(GetReposPage())
                uiActionPublishSubject.onNext(GetReposPage("100"))

                // then
                argumentCaptor<ReposUIViewModel>().apply {
                    verify(onUIEventConsumer, times(4)).accept(capture())
                    assertThat(allValues.size).isEqualTo(4)
                    assertThat(firstValue.inProgress).isTrue()
                    assertThat(firstValue.isSuccess).isFalse()
                    assertThat(secondValue.inProgress).isFalse()
                    assertThat(secondValue.isSuccess).isTrue()
                    assertThat(secondValue.page).isEqualTo(page)
                    assertThat(thirdValue.inProgress).isTrue()
                    assertThat(thirdValue.isSuccess).isFalse()
                    assertThat(allValues.get(3).inProgress).isFalse()
                    assertThat(allValues.get(3).isSuccess).isTrue()
                    assertThat(allValues.get(3).page).isEqualTo(page)
                }

            }
        }

        context("when requesting all pages up to last") {
            val page1 = ReposPage()
            val page2 = ReposPage()
            val page3 = ReposPage()
            val since = "3000"
            val requestAction = GetAllPagesToSince(since)

            it("will dispatch right events") {

                whenever(mockRepository.allPagesUpToSince(eq(since))).thenReturn(Observable.just
                (page1, page2, page3))

                // when
                uiActionPublishSubject.onNext(requestAction)

                // then
                argumentCaptor<ReposUIViewModel>().apply {
                    verify(onUIEventConsumer, times(4)).accept(capture());
                    assertThat(allValues.size).isEqualTo(4)
                    assertThat(firstValue.inProgress).isTrue()
                    assertThat(firstValue.isSuccess).isFalse()
                    assertThat(allValues.get(1).inProgress).isFalse()
                    assertThat(allValues.get(1).isSuccess).isTrue()
                    assertThat(allValues.get(1).page).isEqualTo(page1);
                    assertThat(allValues.get(2).inProgress).isFalse()
                    assertThat(allValues.get(2).isSuccess).isTrue()
                    assertThat(allValues.get(2).page).isEqualTo(page2);
                    assertThat(allValues.get(3).inProgress).isFalse()
                    assertThat(allValues.get(3).isSuccess).isTrue()
                    assertThat(allValues.get(3).page).isEqualTo(page3);

                }
            }

            it("will dispatch when an error was dispatch from upper") {
                whenever(mockRepository.allPagesUpToSince(eq(since))).thenReturn(Observable.merge
                (Observable.just(page1), Observable.error(error), Observable.just(page2)))

                // when
                uiActionPublishSubject.onNext(requestAction)

                // then
                argumentCaptor<ReposUIViewModel>().apply {
                    verify(onUIEventConsumer, times(3)).accept(capture())
                    assertThat(allValues.size).isEqualTo(3)
                    assertThat(firstValue.inProgress).isTrue()
                    assertThat(firstValue.isSuccess).isFalse()
                    assertThat(allValues.get(1).inProgress).isFalse()
                    assertThat(allValues.get(1).isSuccess).isTrue()
                    assertThat(allValues.get(1).page).isEqualTo(page1)
                    assertThat(allValues.get(2).inProgress).isFalse()
                    assertThat(allValues.get(2).isSuccess).isFalse()
                    assertThat(allValues.get(2).errorMessage).isEqualTo(error.message)
                }

            }

            it("will dispatch the right events when the connectivity state changed") {
                whenever(mockRepository.allPagesUpToSince(eq(since))).thenReturn(Observable.just(page1))

                // when
                uiActionPublishSubject.onNext(requestAction)
                statePublishSubject.onNext(ConnectivityState(false))
                statePublishSubject.onNext(ConnectivityState(false))

                // then
                argumentCaptor<ReposUIViewModel>().apply {
                    verify(onUIEventConsumer, times(3)).accept(capture())
                    assertThat(allValues.size).isEqualTo(3)
                    assertThat(firstValue.inProgress).isTrue()
                    assertThat(firstValue.isSuccess).isFalse()
                    assertThat(allValues.get(1).inProgress).isFalse()
                    assertThat(allValues.get(1).isSuccess).isTrue()
                    assertThat(allValues.get(1).page).isEqualTo(page1)
                    assertThat(allValues.get(2).inProgress).isFalse()
                    assertThat(allValues.get(2).isSuccess).isTrue()
                    assertThat(allValues.get(2).page).isEqualTo(page1)
                }

            }

        }



        afterEachTest {
            disposable.dispose()
            reset(mockRepository)
            reset(onUIEventConsumer)
            reset(onUIEventErrorConsumer)
        }
    }

    afterGroup {
        rxOutFromTestMode()
    }
})