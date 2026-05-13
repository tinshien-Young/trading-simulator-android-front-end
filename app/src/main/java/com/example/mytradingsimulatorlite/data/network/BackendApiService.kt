package com.example.mytradingsimulatorlite.data.network

import com.example.mytradingsimulatorlite.data.model.*
import retrofit2.http.*

interface BackendApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: AuthRequest): AuthResponse

    @GET("api/scenarios")
    suspend fun getScenarios(@Header("Authorization") auth: String): List<Scenario>

    @POST("api/portfolio")
    suspend fun initializePortfolio(
        @Header("Authorization") auth: String,
        @Body request: PortfolioInitializeRequest
    ): PortfolioResponse

    @GET("api/portfolio")
    suspend fun getPortfolio(@Header("Authorization") auth: String): PortfolioResponse

    @POST("api/portfolio/advance")
    suspend fun advanceDay(@Header("Authorization") auth: String): AdvanceResponse

    @POST("api/portfolio/reset")
    suspend fun resetPortfolio(@Header("Authorization") auth: String): PortfolioResponse

    @GET("api/stocks")
    suspend fun getStocks(@Header("Authorization") auth: String): List<StockInfo>

    @GET("api/prices")
    suspend fun getPrices(@Header("Authorization") auth: String): List<PriceResponse>

    @POST("api/orders/buy")
    suspend fun buyStock(
        @Header("Authorization") auth: String,
        @Body request: OrderRequest
    ): OrderResponse

    @POST("api/orders/sell")
    suspend fun sellStock(
        @Header("Authorization") auth: String,
        @Body request: OrderRequest
    ): OrderResponse

    @GET("api/orders")
    suspend fun getOrderHistory(@Header("Authorization") auth: String): List<OrderResponse>
}
