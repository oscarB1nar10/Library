package com.example.library.states

sealed class State<T> {
    class Loading<T> : State<T>()

    data class Success<T>(val data: T) : State<T>() {

        var hasBeenHandled = false
            private set // Allow external read but not write

        /**
         * Returns the content and prevents its use again.
         */
        fun getContentIfNotHandled(): T? {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                data
            }
        }

        /**
         * Returns the content, even if it's already been handled.
         */
        fun peekContent(): T = data
    }

    data class Failed<T>(val message: String) : State<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }

    fun getWrappedData(state: Success<T>): T = state.data
}