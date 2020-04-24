package com.example.thechallen_ge

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment

class Dialog : AppCompatDialogFragment() {
// Test test
    //Test test
     private lateinit var listener: DialogListener
     private lateinit var builder: AlertDialog.Builder

    interface DialogListener {
        fun onOkayClicked()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): android.app.Dialog {
        builder = AlertDialog.Builder(activity!!)
        builder.setPositiveButton("Ok") { dialog, which -> listener.onOkayClicked() }

        if (arguments != null) {
            val title = arguments!!.getString("title")
            val message = arguments!!.getString("message")

            builder.setTitle(title)
            builder.setMessage(message)
        }

        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = context as DialogListener
        } catch (e: ClassCastException) {

        }

    }
}