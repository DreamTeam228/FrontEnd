package msc.fooxer.studplaces

import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings.System.getString
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView


open class CustomAdapter internal constructor(context: Context, private val elements: List<DataElement>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private val inflater: LayoutInflater

    init {
        this.inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = inflater.inflate(R.layout.element, viewGroup, false)
        return ViewHolder(view)
    }

  open override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        Log.d("VIEWHOLDER_POSITION", "POSITION IS $i")
        val element = elements[i]
        viewHolder.imageView.setImageResource(element.image)
        viewHolder.textView.text = element.text
        viewHolder.priceView.text = element.price.toString()

        viewHolder.imageView.setOnClickListener(View.OnClickListener {
            val info = Intent (it.context, Information::class.java)
            info.putExtra("POSITION", i)
                info.putExtra("FROM_FAV", false)
            /*info.putExtra(Information.NAME, elements[i].text)
            info.putExtra("IS_FAVORITE", elements[i].isFavorite)*/
            startActivity(it.context, info, null)
        })
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal val imageView: ImageView
        internal val textView: TextView
        internal val priceView: TextView

        init {
            imageView = view.findViewById<View>(R.id.photo) as ImageView
            textView = view.findViewById<View>(R.id.description) as TextView
            priceView = view.findViewById<View>(R.id.price) as TextView
        }
    }
}

// реализация второго адаптера для верного отображения информации об объектах из избранного (костыль)

class FavAdapter (context: Context, private val elements: List<DataElement>) : CustomAdapter (context = context, elements = elements) {

        override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        Log.d("VIEWHOLDER_POSITION", "POSITION IS $i")
        val element = elements[i]
        viewHolder.imageView.setImageResource(element.image)
        viewHolder.textView.text = element.text
        viewHolder.priceView.text = element.price.toString()

            viewHolder.imageView.setOnClickListener(View.OnClickListener {
            val info = Intent (it.context, Information::class.java)
            info.putExtra("POSITION", i)
            info.putExtra("FROM_FAV", true)
            /*info.putExtra(Information.NAME, elements[i].text)
            info.putExtra("IS_FAVORITE", elements[i].isFavorite)*/
            startActivity(it.context, info, null)
        })
    }
}