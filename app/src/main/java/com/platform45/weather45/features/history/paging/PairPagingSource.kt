package com.platform45.weather45.features.history.paging

import androidx.paging.PagingSource
import com.platform45.weather45.constants.API_KEY
import com.platform45.weather45.features.history.HistoryViewModel
import com.platform45.weather45.models.PairTradeHistory
import com.platform45.weather45.persistance.room.tables.pairHistory.PairHistoryTable
import com.platform45.weather45.repositories.FXRepository

class PairPagingSource(val startDate: String, val endDate: String, val currency: String, val fXRepository: FXRepository) : PagingSource<Int, PairHistoryTable>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PairHistoryTable> = try {
        val loadPage = params.key ?: 0

        val response = fXRepository.getSeriesCache(
            API_KEY,
            startDate,
            endDate,
            currency,
            "ohlc") as ArrayList

        LoadResult.Page(
            data = response,
            prevKey = null,
            nextKey = loadPage + 1
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

}