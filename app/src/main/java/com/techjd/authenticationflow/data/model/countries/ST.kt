package com.techjd.authenticationflow.data.model.countries


import com.google.gson.annotations.SerializedName

data class ST(
    @SerializedName("country")
    val country: String,
    @SerializedName("region")
    val region: String
)