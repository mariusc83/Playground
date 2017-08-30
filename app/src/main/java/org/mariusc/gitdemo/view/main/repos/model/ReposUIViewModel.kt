package org.mariusc.gitdemo.view.main.repos.model

import org.mariusc.gitdemo.data.network.model.ReposPage
import org.mariusc.gitdemo.view.model.UIViewModel


/**
 * Created by MConstantin on 5/1/2017.
 */

data class ReposUIViewModel(override val errorMessage: String = "", override val inProgress: Boolean = false, override val isSuccess: Boolean = false, val page: ReposPage) : UIViewModel {
}
