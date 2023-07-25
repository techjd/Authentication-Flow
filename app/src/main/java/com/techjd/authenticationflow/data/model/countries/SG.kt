package com.techjd.authenticationflow.data.model.countries


import com.google.gson.annotations.SerializedName

data class SG(
    @SerializedName("country")
    val country: String,
    @SerializedName("region")
    val region: String
)