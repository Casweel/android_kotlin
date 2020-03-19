package com.example.myapplication

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

val num: MutableList<String> = mutableListOf()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 1..1000000) {
            num.add(toString(i));
        }
        val adapter = MyListAdapter(this, num)
        list_view.adapter = adapter

    }

    class MyListAdapter(
        private var activity: MainActivity,
        private var items: MutableList<String>
    ) :
        BaseAdapter() {
        val colors: Array<Int> = arrayOf(Color.parseColor("#CCCCCC"), Color.parseColor("#FFFFFF"))
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View
            val textView: TextView?

            if (convertView == null) {
                val inflater =
                    activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(R.layout.list_item, null)
            } else {
                view = convertView
            }
            textView = view.findViewById(R.id.text_view)
            textView.setText(items[position])
            view.setBackgroundColor(colors[position % 2])
            return view
        }

        override fun getItem(i: Int): String {
            return items[i].toString()
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun getCount(): Int {
            return items.size
        }
    }
}

val digits: Array<String> =
    arrayOf(
        "",
        "one ",
        "two ",
        "three ",
        "four ",
        "five ",
        "six ",
        "seven ",
        "eight ",
        "nine ",
        "ten ",
        "eleven ",
        "twelve ",
        "thirteen ",
        "fourteen ",
        "fifteen ",
        "sixteen ",
        "seventeen ",
        "eighteen ",
        "nineteen "
    )
val dec: Array<String> =
    arrayOf(
        "",
        "",
        "twenty ",
        "thirty ",
        "forty ",
        "fifty ",
        "sixty ",
        "seventy ",
        "eighty ",
        "ninety "
    )

fun convert(i: Int, s: String): String {
    var number = ""
    if (i > 19) {
        number = dec[i / 10] + digits[i % 10]
    } else {
        number += digits[i]
    }
    if (i != 0) {
        number += s
    }
    return number
}

fun toString(i: Int): String {
    var out = ""
    out += convert((i / 1000000), "million ")
    out += convert((i / 100000 % 100), "lakh ") //100 000
    out += convert((i / 1000 % 100), "thousand ")
    out += convert((i / 100 % 10), "hundred ")
    if (i > 100 && i % 100 > 0) {
        out += "and "
    }
    out += convert((i % 100), " ")
    return out
}