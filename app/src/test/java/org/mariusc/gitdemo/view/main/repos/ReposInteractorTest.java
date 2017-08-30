package org.mariusc.gitdemo.view.main.repos;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mariusc.gitdemo.data.network.repository.IReposRepository;
import org.mariusc.gitdemo.data.network.model.ReposPage;
import org.mariusc.gitdemo.utils.RxJavaScheduleOnTrampolineRule;
import org.mariusc.system.connectivity.ConnectivityState;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.BehaviorSubject;

import static org.mockito.BDDMockito.given;

/**
 * Created by MConstantin on 5/10/2017.
 */
@RunWith(Enclosed.class)
public class ReposInteractorTest {


    @RunWith(MockitoJUnitRunner.class)
    public static class A_Request_For_All_Public_Pages_Up_To {

        @Rule
        public RxJavaScheduleOnTrampolineRule rxJavaScheduleOnTrampolineRule=new
                RxJavaScheduleOnTrampolineRule();

        @Mock
        IReposRepository mockRepository;

        ReposPage mockPage1=new ReposPage();
        ReposPage mockPage2=new ReposPage();
        ReposPage mockPage3=new ReposPage();

        @Before
        public void setUp() throws Exception {

        }

        @Test
        public void returns_a_functional_observable_in_a_sunshine_scenario() throws Exception {
            // given
            final String since = "300";
            given(mockRepository.allPagesUpToSince(since)).willReturn(Observable.fromArray
                    (mockPage1, mockPage2, mockPage3));


            final Observable<ConnectivityState> observable = Observable.just(ConnectivityState.Companion
                    .active());
            final ReposInteractor reposInteractor = new ReposInteractor(mockRepository,
                    observable.share());


            // when
            final TestObserver<ReposPage> testObserver = reposInteractor.allPublicReposPagesUpUntil
                    (since).test();

            // then
            testObserver.assertValues(mockPage1, mockPage2, mockPage3);
            testObserver.assertComplete();
        }

        @Test
        public void returns_a_functional_observable_when_subscribing_multiple_times()
                throws Exception {
            // given
            final String since = "300";
            given(mockRepository.allPagesUpToSince(since))
                    .willReturn(Observable.fromArray(mockPage1, mockPage2, mockPage3));


            final Observable<ConnectivityState> observable = Observable.just(ConnectivityState.Companion
                    .active());
            final ReposInteractor reposInteractor = new ReposInteractor(mockRepository,
                    observable.share());


            // when
            final TestObserver<ReposPage> testObserver1 = reposInteractor
                    .allPublicReposPagesUpUntil(since)
                    .test();
            final TestObserver<ReposPage> testObserver2 = reposInteractor
                    .allPublicReposPagesUpUntil(since)
                    .test();

            // then
            testObserver1.assertValues(mockPage1, mockPage2, mockPage3);
            testObserver1.assertComplete();
            testObserver2.assertValues(mockPage1, mockPage2, mockPage3);
            testObserver2.assertComplete();
        }

        @Test
        public void replays_the_stream_when_connectivity_observable_sends_a_new_state()
                throws Exception {
            // given
            final String since = "300";
            given(mockRepository.allPagesUpToSince(since))
                    .willReturn(Observable.fromArray(mockPage1, mockPage2, mockPage3));


            final BehaviorSubject<ConnectivityState> observable = BehaviorSubject
                    .createDefault(ConnectivityState.Companion.active());
            final ReposInteractor reposInteractor = new ReposInteractor(mockRepository,
                    observable.share());
            final TestObserver<ReposPage> testObserver = reposInteractor
                    .allPublicReposPagesUpUntil(since)
                    .test();


            // when
            observable.onNext(ConnectivityState.Companion.notActive());

            // then
            testObserver.assertValues(mockPage1, mockPage2, mockPage3, mockPage1, mockPage2, mockPage3);
            testObserver.assertNotComplete();
        }

        @Test
        public void does_not_replay_the_stream_when_connectivity_observable_sends_same_state()
                throws Exception {
            // given
            final String since = "300";
            given(mockRepository.allPagesUpToSince(since))
                    .willReturn(Observable.fromArray(mockPage1, mockPage2, mockPage3));


            final BehaviorSubject<ConnectivityState> observable = BehaviorSubject
                    .createDefault(ConnectivityState.Companion.active());
            final ReposInteractor reposInteractor = new ReposInteractor(mockRepository,
                    observable.share());
            final TestObserver<ReposPage> testObserver = reposInteractor
                    .allPublicReposPagesUpUntil(since)
                    .test();


            // when
            observable.onNext(ConnectivityState.Companion.active());

            // then
            testObserver.assertValues(mockPage1, mockPage2, mockPage3);
            testObserver.assertNotComplete();
        }

    }
}