package org.mariusc.gitdemo.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by MConstantin on 5/30/2017.
 */
data class  RepoModel(@SerializedName("id") val id: Int,
                     @SerializedName("name") val name: String = "",
                     @SerializedName("full_name") val fullName: String = "",
                     @SerializedName("description") val description: String = "",
                     @SerializedName("owner") val owner: RepoOwnerModel)