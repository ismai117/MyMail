package org.ncgroup.versereach.utils


sealed class ResultState<T>(
    val data: T? = null,
    val message: String = ""
) {
    class Loading<T>(data: T? = null): ResultState<T>(data = data)
    class Success<T>(data: T?) : ResultState<T>(data = data)
    class Error<T>(message: String, data: T? = null) : ResultState<T>(data = data, message = message)
}