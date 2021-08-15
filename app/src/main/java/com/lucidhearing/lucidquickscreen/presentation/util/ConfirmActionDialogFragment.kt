package com.lucidhearing.lucidquickscreen.presentation.util

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lucidhearing.lucidquickscreen.databinding.FragmentConfirmActionDialogBinding
import com.lucidhearing.lucidquickscreen.presentation.util.listener.ConfirmActionDialogListener
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.shared.SharedConfirmDialogViewModel

class ConfirmActionDialogFragment() : DialogFragment() {
    private var _confirmActionDialogBinding: FragmentConfirmActionDialogBinding? = null
    private val confirmActionDialogBinding get() = _confirmActionDialogBinding!!
    private val sharedConfirmDialogViewModel: SharedConfirmDialogViewModel by viewModels(ownerProducer = { requireParentFragment() })
    var message: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        _confirmActionDialogBinding =
            FragmentConfirmActionDialogBinding.inflate(inflater, container, false)
        val view = confirmActionDialogBinding.root
        if (savedInstanceState != null) updateFromSavedInstanceState(savedInstanceState)

        confirmActionDialogBinding.apply {
            textviewMessage.text = message ?: ""
            buttonCancel.setOnClickListener {
                dismiss()
            }
            buttonConfirm.setOnClickListener {
                sharedConfirmDialogViewModel.setConfirmAction(true)
                dismiss()
            }
        }
        return view
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

    companion object {
        const val TAG = "ConfirmActionDialogFragment"
        private const val DIALOG_MESSAGE_OUTSTATE = "dialog_message"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _confirmActionDialogBinding = null
    }
}