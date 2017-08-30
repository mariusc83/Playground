package org.mariusc.gitdemo.data.network.model

/**
 * Created by MConstantin on 4/28/2017.
 */

interface Page {
    val isFirst:Boolean
    val hasMore: Boolean
    val data:List<RepoModel>
}
