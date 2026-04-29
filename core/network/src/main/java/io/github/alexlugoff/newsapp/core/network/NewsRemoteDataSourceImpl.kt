package io.github.alexlugoff.newsapp.core.network

import io.github.alexlugoff.newsapp.core.common.result.SealedResult
import io.github.alexlugoff.newsapp.core.common.error.DataError
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.exception.HttpException
import com.prof18.rssparser.model.RssChannel
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(
    private val rssParser: RssParser
) : NewsRemoteDataSource {
    override suspend fun getNewsFeed(url: String): SealedResult<RssChannel, DataError.Network> {
        return try {
            val channel = rssParser.getRssChannel(url)
            SealedResult.Success(channel)
        } catch (e: UnknownHostException) {
            SealedResult.Failure(DataError.Network.UnknownHost(e.localizedMessage))
        } catch (e: IOException) {
            SealedResult.Failure(DataError.Network.ConnectionTimeout(e.localizedMessage))
        } catch (e: HttpException) {
            SealedResult.Failure(
                DataError.Network.HttpError(
                    e.code,
                    e.localizedMessage ?: "Http Error"
                )
            )
        } catch (e: Exception) {
            SealedResult.Failure(DataError.Network.Unknown(e.localizedMessage))
        }
    }
}
