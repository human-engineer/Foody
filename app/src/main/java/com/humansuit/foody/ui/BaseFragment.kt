package com.humansuit.foody.ui

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {


    protected fun showErrorDialog() {
        val dialogBuilder = AlertDialog.Builder(context)
        with(dialogBuilder) {
            setTitle("Error!")
            setMessage("Error message")
            setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int -> }
            setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }


}