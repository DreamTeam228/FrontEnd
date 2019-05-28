package msc.fooxer.studplaces

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso


open class CustomAdapter internal constructor(context: Context, private var elements: List<Place>) :
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
        Log.d("VIEWHOLDER_POSITION", "POSITION IS $i")
        val element = elements[i]
        Picasso.get()
            .load(element.picture)
            .error(R.drawable.samurai)
            .placeholder(R.color.Metro_Line9)
            .into(viewHolder.imageView)
        viewHolder.textView.text = element.name
        if (element.price == 0) {
            if (element.discount == 0)
                viewHolder.priceView.text = "Бесплатно"
            else
                viewHolder.priceView.text = "Скидка ${element.discount}%"

        } else {
            val str = "${element.price} рублей"
            viewHolder.priceView.text = str
        }
        viewHolder.categoryView.text = element.category

    }

    override fun getItemCount(): Int {
        return elements.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal val imageView: ImageView
        internal val textView: TextView
        internal val priceView: TextView
        internal val categoryView: TextView

        init {
            imageView = view.findViewById<View>(R.id.photo) as ImageView
            textView = view.findViewById<View>(R.id.description) as TextView
            priceView = view.findViewById<View>(R.id.price) as TextView
            categoryView = view.findViewById<View>(R.id.category) as TextView
            view.setOnClickListener{
                val info = Intent (it.context, Information::class.java)
                info.putExtra("element", elements[adapterPosition])
                startActivity(it.context, info, null)
            }
        }

    }

    fun setData(elements: List<Place>) {
        this.elements = elements
        notifyDataSetChanged()
    }
}


class SearchAdapter internal constructor(context: Context, private val elements: Array<SearchOption>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    init {
        this.inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = inflater.inflate(R.layout.checkbox_element, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d("VIEWHOLDER_POSITION", "POSITION IS $position")
        val element = elements[position]
        viewHolder.imageView.setImageResource(element.image)
        viewHolder.checkBox.text = element.text

        if (viewHolder.checkBox.isChecked) {
            Log.d("CHECKSTATE_IS_TRUE", "POSITION IS $position")
        }

    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal val imageView: ImageView
        internal val checkBox: CheckBox
        init {
            imageView = view.findViewById<View>(R.id.list_icon) as ImageView
            checkBox = view.findViewById<View>(R.id.checkBox_search) as CheckBox
        }
    }
}
