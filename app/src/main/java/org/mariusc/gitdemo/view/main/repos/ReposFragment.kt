package org.mariusc.gitdemo.view.main.repos

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.mariusc.gitdemo.R
import org.mariusc.gitdemo.data.network.model.ReposPageInfo
import org.mariusc.gitdemo.databinding.ReposFragmentLayoutBinding
import org.mariusc.gitdemo.view.adapter.binding.DefaultRowBindingComponent
import org.mariusc.gitdemo.view.main.repos.adapter.ReposAdapter
import org.mariusc.gitdemo.view.main.repos.model.GetAllPagesToSince
import org.mariusc.gitdemo.view.main.repos.model.GetReposPage
import org.mariusc.gitdemo.view.main.repos.model.ReposUIViewModel
import org.mariusc.gitdemo.view.model.BaseUIAction

import javax.inject.Inject

import dagger.android.support.DaggerFragment
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.Exceptions
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

/**
 * Created by MConstantin on 5/1/2017.
 */

class ReposFragment : DaggerFragment() {
    private val TO_RESUME_ACTION_KEY = "resume_action_key"

    @set:Inject
    lateinit var presenter: ReposFragmentPresenter

    lateinit internal var reposAdapter: ReposAdapter

    lateinit var binding: ReposFragmentLayoutBinding

    private val uiActionBehaviorSubject = PublishSubject.create<BaseUIAction>()
    private var disposable: Disposable? = null
    private var currentReposPage: ReposPageInfo? = null
    private var toPersistBaseUiAction: BaseUIAction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reposAdapter = ReposAdapter(getLayoutInflater(), DefaultRowBindingComponent())
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ReposFragmentLayoutBinding>(inflater!!, R.layout.repos_fragment_layout,
                container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.layoutManager = LinearLayoutManager(this.context)
        binding.list.adapter = reposAdapter
        reposAdapter.setInfiniteAdapterCallback(object : ReposAdapter.InfiniteAdapterCallback {
            override fun onRequestMoreItems() {
                currentReposPage?.let {
                    val event = GetReposPage(it.nextSince)
                    toPersistBaseUiAction = GetAllPagesToSince(it.nextSince)
                    uiActionBehaviorSubject.onNext(event)
                }
            }
        })
        binding.swiperefresh.setOnRefreshListener {
            val event = GetReposPage("0")
            uiActionBehaviorSubject.onNext(event)
            toPersistBaseUiAction = event
        }
        if (savedInstanceState != null) {
            toPersistBaseUiAction = savedInstanceState.getParcelable<BaseUIAction>(TO_RESUME_ACTION_KEY)
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (toPersistBaseUiAction != null) {
            outState!!.putParcelable(TO_RESUME_ACTION_KEY, toPersistBaseUiAction)
        }
    }

    override fun onResume() {
        super.onResume()
        disposable = presenter
                .subscribeActionProvider(uiActionBehaviorSubject, Consumer<ReposUIViewModel> { reposUIViewModel ->
                    //currentReposPage = null;
                    if (reposUIViewModel.inProgress) {
                        binding.swiperefresh.isEnabled = false
                        binding.swiperefresh.isRefreshing = true
                        return@Consumer
                    } else {
                        binding.swiperefresh.isEnabled = true
                        binding.swiperefresh.isRefreshing = false
                    }

                    if (reposUIViewModel.isSuccess) {
                        currentReposPage = reposUIViewModel.page.pageInfo
                        reposAdapter.setData(reposUIViewModel.page)
                    }
                }, Consumer<Throwable> { throwable -> Exceptions.throwIfFatal(throwable) })

        if (toPersistBaseUiAction != null) {
            uiActionBehaviorSubject.onNext(toPersistBaseUiAction)
        } else {
            val event = GetReposPage("0")
            uiActionBehaviorSubject.onNext(event)
            toPersistBaseUiAction = event
        }
    }

    override fun onPause() {
        disposable!!.dispose()
        super.onPause()
    }
}
