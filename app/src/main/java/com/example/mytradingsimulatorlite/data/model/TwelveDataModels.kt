package com.example.mytradingsimulatorlite.data.model

data class TwelveDataPriceResponse(
    val price: String
)

data class TwelveDataTimeSeriesResponse(
    val values: List<TwelveDataPricePoint>?
)

data class TwelveDataPricePoint(
    val datetime: String?,
    val close: String?
)

data class TwelveDataQuoteResponse(
    val symbol: String?,
    val bid: String?,
    val ask: String?,
    val price: String?
)
