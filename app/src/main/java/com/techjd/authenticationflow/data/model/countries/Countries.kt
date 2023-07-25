package com.techjd.authenticationflow.data.model.countries


import com.google.gson.annotations.SerializedName

data class Countries(
    @SerializedName("access")
    val access: String,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String,
    @SerializedName("status-code")
    val statusCode: Int,
    @SerializedName("version")
    val version: String
)