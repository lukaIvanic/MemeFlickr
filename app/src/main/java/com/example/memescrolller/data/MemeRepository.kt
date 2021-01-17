package com.example.memescrolller.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.memescrolller.api.MemeApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemeRepository @Inject constructor(private val memeApi: MemeApi) {

    val memePagingSource: MemePagingSource = MemePagingSource(memeApi)

    fun getMeme() =
        Pager(
            config = PagingConfig(
                pageSize = 4,
                maxSize = 20,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { memePagingSource }
        ).liveData


}