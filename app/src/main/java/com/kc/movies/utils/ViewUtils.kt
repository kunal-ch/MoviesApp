package com.kc.movies.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog

fun clearLightStatusBar(view: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = view.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        view.systemUiVisibility = flags
    }
}

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

fun showAlertDialogForDetail(activity: Activity) {
    val builder = AlertDialog.Builder(activity)
    builder.setTitle("MoviesApp")
    builder.setMessage("No internet connection available!!")
    builder.setIcon(android.R.drawable.ic_dialog_alert)
    builder.setPositiveButton("Try Again"){dialogInterface, which ->
        showAlertDialogForDetail(activity)
    }
    builder.setNegativeButton("OK"){dialogInterface, which ->
    }
    val alertDialog: AlertDialog = builder.create()
    alertDialog.setCancelable(true)
    alertDialog.show()
}

fun showAlertDialogForList(activity: Activity) {
    val builder = AlertDialog.Builder(activity)
    builder.setTitle("MoviesApp")
    builder.setMessage("No internet connection available!!")
    builder.setIcon(android.R.drawable.ic_dialog_alert)
    builder.setPositiveButton("OK"){dialogInterface, which ->
    }
    val alertDialog: AlertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialog.show()
}

fun showErrorDialog(activity: Activity, message: String) {
    val builder = AlertDialog.Builder(activity)
    builder.setTitle("MoviesApp")
    builder.setMessage(message)
    builder.setIcon(android.R.drawable.ic_dialog_alert)
    builder.setPositiveButton("OK"){dialogInterface, which ->
    }
    val alertDialog: AlertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialog.show()
}