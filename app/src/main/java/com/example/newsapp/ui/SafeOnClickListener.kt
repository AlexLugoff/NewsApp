package com.example.newsapp.ui

import android.os.SystemClock
import android.view.View

class SafeOnClickListener(
    private val clickIntervalInMillis: Long = 250L,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {

    private var lastTimeClicked: Long = 0

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < clickIntervalInMillis) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}