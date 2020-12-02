package com.example.library.util

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.library.BaseActivity

fun BaseActivity.getColorHelper(@ColorRes id: Int) = ContextCompat.getColor(this, id)