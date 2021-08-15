package com.lucidhearing.lucidquickscreen.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion
import com.lucidhearing.lucidquickscreen.databinding.FragmentLifestyleQuestionBinding
import com.lucidhearing.lucidquickscreen.presentation.adapter.LifestyleQuestionAdapter
import com.lucidhearing.lucidquickscreen.presentation.util.AlertDialogFragment
import com.lucidhearing.lucidquickscreen.presentation.util.ConfirmActionDialogFragment
import com.lucidhearing.lucidquickscreen.presentation.util.listener.RequestStateListener
import com.lucidhearing.lucidquickscreen.presentation.util.toBoolean
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.customerViewmodel.CustomerViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.lifestyleQuestionViewModel.LifestyleQuestionViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.shared.SharedConfirmDialogViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LifestyleQuestionFragment : Fragment(), RequestStateListener<List<LifestyleQuestion>> {
    private lateinit var fragmentLifestyleQuestionsBinding: FragmentLifestyleQuestionBinding
    lateinit var customerViewModel: CustomerViewModel
    lateinit var lifestyleQuestionViewModel: LifestyleQuestionViewModel
    private val sharedConfirmDialogViewModel: SharedConfirmDialogViewModel by viewModels(
        ownerProducer = { this })
    private lateinit var lifestyleQuestionAdapter: LifestyleQuestionAdapter
    private lateinit var confirmDialog: ConfirmActionDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backNavigationAction()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lifestyle_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLifestyleQuestionsBinding = FragmentLifestyleQuestionBinding.bind(view)
        customerViewModel = (activity as HomeActivity).customerViewModel
        lifestyleQuestionViewModel = (activity as HomeActivity).lifestyleQuestionViewModel
        lifestyleQuestionViewModel.requestStateListener = this
        lifestyleQuestionAdapter = (activity as HomeActivity).lifestyleQuestionAdapter
        confirmDialog = ConfirmActionDialogFragment()
        addLifestyleQuestionAdapterListner()
        initLifestyleQuestionList()
        nextButtonEventListener()
        addBackButtonListener()
        observeConfirmDialogAction()
        observeLoadingState()
    }

    private fun initLifestyleQuestionList() {
        fragmentLifestyleQuestionsBinding.rvLifestyleQuestion.apply {
            layoutManager = LinearLayoutManager(activity)
            lifestyleQuestionAdapter.lifestyleQuestionResponse =
                customerViewModel.lifestyleQuestionCustomerResponse
            if (lifestyleQuestionViewModel.lifestyleQuestions.size > 0) {
                lifestyleQuestionAdapter.lifestyleQuestions =
                    lifestyleQuestionViewModel.lifestyleQuestions
                adapter = lifestyleQuestionAdapter
            } else {
                lifestyleQuestionViewModel.getLifestyleQuestions()
            }
        }
        activateNextbutton()
    }

    private fun observeLoadingState() {
        lifestyleQuestionViewModel.loadingState.observeForever { state ->
            fragmentLifestyleQuestionsBinding.apply {
                if (state) {
                    shimmerFrameLayout.startShimmer()
                    shimmerFrameLayout.visibility = View.VISIBLE
                    buttonNext.visibility = View.GONE
                } else {
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                }
            }
        }
    }

    private fun nextButtonEventListener() {
        fragmentLifestyleQuestionsBinding.buttonNext.setOnClickListener {
            findNavController().navigate(R.id.action_lifestyleQuestionFragment_to_resultGraphFragment)
        }
    }

    private fun addBackButtonListener() {
        fragmentLifestyleQuestionsBinding.apply {
            buttonBack.setOnClickListener {
                backNavigationAction()
            }
        }
    }

    private fun observeConfirmDialogAction() {
        sharedConfirmDialogViewModel.confirmAction.observe(viewLifecycleOwner, { state ->
            if (state) {
                customerViewModel.clearCustomerDetails()
                lifestyleQuestionViewModel.clearLifestyleQuestionsData()
                findNavController().popBackStack(R.id.homeFragment, false)
                sharedConfirmDialogViewModel.setConfirmAction(false)
            }
        })
    }

    private fun backNavigationAction() {
        confirmDialog.message = resources.getString(R.string.dialog_confirm_exit_test)
        confirmDialog.show(childFragmentManager, ConfirmActionDialogFragment.TAG)
    }

    private fun addLifestyleQuestionAdapterListner() {
        lifestyleQuestionAdapter.setOnItemClickListener { questionCode, response ->
            customerViewModel.setQuestionResponse(questionCode, response)
            activateNextbutton()
        }
    }

    private fun activateNextbutton() {
        fragmentLifestyleQuestionsBinding.apply {
            val responseCount = customerViewModel.lifestyleQuestionCustomerResponse.size
            if (responseCount > 0 && responseCount == lifestyleQuestionViewModel.lifestyleQuestions.size) buttonNext.isEnabled =
                true
        }
    }

    override fun onSuccess(returnValue: List<LifestyleQuestion>) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                if (returnValue.size > 0) {
                    lifestyleQuestionAdapter.lifestyleQuestions = returnValue
                    fragmentLifestyleQuestionsBinding.rvLifestyleQuestion.adapter =
                        lifestyleQuestionAdapter
                    fragmentLifestyleQuestionsBinding.buttonNext.visibility = View.VISIBLE
                }
                else {
                    showAlert(getString(R.string.error_unable_to_process))
                }
            }
        }

    }

    override fun onError(message: String) {
        showAlert(message)
    }

    private fun showAlert(message: String) {
        val alertDialog = AlertDialogFragment()
        alertDialog.message = message
        alertDialog.show(childFragmentManager, AlertDialogFragment.TAG)
    }

}