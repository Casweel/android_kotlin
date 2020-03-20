package com.example.laba1kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_elements.view.*


class MyListAdapter(
    val items: ArrayList<Civilization>,
    val context: Context,
    var clickListener: MyClickListener
) : RecyclerView.Adapter<MyListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_elements,
                parent,
                false
            )
        )

    }

    // Прогрузка элементов в адаптере
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.name?.text = items.get(position).name
        var url =
            "https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/images/tech/" + items.get(
                position
            ).graphic
        Picasso.with(context).load(url).into(holder?.image)

        holder.initialize(items.get(position), clickListener)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.item_textView
        var helptext = view.item_helptext
        var image = view.item_icon

        fun initialize(item: Civilization, action: MyClickListener) {
            name.text = item.name
            helptext.text = item.helptext
            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }

    //class Civilization(val name: String, val graphic: String)
    class Civilization {
        var name:String = ""
        var helptext:String = ""
        var graphic:String = ""
        constructor (name: String, helptext: String, graphic: String){
            this.name = name
            this.helptext = helptext
            this.graphic = graphic
        }
        constructor (name: String, graphic: String){
            this.name = name
            this.graphic = graphic
        }
    }

    interface MyClickListener {
        fun onItemClick(item: Civilization, position: Int)
    }

}