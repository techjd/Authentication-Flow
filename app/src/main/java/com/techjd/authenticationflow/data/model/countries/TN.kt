package com.techjd.authenticationflow.data.model.countries


import com.google.gson.annotations.SerializedName

data class TN(
    @SerializedName("country")
    val country: String,
    @SerializedName("region")
    val region: String
)