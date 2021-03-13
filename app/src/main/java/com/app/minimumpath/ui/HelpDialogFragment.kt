package com.app.minimumpath.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.app.minimumpath.R

class HelpDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(context!!, R.style.dialogStyle)
            builder.setMessage(getDescMsg())
                .setNegativeButton("OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun getDescMsg(): String {
        val defaultMsg = context?.getString(R.string.help_dialog_msg)
        return arguments?.getString(DESC_MSG, defaultMsg)!!
    }
    companion object{
        private const val DESC_MSG = "DESC_MSG"
        fun newInstance(msg:String) = HelpDialogFragment().apply {
            arguments = bundleOf(
                DESC_MSG to msg
            )
        }
    }
}