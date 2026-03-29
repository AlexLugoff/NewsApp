package com.example.newsapp.core.common.error

sealed interface DataError {

    sealed interface Network : DataError {
        data class UnknownHost(val message: String? = null) : Network
        data class ConnectionTimeout(val message: String? = null) : Network
        data class Unknown(val message: String? = null) : Network
        data class HttpError(val code: Int, val message: String) : Network
    }

    sealed interface Local : DataError {
        data class NotFound(val message: String? = null) : Local
        data class Unknown(val message: String? = null) : Local
    }

    sealed interface Parser : DataError {
        data class InvalidFormat(val message: String, val source: String? = null) : Parser
    }
}