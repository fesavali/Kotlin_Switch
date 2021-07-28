package com.sriyank.javatokotlindemo.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sriyank.javatokotlindemo.R
import com.sriyank.javatokotlindemo.extensions.toast
import com.sriyank.javatokotlindemo.models.Repository

import kotlinx.android.synthetic.main.list_item.view.*


class DisplayAdapter(private val context: Context, private var repositoryList: List<Repository>) : RecyclerView.Adapter<DisplayAdapter.MyViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
		val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
		return MyViewHolder(view)
	}

	override fun onBindViewHolder(holder: MyViewHolder, positions: Int) {
		val current = repositoryList[positions]
		holder.setData(current, positions)
	}

	override fun getItemCount(): Int = repositoryList.size

	fun swap(dataList: List<Repository>) {
		if (dataList.isEmpty())
			context.toast("No Items Found")
		repositoryList = dataList

	}

	inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		private var pos: Int = 0
		private var current: Repository? = null

		init {

			itemView.imgBookmark.setOnClickListener {
				bookmarkRepository(current)
			}

			itemView.setOnClickListener {
				current?.let {
					val url = current!!.htmlUrl
					val webpage = Uri.parse(url)
					val intent = Intent(Intent.ACTION_VIEW, webpage)
					if (intent.resolveActivity(context.packageManager) != null) {
						context.startActivity(intent)
					}
				}
			}
		}

		fun setData(current: Repository?, position: Int) {

			current?.let {
				itemView.txvName.text = current.name
				itemView.txvLanguage.text = current.language
				itemView.txvForks.text = current.forks.toString()
				itemView.txvWatchers.text = current.watchers.toString()
				itemView.txvStars.text = current.stars.toString()
			}
			this.pos = position
			this.current = current
		}


	companion object {

		private val TAG = DisplayAdapter::class.java.simpleName
	}
}
