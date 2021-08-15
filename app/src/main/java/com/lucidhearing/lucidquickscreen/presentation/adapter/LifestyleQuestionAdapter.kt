package com.lucidhearing.lucidquickscreen.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.lucidhearing.lucidquickscreen.data.model.dataModel.QuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion
import com.lucidhearing.lucidquickscreen.databinding.LifestyleQuestionListItemBinding
import com.lucidhearing.lucidquickscreen.presentation.util.toBoolean
import java.util.HashMap

class LifestyleQuestionAdapter: RecyclerView.Adapter<LifestyleQuestionAdapter.LifestyleQuestionViewHolder>() {

    private var onResponseClickListener:((String, Boolean) -> Unit)? = null
    var lifestyleQuestions: List<LifestyleQuestion> = listOf()
    var lifestyleQuestionResponse = HashMap<String, QuestionResponse>()

    override fun getItemCount(): Int {
        return lifestyleQuestions.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifestyleQuestionViewHolder {
        val binding = LifestyleQuestionListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return LifestyleQuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LifestyleQuestionViewHolder, position: Int) {
        val lifestyleQuestion = lifestyleQuestions[position]
        holder.bind(lifestyleQuestion,(position+1))
    }

    inner class LifestyleQuestionViewHolder(
       val binding:LifestyleQuestionListItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(lifestyleQuestion: LifestyleQuestion, position:Int){
            binding.apply{
                textviewQue.text = lifestyleQuestion.question
                imageNumber.text = position.toString()
                val queResponse:QuestionResponse? =  lifestyleQuestionResponse[lifestyleQuestion.questionCode]
                if(queResponse != null){
                    radioButtonTrue.isChecked = queResponse.response
                    radioButtonFalse.isChecked = !queResponse.response
                }
                radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radioButton = group.findViewById<RadioButton>(checkedId)
                    onResponseClickListener?.let {
                        it(lifestyleQuestion.questionCode!!,radioButton.text.toString().toBoolean())
                    }
                })
            }
        }
    }

    fun setOnItemClickListener(listener :((String,Boolean)-> Unit)){
        onResponseClickListener = listener
    }
}