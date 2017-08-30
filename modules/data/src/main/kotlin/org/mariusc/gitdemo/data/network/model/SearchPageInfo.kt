package org.mariusc.gitdemo.data.network.model


/**
 * Created by MConstantin on 4/28/2017.
 */

data class SearchPageInfo(val hasNext: Boolean = false,
                          val page: String = "",
                          val currentPage: String = "",
                          val nextPage: String = "",
                          val prevPage: String = "")
