package org.mariusc.gitdemo.view.main.repos;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mariusc.gitdemo.data.network.repository.ReposRepository;
import org.mariusc.gitdemo.data.network.model.ReposPage;
import org.mariusc.gitdemo.provider.TextProvider;
import org.mariusc.gitdemo.view.main.repos.model.ReposUIViewModel;
import org.mariusc.gitdemo.view.model.BaseUIAction;
import org.mariusc.gitdemo.view.utils.ErrorResolver;
import org.mariusc.system.connectivity.ConnectivityState;
import org.mariusc.system.log.ILogger;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by MConstantin on 5/10/2017.
 */
@RunWith(Enclosed.class)
public class ReposFragmentPresenterTest {

    public static abstract class AReposFragmentPresenter {
        @Mock
        Consumer<ReposUIViewModel> mockConsumer;

        @Mock
        Consumer<Throwable> mockThrowableConsumer;

        @Mock
        ReposRepository mockReposRepository;

        BehaviorSubject<ConnectivityState> connectivityStateSubject = BehaviorSubject.create();

        ReposInteractor reposInteractor;

        ReposFragmentPresenter presenter;

        @Mock
        TextProvider mockTextProvider;

        @Mock
        ErrorResolver mockErrorResolver;

        @Mock
        ILogger mockLogger;


    }

    @RunWith(RobolectricTestRunner.class)
    public static class A_ReposFragmentPresenter_For_A_GetReposPage_Action extends AReposFragmentPresenter {

        private static final String since = "0";

        PublishSubject<BaseUIAction> publishSubject = PublishSubject.create();
        private final ReposPage mockReposPage = new ReposPage();

        @Before
        public void setUp() {
            MockitoAnnotations.initMocks(this);
            given(mockReposRepository.publicRepositories(since)).willReturn(Single.just
                    (mockReposPage));
            reposInteractor = new ReposInteractor(mockReposRepository, connectivityStateSubject);
            presenter = new ReposFragmentPresenter(reposInteractor, mockLogger,
                    mockErrorResolver, Schedulers.trampoline());

        }


        @Test
        public void returns_a_disposable_when_subscribe_to() throws Exception {
            // when
            final Disposable disposable = presenter.subscribeActionProvider(publishSubject,
                    mockConsumer, mockThrowableConsumer);

            // then
            assertThat(disposable).isNotNull();
        }

    }

}