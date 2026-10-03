package com.easytap.app.core

/** Every failure the user can hit maps to a typed error, so nothing fails silently. */
sealed interface AppError {
    data object NoInternet : AppError
    data object Timeout : AppError
    data object RateLimited : AppError
    data object ServerError : AppError
    data object AiUnavailable : AppError
    data object LowConfidence : AppError
    data object InvalidAiResponse : AppError
    data object PermissionDenied : AppError
    data object ScreenAnalysisFailed : AppError
}

sealed interface AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}
