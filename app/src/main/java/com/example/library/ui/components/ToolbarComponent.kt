package com.example.library.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.library.R

//Custom component
//https://medium.com/@elye.project/building-custom-component-with-kotlin-fc082678b080

class ToolbarComponent @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null): ConstraintLayout(context,attrs){

    init {

        if(isInEditMode){
            LayoutInflater.from(context).inflate(R.layout.layout_toolbar_component, this, true)
        }
    }
}