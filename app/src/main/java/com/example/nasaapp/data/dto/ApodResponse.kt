package com.example.nasaapp.data.dto

import com.google.gson.annotations.SerializedName

data class ApodResponse(
    @SerializedName("date")
    val date: String,
    @SerializedName("explanation")
    val explanation: String,
    @SerializedName("url")
    val imageHD: String,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("service_version")
    val serviceVersion: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("copyright")
    val copyright: String?
)