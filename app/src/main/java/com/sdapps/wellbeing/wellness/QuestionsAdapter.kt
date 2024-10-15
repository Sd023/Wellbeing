package com.sdapps.wellbeing.wellness

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.wellbeing.R

class QuestionsAdapter(var listItems: ArrayList<TaskBO>) : RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {

    private var answeredQuestions: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.questions_list, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(holder,holder.adapterPosition)
    }

    private fun bind(holder: ViewHolder, position: Int){
        holder.taskId.text = "${listItems[position].taskId}"
        holder.questions.text = listItems[position].question

        holder.radioYes.setOnCheckedChangeListener { _ , isChecked ->
            if(isChecked){
                holder.radioNo.isEnabled = false
                answeredQuestions++
                Log.d("QSN","$answeredQuestions")
            }
        }

        holder.radioNo.setOnCheckedChangeListener { _ , isChecked ->
            if(isChecked) {
                holder.radioYes.isEnabled = false
            }

        }

    }

    fun getTotalCount(): Int = answeredQuestions


    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var taskId : TextView = view.findViewById(R.id.taskId)
        var questions : TextView = view.findViewById(R.id.questions)
        var radioYes : RadioButton = view.findViewById(R.id.answerYes)
        var radioNo : RadioButton = view.findViewById(R.id.answerNo)
    }


}