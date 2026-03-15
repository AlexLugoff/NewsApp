package com.example.newsapp.data.datasource.remote

import com.example.newsapp.data.RssApiService
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.data.models.RssFeedDto
import com.example.newsapp.extensions.SealedResult
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(
    private val apiService: RssApiService
) : NewsRemoteDataSource {
    override suspend fun getNewsFeed(url: String): SealedResult<RssFeedDto, DataError.Network> {
        return try {
            val dto = apiService.getNews(url)
            SealedResult.Success(dto)
        } catch (e: UnknownHostException) {
            SealedResult.Failure(DataError.Network.UNKNOWN_HOST)
        } catch (e: IOException) {
            SealedResult.Failure(DataError.Network.CONNECTION_TIMEOUT)
        } catch (e: HttpException) {
            SealedResult.Failure(DataError.Network.UNKNOWN)
        } catch (e: Exception) {
            SealedResult.Failure(DataError.Network.UNKNOWN)
        }
    }
}