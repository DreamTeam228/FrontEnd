package msc.fooxer.studplaces

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import java.util.*

class CustomAdapter internal constructor(context: Context, private val elements: List<DataModel>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private val inflater: LayoutInflater

    init {
        this.inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = inflater.inflate(R.layout.element, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val element = elements[i]
        viewHolder.imageView.setImageResource(element.image)
        viewHolder.textView.text = element.text

        /*viewHolder.imageView.setOnClickListener(View.OnClickListener {
            viewHolder.textView.text = "Pressed"

            /*val textView = viewHolder.textView.findViewById<TextView>(R.id.name)
            textView.text = element.text + "dffd"*/
        })*/
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal val imageView: ImageView
        internal val textView: TextView

        init {
            imageView = view.findViewById<View>(R.id.photo) as ImageView
            textView = view.findViewById<View>(R.id.description) as TextView
        }
    }
}
