package com.platform45.weather45.models

import com.google.gson.annotations.SerializedName

data class DailyWeatherDetails(
    @SerializedName("sunrise") var sunrise: Int?,
    @SerializedName("sunset") var sunset: Int?,
    @SerializedName("temp") var temp: Temp?,
    @SerializedName("feels_like") var feelsLike: FeelsLike?,
    @SerializedName("pressure") var pressure: Int?,
    @SerializedName("humidity") var humidity: Int?,
    @SerializedName("weather") var weather: List<Weather?>?,
    @SerializedName("speed") var speed: Double?,
    @SerializedName("deg") var deg: Int?,
    @SerializedName("gust") var gust: Double?,
    @SerializedName("gust") var clouds: Int?,
    @SerializedName("pop") var pop: Int?,
)
