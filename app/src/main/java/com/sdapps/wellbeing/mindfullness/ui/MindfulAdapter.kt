package com.sdapps.wellbeing.mindfullness.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.wellbeing.R
import com.sdapps.wellbeing.mindfullness.data.MenuBO
import com.sdapps.wellbeing.mindfullness.manager.CustomView

class MindfulAdapter(private var listItems: ArrayList<MenuBO>, var view: CustomView):
    RecyclerView.Adapter<MindfulAdapter.ViewHolder>() {

        private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.mindful_items, parent, false)
        context = parent.context
        return ViewHolder(layout)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(holder,holder.adapterPosition)
    }

    private fun bind(holder: ViewHolder,position: Int){
        var textValue = ""
        val image = BitmapDrawable(context.resources,listItems[position].image)
        holder.icon1.setImageDrawable(image)

        holder.cardItemText.text = listItems[position].mainMenu

        textValue = if(listItems[position].isTime){
            "${listItems[position].attr} seconds"
        } else{
            "${listItems[position].attr} tasks"
        }

        holder.timeText.text = textValue


        holder.cardItem.setOnClickListener {
            view.handleCardClick(listItems[position].mainMenu, listItems[position])
        }
    }
    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view)  {
        var cardItemText: TextView = view.findViewById(R.id.cardItemText)
        var timeText: TextView = view.findViewById(R.id.timerText)
        var cardItem: CardView = view.findViewById(R.id.cardItem)
        var icon1 : ImageView = view.findViewById(R.id.icon1)
    }
}