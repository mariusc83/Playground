package org.mariusc.gitdemo.data.network.repository

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import okhttp3.Headers
import org.jetbrains.spek.api.Spek
import org.mariusc.gitdemo.data.mock
import org.mariusc.gitdemo.data.network.IGitApiService
import org.mariusc.gitdemo.data.network.model.RepoModel
import org.mariusc.gitdemo.data.network.repository.parse.ReposPageParser
import org.mariusc.gitdemo.data.rxInTestMode
import org.mariusc.gitdemo.data.rxOutFromTestMode
import retrofit2.Response
import org.assertj.core.api.Java6Assertions.assertThat
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mariusc.gitdemo.data.network.model.RepoOwnerModel
import org.mariusc.gitdemo.data.network.model.ReposPage
import kotlin.properties.Delegates
import kotlin.RuntimeException

/**
 * Created by MConstantin on 6/13/2017.
 */
class ReposRepositoryTest : Spek({

    beforeGroup {
        rxInTestMode()
    }

    given("A Request For Single Repositories Page") {
        val model1: RepoModel = RepoModel(1, owner = RepoOwnerModel())
        val model2: RepoModel = RepoModel(2, owner = RepoOwnerModel())
        val mockGitApiService: IGitApiService = mock<IGitApiService>()
        val responseData: List<RepoModel> = listOf(model1, model2)
        var reposRepository: ReposRepository by Delegates.notNull<ReposRepository>()


        on("first page request in sunshine scenario") {
            val since = "0"
            val nextSince = "300"
            val nextUrl = "https://api.github.com/repositories?since=" + nextSince
            val firstUrl = "https://api.github.com/repositories{?since}"
            val header = "Link:<" + nextUrl + ">; rel=\"next\"," +
                    " <" + firstUrl + ">; rel=\"first\""
            val successResponse: Response<List<RepoModel>> = Response.success(responseData,
                    Headers.Builder().add(header).build())

            it("will return a valid observable") {
                // when
                reposRepository = ReposRepository(NetworkReposRepository(mockGitApiService, ReposPageParser()))
                whenever(mockGitApiService.fetchRepositories(eq(since))).thenReturn(Observable.just(successResponse))
                val testObserver = reposRepository!!.publicRepositories().test();


                // then
                testObserver.assertValueCount(1);
                testObserver.assertNoErrors();

                val page = testObserver.values().get(0);
                assertThat(page.hasMore).isTrue();
                assertThat(page.isFirst).isTrue();
                assertThat(page.pageInfo.currentSince).isEqualTo(since);
                assertThat(page.pageInfo.nextSince).isEqualTo(nextSince);
                assertThat(page.pageInfo.hasNext).isTrue();
                assertThat(page.data).containsExactlyElementsOf(responseData);

            }
        }


        on("next page request in sunshine scenario") {
            val since = "300"
            val nextSince = "600"
            val nextUrl = "https://api.github.com/repositories?since=" + nextSince
            val firstUrl = "https://api.github.com/repositories{?since}"
            val header = "Link:<" + nextUrl + ">; rel=\"next\"," +
                    " <" + firstUrl + ">; rel=\"first\""
            val successResponse: Response<List<RepoModel>> = Response.success(responseData,
                    Headers.Builder().add(header).build())

            it("will return a valid observable") {
                // when
                whenever(mockGitApiService.fetchRepositories(eq(since))).thenReturn(Observable.just(successResponse))
                val testObserver = reposRepository!!.publicRepositories(since).test();

                // then
                testObserver.assertValueCount(1);
                testObserver.assertNoErrors();

                val page = testObserver.values().get(0);
                assertThat(page.hasMore).isTrue();
                assertThat(page.isFirst).isFalse();
                assertThat(page.pageInfo.currentSince).isEqualTo(since);
                assertThat(page.pageInfo.nextSince).isEqualTo(nextSince);
                assertThat(page.pageInfo.hasNext).isTrue();
                assertThat(page.data).containsExactlyElementsOf(responseData);
            }
        }

        on("last page request in sunshine scenario") {
            val since = "300"
            val firstUrl = "https://api.github.com/repositories{?since}"
            val header = "Link:<$firstUrl>; rel=\"first\""
            val successResponse: Response<List<RepoModel>> = Response.success(responseData,
                    Headers.Builder().add(header).build())

            it("will return a valid observable") {
                // when
                whenever(mockGitApiService.fetchRepositories(eq(since))).thenReturn(Observable.just(successResponse))

                val testObserver = reposRepository!!.publicRepositories(since).test();


                // then
                testObserver.assertValueCount(1);
                testObserver.assertNoErrors();

                val page = testObserver.values().get(0);
                assertThat(page.hasMore).isFalse();
                assertThat(page.isFirst).isFalse();
                assertThat(page.pageInfo.currentSince).isEqualTo(since);
                assertThat(page.pageInfo.nextSince).isEmpty();
                assertThat(page.pageInfo.hasNext).isFalse();
                assertThat(page.data).containsExactlyElementsOf(responseData);

            }
        }

    }

    given("A Request For All Repositories Pages Up To A Since") {
        val model1: RepoModel = RepoModel(1, owner = RepoOwnerModel())
        val model2: RepoModel = RepoModel(2, owner = RepoOwnerModel())
        val mockGitApiSerice: IGitApiService = mock<IGitApiService>()

        var successResponse1: Response<List<RepoModel>> by Delegates
                .notNull<Response<List<RepoModel>>>()
        var successResponse2: Response<List<RepoModel>> by Delegates
                .notNull<Response<List<RepoModel>>>()
        var successResponse3: Response<List<RepoModel>> by Delegates
                .notNull<Response<List<RepoModel>>>()
        val responseData: List<RepoModel> = listOf(model1, model2)
        val exception = RuntimeException("Test Exception")
        var reposRepository by Delegates.notNull<ReposRepository>()

        val since0 = "0"
        val since1 = "100"
        val since2 = "200"
        val since3 = "300"


        on("all pages to last one request in sunshine scenario") {

            it("will return a valid observable") {
                // when
                val next1Url = "https://api.github.com/repositories?since=$since1"
                val next2Url = "https://api.github.com/repositories?since=$since2"
                val next3Url = "https://api.github.com/repositories?since=$since3"
                val firstUrl = "https://api.github.com/repositories{?since}";
                val header1 = "Link:<" + next1Url + ">; rel=\"next\"," +
                        " <" + firstUrl + ">; rel=\"first\"";
                val header2 = "Link:<" + next2Url + ">; rel=\"next\"," +
                        " <" + firstUrl + ">; rel=\"first\"";
                val header3 = "Link:<" + next3Url + ">; rel=\"next\"," +
                        " <" + firstUrl + ">; rel=\"first\"";

                successResponse1 = Response.success(responseData, Headers.Builder().add(header1).build())
                successResponse2 = Response.success(responseData, Headers.Builder().add(header2)
                        .build())
                successResponse3 = Response.success(responseData, Headers.Builder().add(header3)
                        .build())

                reposRepository = ReposRepository(NetworkReposRepository(mockGitApiSerice,
                        ReposPageParser()))

                whenever(mockGitApiSerice.fetchRepositories(eq(since0))).thenReturn(Observable.just
                (successResponse1));
                whenever(mockGitApiSerice.fetchRepositories(eq(since1))).thenReturn(Observable.just
                (successResponse2));
                whenever(mockGitApiSerice.fetchRepositories(eq(since2))).thenReturn(Observable.just
                (successResponse3));

                val testObserver: TestObserver<ReposPage> = reposRepository.allPagesUpToSince(since2)
                        .test();

                // then
                testObserver.assertValueCount(3);
                testObserver.assertNoErrors();
                testObserver.assertComplete();
                val page1 = testObserver.values().get(0);
                assertThat(page1.data).containsExactlyElementsOf(responseData);
                assertThat(page1.pageInfo.currentSince).isEqualTo("0");
                val page2 = testObserver.values().get(1);
                assertThat(page2.data).containsExactlyElementsOf(responseData);
                val page3 = testObserver.values().get(2);
                assertThat(page3.data).containsExactlyElementsOf(responseData);

            }
        }

        on("all pages to last one request when the third request crashes") {

            it("will return an observable which emits the first 2 events followed by a " +
                    "RuntimeException") {

                // when
                val next1Url = "https://api.github.com/repositories?since=$since1"
                val next2Url = "https://api.github.com/repositories?since=$since2"
                val next3Url = "https://api.github.com/repositories?since=$since3"
                val firstUrl = "https://api.github.com/repositories{?since}";
                val header1 = "Link:<" + next1Url + ">; rel=\"next\"," +
                        " <" + firstUrl + ">; rel=\"first\"";
                val header2 = "Link:<" + next2Url + ">; rel=\"next\"," +
                        " <" + firstUrl + ">; rel=\"first\"";
                val header3 = "Link:<" + next3Url + ">; rel=\"next\"," +
                        " <" + firstUrl + ">; rel=\"first\"";


                successResponse1 = Response.success(responseData, Headers.Builder().add(header1).build())
                successResponse2 = Response.success(responseData, Headers.Builder().add(header2)
                        .build())
                successResponse3 = Response.success(responseData, Headers.Builder().add(header3)
                        .build())

                reposRepository = ReposRepository(NetworkReposRepository(mockGitApiSerice,
                        ReposPageParser()))

                whenever(mockGitApiSerice.fetchRepositories(eq(since0))).thenReturn(Observable.just
                (successResponse1));
                whenever(mockGitApiSerice.fetchRepositories(eq(since1))).thenReturn(Observable.just
                (successResponse2));
                whenever(mockGitApiSerice.fetchRepositories(eq(since2))).thenReturn(Observable
                        .error(exception))

                val testObserver: TestObserver<ReposPage> = reposRepository.allPagesUpToSince(since2)
                        .test()

                // then
                testObserver.assertValueCount(2)
                val page1 = testObserver.values().get(0);
                assertThat(page1.data).containsExactlyElementsOf(responseData);
                assertThat(page1.pageInfo.currentSince).isEqualTo(since0);
                val page2 = testObserver.values().get(1);
                assertThat(page2.data).containsExactlyElementsOf(responseData);
                assertThat(page2.pageInfo.currentSince).isEqualTo(since1);
                testObserver.assertError(exception)

            }
        }

    }

    afterGroup {
        rxOutFromTestMode()
    }

})
