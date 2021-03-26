package com.example.library.framework.util

import com.example.library.BaseActivity

object MessageDialogsHelper {

    fun BaseActivity.showSuccessfulBanner(message: String) {
        this.showBanner(
            message,
            BaseActivity.BannerType.SUCCESS
        )
    }

    fun BaseActivity.showErrorBanner(message: String) {
        this.showBanner(
            message,
            BaseActivity.BannerType.ERROR
        )
    }
}