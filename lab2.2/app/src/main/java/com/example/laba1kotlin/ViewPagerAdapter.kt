package com.example.laba1kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.laba1kotlin.R
import com.squareup.picasso.Picasso

class ViewPagerAdapter : PagerAdapter {

    var flag = true
    var context: Context
    var myItems: ArrayList<MyListAdapter.Civilization>
    var currentPosition: Int = 0
    lateinit var layoutInflater: LayoutInflater

    constructor(
        context: Context,
        items: ArrayList<MyListAdapter.Civilization>,
        current: Int
    ) : super() {
        this.context = context
        this.myItems = items
        currentPosition = current

    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean =
        view == `object` as LinearLayout

    override fun getCount(): Int {
        return myItems.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var firstPosition = currentPosition + position
        var image: ImageView
        var nameText: TextView
        var helpText: TextView
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view: View = layoutInflater.inflate(R.layout.slider_layout, container, false)
        image = view.findViewById(R.id.graphicS)
        nameText = view.findViewById(R.id.nameS)
        helpText = view.findViewById(R.id.helptextS)
        var url =
            "https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/images/tech/" + myItems.get(
                firstPosition
            ).graphic
        Picasso.with(context).load(url).into(image)
        nameText.setText(myItems.get(firstPosition).name)
        helpText.setText(myItems.get(firstPosition).helptext)
        container!!.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container!!.removeView(`object` as LinearLayout)
    }
}