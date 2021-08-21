package com.va.mathengline.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.va.mathengline.R
import com.va.mathengline.models.MathOperationItem

class OperationsAdapter(var items: ArrayList<MathOperationItem> = ArrayList()): RecyclerView.Adapter<OperationsAdapter.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_operation, parent, false))

	override fun getItemCount() = items.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		fun bind(item: MathOperationItem) = with(itemView) {
			val tvTitle = findViewById<TextView>(R.id.tv_title)
			val tvSubtitle = findViewById<TextView>(R.id.tv_subtitle)
			val resultTv = findViewById<TextView>(R.id.tv_result)
			tvTitle.text =item.getOperator().text
			tvSubtitle.text = item.getParameters().joinToString()
			if (item.result!=null){
				resultTv.isVisible = true
				resultTv.text = "Result : ${item.result}"
			}
		}
	}
}