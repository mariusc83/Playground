package org.mariusc.gitdemo.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by MConstantin on 5/30/2017.
 */
data class RepoOwnerModel(@SerializedName("id") val id: Long = 0,
                          @SerializedName("avatar_url") val avatarUrl: String = "",
                          @SerializedName("login") val login: String = "")