package com.lucidhearing.lucidquickscreen.presentation

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.databinding.FragmentCustomerDetailsBinding
import com.lucidhearing.lucidquickscreen.presentation.util.AlertDialogFragment
import com.lucidhearing.lucidquickscreen.presentation.util.LoadingDialog
import com.lucidhearing.lucidquickscreen.presentation.util.listener.RequestStateListener
import com.lucidhearing.lucidquickscreen.presentation.util.parsePhoneNumber
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.customerViewmodel.CustomerViewModel
import com.lucidhearing.lucidquickscreen.utils.AppUtils


class CustomerDetailsFragment : Fragment() {
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var fragmentCustomerDetailsBinding: FragmentCustomerDetailsBinding
    private lateinit var loadingDialog:LoadingDialog

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
        return inflater.inflate(R.layout.fragment_customer_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCustomerDetailsBinding = FragmentCustomerDetailsBinding.bind(view)
        val homeActivity = (activity as HomeActivity)
        loadingDialog = LoadingDialog(homeActivity)
        customerViewModel = homeActivity.customerViewModel
        initTextFieldWithViewModel()
        addUIEventListeners()
    }

    private fun initTextFieldWithViewModel(){
        fragmentCustomerDetailsBinding.apply {
            textfieldFirstname.editText?.setText(customerViewModel.customer.firstname)
            textfieldLastname.editText?.setText(customerViewModel.customer.lastname)
            textfieldEmail.editText?.setText(customerViewModel.customer.email)
            textfieldZipcode.editText?.setText(customerViewModel.customer.zip)
            textfieldPhone.editText?.setText(customerViewModel.customer.phone)
        }
    }

    private fun observeViewModelLoadingState(){
        customerViewModel.loadingState.observe(viewLifecycleOwner,{ state ->
            if(state) loadingDialog.startLoadingDialog() else loadingDialog.dismissDialog()
        })
    }

    private fun addUIEventListeners(){
        addFirstnameTextFieldListener()
        addLastnameTextFieldListener()
        addEmailTextFieldListener()
        addZipTextFieldListener()
        addPhoneTextFieldListener()
        addSubmitButtonListener()
        addBackButtonListener()
    }

    private fun addFirstnameTextFieldListener(){
        fragmentCustomerDetailsBinding.apply {
            textfieldFirstname.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    textfieldFirstname.error = ""
                    textfieldFirstname.isErrorEnabled = false
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    customerViewModel.customer.firstname = s.toString()
                }
            })
        }
    }

    private fun addLastnameTextFieldListener(){
        fragmentCustomerDetailsBinding.apply{
            textfieldLastname.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    textfieldLastname.error = ""
                    textfieldLastname.isErrorEnabled = false
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    customerViewModel.customer.lastname = s.toString()
                }
            })
        }
    }

    private fun addEmailTextFieldListener(){
        fragmentCustomerDetailsBinding.apply {
            textfieldEmail.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    textfieldEmail.error = ""
                    textfieldEmail.isErrorEnabled = false
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    customerViewModel.customer.email = s.toString()
                }
            })
        }
    }

    private fun addZipTextFieldListener(){
        fragmentCustomerDetailsBinding.apply {
            textfieldZipcode.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    textfieldZipcode.error = ""
                    textfieldZipcode.isErrorEnabled = false
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    customerViewModel.customer.zip = s.toString()
                }
            })
        }
    }

    private fun addPhoneTextFieldListener(){
        fragmentCustomerDetailsBinding.apply {
            textfieldPhone.editText?.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    super.beforeTextChanged(s, start, count, after)
                    textfieldPhone.error = ""
                    textfieldPhone.isErrorEnabled = false
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    super.onTextChanged(s, start, before, count)
                }

                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                    val phone = s.toString()
                    customerViewModel.customer.phone = phone.parsePhoneNumber()
                }
            })
        }
    }

    private fun addSubmitButtonListener(){
        fragmentCustomerDetailsBinding.apply {
            buttonSubmit.setOnClickListener {
                val isError = checkFormValidations()
                if (!isError) {
                    findNavController().navigate(R.id.action_customerLogin_to_lifestyleQuestionFragment)
                }
            }
        }
    }

    private fun addBackButtonListener(){
        fragmentCustomerDetailsBinding.apply {
            buttonBack.setOnClickListener {
                backNavigationAction()
            }
        }
    }

    private fun checkFormValidations(): Boolean {
        var isError: Boolean = false
        val firstname: String = customerViewModel.customer.firstname!!.trim()
        val lastname: String = customerViewModel.customer.lastname!!.trim()
        val email: String = customerViewModel.customer.email!!.trim()
        val zip: String = customerViewModel.customer.zip!!.trim()
        val phone: String = customerViewModel.customer.phone!!.trim()

        fragmentCustomerDetailsBinding.apply {
            if (TextUtils.isEmpty(firstname) || !AppUtils.isNameValid(firstname)) {
                isError = true
                textfieldFirstname.error = resources.getString(R.string.error_invalid_firstname)
            }

            if (TextUtils.isEmpty(lastname) || !AppUtils.isNameValid(lastname)) {
                isError = true
                textfieldLastname.error = resources.getString(R.string.error_invalid_lastname)
            }

            if (TextUtils.isEmpty(email) || !AppUtils.isEmailValid(email)) {
                isError = true
                textfieldEmail.error = resources.getString(R.string.error_invalid_email)
            }

            if (TextUtils.isEmpty(zip) || !AppUtils.isZipValid(zip)) {
                isError = true
                textfieldZipcode.error = resources.getString(R.string.error_invalid_zip)
            }

            if (TextUtils.isEmpty(phone) || !AppUtils.isPhoneValid(phone)) {
                isError = true
                textfieldPhone.error = resources.getString(R.string.error_invalid_phone)
            }
        }
        return isError
    }

    private fun backNavigationAction(){
        customerViewModel.clearCustomerDetails()
        findNavController().popBackStack()
    }
}
