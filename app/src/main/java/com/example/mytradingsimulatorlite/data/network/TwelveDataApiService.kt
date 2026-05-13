package com.example.mytradingsimulatorlite.data.network

import com.example.mytradingsimulatorlite.data.model.TwelveDataPriceResponse
import com.example.mytradingsimulatorlite.data.model.TwelveDataQuoteResponse
import com.example.mytradingsimulatorlite.data.model.TwelveDataTimeSeriesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TwelveDataApiService {
    @GET("price")
    suspend fun getPrices(
        @Query("symbol") symbols: String,
        @Query("apikey") apiKey: String
    ): Map<String, TwelveDataPriceResponse>

    @GET("time_series")
    suspend fun getTimeSeries(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("outputsize") outputSize: Int,
        @Query("apikey") apiKey: String
    ): TwelveDataTimeSeriesResponse

    @GET("quote")
    suspend fun getQuote(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): TwelveDataQuoteResponse
}
