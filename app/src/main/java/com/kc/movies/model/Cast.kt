package com.kc.movies.model

import com.google.gson.annotations.SerializedName

data class Cast(
        val id: Long,
        val character: String,
        val gender: Int,
        val name: String,
        @SerializedName("profile_path")
        val imagePath: String
)