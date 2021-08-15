package com.lucidhearing.lucidquickscreen.presentation.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.databinding.SuggestionListItemBinding
import com.lucidhearing.lucidquickscreen.domain.usecase.GetRetailerLifestyleQuestionsUseCase
import com.lucidhearing.lucidquickscreen.utils.LifestyleQuestionUtil
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*


class StoreNumberListAdapter(
    private val context:Activity
):ArrayAdapter<String>(context,R.layout.suggestion_list_item) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface UseCaseInterface {
        fun getRetailerLifestyleQuestionsUseCase(): GetRetailerLifestyleQuestionsUseCase
    }
    var storeNumbersFiltered:List<String> = listOf()
    var getRetailerLifestyleQuestionsUseCase:GetRetailerLifestyleQuestionsUseCase

    init {
        val useCaseInterface =
            EntryPointAccessors.fromApplication(context.applicationContext,UseCaseInterface::class.java)
        getRetailerLifestyleQuestionsUseCase = useCaseInterface.getRetailerLifestyleQuestionsUseCase()
    }

    override fun getCount(): Int {
        return storeNumbersFiltered.size
    }

    override fun getItem(position: Int): String {
        return storeNumbersFiltered[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SuggestionListItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        binding.textViewSuggestion.text = storeNumbersFiltered[position]
        return binding.root
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.toLowerCase()
                val filterResults = FilterResults()
                runBlocking {
                    try {
                        val response = getRetailerLifestyleQuestionsUseCase.execute()
                        val questions = response?.data?.getRetailerQuestions?.filterNotNull()
                        if(questions != null && !response.hasErrors()){
                            val retailerQuestions = LifestyleQuestionUtil.parseRetailerQuestions(questions)
                            if(retailerQuestions.size > 0){
                                val suggestions = mutableListOf<String>()
                                retailerQuestions.forEach {
                                    suggestions.add(it.questionCode ?: "")
                                }
                                filterResults.values = suggestions
                            }else{filterResults.values = mutableListOf<String>()}
                        }else{filterResults.values = mutableListOf<String>()}
                    }catch (e:Exception){
                        Log.i(AppConstant.EXCEPTION_TAG,e.message.toString())
                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                (results?.values as List<String>).also { storeNumbersFiltered = it }
                notifyDataSetChanged()
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return super.convertResultToString(resultValue)
            }
        }
    }
}