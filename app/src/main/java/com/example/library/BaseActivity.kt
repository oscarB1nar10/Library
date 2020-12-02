package com.example.library

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.example.library.util.OnSwipeTouchListener
import com.example.library.util.getColorHelper
import kotlinx.android.synthetic.main.layout_dialog.view.*
import kotlinx.android.synthetic.main.layout_toolbar_component.*

abstract class BaseActivity : AppCompatActivity() {

    //region Banner-related entities

    private var popupWindow: PopupWindow? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    enum class BannerType {
        SUCCESS,
        WARNING,
        ERROR,
        INFORMATION
    }

    companion object {
        internal const val INTERVAL_POPUP_AUTO_DISMISS = 3000
        internal const val LONG_INTERVAL_POPUP_AUTO_DISMISS = 6000
    }

    abstract fun getLayoutResourceId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())

        setSupportActionBar(my_toolbar)
    }

    protected open fun setupToolbar() {
        // screens with toolbar should override this method
        setupBackButton()
    }

    fun showBanner(
        message: String,
        bannerType: BannerType,
        dismissible: Boolean = true,
        hasIcon: Boolean = true
    ) {
        showBanner(
            message,
            null,
            bannerType,
            INTERVAL_POPUP_AUTO_DISMISS.toLong(),
            dismissible,
            hasIcon
        )
    }

    fun showBanner(
        message: String?,
        url: String?,
        bannerType: BannerType,
        duration: Long,
        dismissible: Boolean = true,
        hasIcon: Boolean = true
    ) {
        runOnUiThread {
            try {
                val inflater = layoutInflater
                val layout = inflater.inflate(
                    R.layout.layout_dialog,
                    findViewById(R.id.toast_layout_root)
                )
                val text = layout.text
                text.text = message
                if (dismissible) {
                    layout.iv_close.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            popupWindow?.dismiss()
                        }
                    }
                } else {
                    layout.iv_close.visibility = View.GONE
                }
                when (bannerType) {
                    BannerType.SUCCESS -> {
                        layout.setBackgroundColor(getColorHelper(R.color.positive_100))
                        if (hasIcon) {
                            (layout.iv_type as ImageView).apply {
                                visibility = View.VISIBLE
                                setImageResource(R.drawable.ic_checkout_success)
                            }
                        } else {
                            layout.iv_type.visibility = View.GONE
                        }
                    }
                    BannerType.WARNING -> {
                        layout.setBackgroundColor(getColorHelper(R.color.warning_100))
                        if (hasIcon) {
                            (layout.iv_type as ImageView).apply {
                                visibility = View.VISIBLE
                                setImageResource(R.drawable.ic_checkout_warning)
                            }
                        } else {
                            layout.iv_type.visibility = View.GONE
                        }
                    }
                    BannerType.ERROR -> {
                        layout.setBackgroundColor(getColorHelper(R.color.negative_100))
                        if (hasIcon) {
                            (layout.iv_type as ImageView).apply {
                                visibility = View.VISIBLE
                                setImageResource(R.drawable.ic_banner_error)
                            }
                        } else {
                            layout.iv_type.visibility = View.GONE
                        }
                    }
                    BannerType.INFORMATION -> {
                        layout.setBackgroundColor(getColorHelper(R.color.general_100))
                        if (hasIcon) {
                            (layout.iv_type as ImageView).apply {
                                visibility = View.VISIBLE
                                setImageResource(R.drawable.ic_checkout_general)
                            }
                        } else {
                            layout.iv_type.visibility = View.GONE
                        }
                    }
                }

                layout.setOnTouchListener(object : OnSwipeTouchListener(this@BaseActivity) {
                    override fun onSwipeLeft() {
                        layout.slideLeft()
                    }

                    override fun onSwipeRight() {
                        layout.slideRight()
                    }
                })

                if (popupWindow != null) {
                    popupWindow?.dismiss()
                    popupWindow?.contentView = layout
                    if (handler != null) {
                        runnable?.let {
                            handler?.removeCallbacks(it)
                        }
                    }
                } else {
                    popupWindow = PopupWindow(
                        layout,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        false
                    )
                    popupWindow?.animationStyle = R.style.PopupWindowAnimationStyle
                    popupWindow?.isOutsideTouchable = false
                    handler = Handler(Looper.getMainLooper())
                    runnable = Runnable {
                        popupWindow?.dismiss()
                    }
                }

                popupWindow?.update()
                popupWindow?.showAtLocation(layout, Gravity.CENTER or Gravity.TOP, 0, 0)

                runnable?.let {
                    handler?.postDelayed(it, duration)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun View.slideLeft(duration: Int = 500) {
        visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, -this.width.toFloat(), 0f, 0f)
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

    fun View.slideRight(duration: Int = 500) {
        visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, this.width.toFloat(), 0f, 0f)
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

}