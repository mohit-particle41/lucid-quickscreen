package com.lucidhearing.lucidquickscreen.presentation.util

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lucidhearing.lucidquickscreen.R

class AlertDialogFragment: DialogFragment() {
    var message: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) updateFromSavedInstanceState(savedInstanceState)
        return AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(resources.getString(R.string.button_dismiss))
            { _, _ -> }
            .create()
    }

    companion object {
        const val TAG = "ConfirmDialogFragment"
        private const val DIALOG_MESSAGE_OUTSTATE = "dialog_message"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DIALOG_MESSAGE_OUTSTATE,message)
    }

    private fun updateFromSavedInstanceState(savedInstanceState: Bundle?){
        if (savedInstanceState != null){
            message = savedInstanceState.get(DIALOG_MESSAGE_OUTSTATE) as String
        }
    }

}