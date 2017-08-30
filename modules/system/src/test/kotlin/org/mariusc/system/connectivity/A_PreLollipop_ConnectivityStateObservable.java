/*
package org.mariusc.system.connectivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mariusc.system.RxJavaScheduleOnTrampolineRule;
import org.mariusc.system.connectivity.supplier.PreLollipopSupplier;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

*/
/**
 * Created by MConstantin on 4/4/2017.
 *//*



@RunWith(MockitoJUnitRunner.Silent.class)
public class A_PreLollipop_ConnectivityStateObservable extends AConnectivityStateObservableToTest {
    @Rule
    public RxJavaScheduleOnTrampolineRule trampolineRule = new RxJavaScheduleOnTrampolineRule();


    @Before
    public void setUp() throws Exception{
        // given
        given(getMockContext().getSystemService(Context.CONNECTIVITY_SERVICE)).willReturn
                (getMockConnectivityManager());
        given(getMockContext().registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class)))
                .willAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        setReceiver((invocation.getArgument(0)));
                        return getMockIntent();
                    }
                });
        setSupplier(new PreLollipopSupplier(getMockContext()));
    }

    @Test
    public void keeps_receiving_states_when_connectivity_state_changes() throws Exception {
        // given
        final NetworkInfo[] networkInfos = {getMockNetworkInfo()};
        given(getMockConnectivityManager().getAllNetworkInfo()).willReturn(networkInfos);
        ConnectivityStateObservable observable = new ConnectivityStateObservable(getSupplier());
        final TestObserver<ConnectivityState> observer = observable.test();
        given(getMockIntent().getAction()).willReturn(ConnectivityManager.CONNECTIVITY_ACTION);

        // when
        getReceiver().onReceive(getMockContext(), getMockIntent());

        // then
        observer.assertValueCount(2);
        final List<ConnectivityState> values = observer.values();
        assertThat(values.get(0).networks()).containsExactly(getMockNetworkInfo());
        assertThat(values.get(1).networks()).containsExactly(getMockNetworkInfo());
    }

    @Test
    public void stops_emitting_events_when_disposed() throws Exception {
        // given
        final NetworkInfo[] networkInfos = {getMockNetworkInfo()};
        given(getMockConnectivityManager().getAllNetworkInfo()).willReturn(networkInfos);
        ConnectivityStateObservable observable = new ConnectivityStateObservable(getSupplier());
        final TestObserver<ConnectivityState> observer = observable.test();
        given(getMockIntent().getAction()).willReturn(ConnectivityManager.CONNECTIVITY_ACTION);
        observer.dispose();

        // when
        getReceiver().onReceive(getMockContext(), getMockIntent());

        // then
        observer.assertValueCount(1);
    }


    @Test
    public void ignores_states_emitted_by_an_unknown_broadcaster() throws Exception {
        // given
        final NetworkInfo[] networkInfos = {getMockNetworkInfo()};
        given(getMockConnectivityManager().getAllNetworkInfo()).willReturn(networkInfos);
        ConnectivityStateObservable observable = new ConnectivityStateObservable(getSupplier());
        final TestObserver<ConnectivityState> observer = observable.test();
        given(getMockIntent().getAction()).willReturn(null);

        // when
        getReceiver().onReceive(getMockContext(), getMockIntent());

        // then
        observer.assertValueCount(1);
    }

}*/
