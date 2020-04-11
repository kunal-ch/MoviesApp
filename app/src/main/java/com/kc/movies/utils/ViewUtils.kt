package com.kc.movies.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun showKeyboard(view: View) {
    val inputManager = view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputManager?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun hideKeyboard(activity: Activity) {
    val view = activity.findViewById<View>(android.R.id.content)
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}