package com.example.library.util

fun String?.checkIsNullOrEmpty(): Boolean{
    return !this.isNullOrEmpty() && !this.equals("null", ignoreCase = true)
}
