package org.mariusc.gitdemo.view.main.repos

import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * Created by MConstantin on 7/26/2017.
 */
@RunWith(JUnitPlatform::class)
class TestClass : Spek({

    context("on a workable observable") {
        val subject: PublishSubject<String> = PublishSubject.create()

        it("will send the first event when reconnecting") {
            val obs = subject.replay(1).autoConnect()

            val observer1: TestObserver<String> = obs.test()
            val observer2: TestObserver<String> = obs.test()


            subject.onNext("test")



            observer1.assertValue { it == "test" }
            observer2.assertValue { it == "test" }

            observer1.dispose()

            subject.onNext("test1")

            observer2.assertValues("test", "test1")

            observer2.dispose()

            subject.onNext("test3")

            val observer3 = obs.test()

            observer3.assertValue { it == "test3" }

        }

    }
})