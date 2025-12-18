package com.example.newsapp.data.exception

sealed interface DomainError

sealed interface DataError : DomainError {
    enum class Network : DataError {
        UNKNOWN_HOST, CONNECTION_TIMEOUT, UNKNOWN
    }
    enum class Local : DataError {
        NOT_FOUND, UNKNOWN
    }
    enum class Parser : DataError {
        INVALID_FORMAT
    }
}