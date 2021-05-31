package com.example.navigasiapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.navigasiapp.ProductActivity
import com.example.navigasiapp.R
import com.example.navigasiapp.model.CategoryModel
import kotlinx.android.synthetic.main.activity_product.view.*
import kotlinx.android.synthetic.main.row_item_category.view.*
import java.net.URL


class CategoryAdapter (val context: Context) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>(), Filterable {
    var arrayList = ArrayList<CategoryModel>()
    var CategoryListFilter = ArrayList<CategoryModel>()

    fun setData(arrayList: ArrayList<CategoryModel>) {
        this.arrayList = arrayList
        this.CategoryListFilter = arrayList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(model: CategoryModel) {
            itemView.categoryName.text   = "${model.id}. ${model.name}"
            //Menampilkan gambar yang didapatkan dari url
            val url: URL = URL("http://ins2.teknisitik.com/" + model.image_link)
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            itemView.imgCategory.setImageBitmap(bmp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_category, parent, false)
        return CategoryAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(arrayList[position])
        holder.itemView.setOnClickListener() {
            val model = arrayList.get(position)

            var categoryId: Int = model.id


            val intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("categoryId", categoryId)
            context.startActivity(intent)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?):
                    FilterResults {
                val filterResults = FilterResults()
                if (constraint == null || constraint.length < 0) {
                    filterResults.count = CategoryListFilter.size
                    filterResults.values = CategoryListFilter
                } else {
                var searchChr = constraint.toString()
                val categorySearch = ArrayList<CategoryModel>()
                for (item in CategoryListFilter) {
                    if (item.name.toLowerCase()
                            .contains(searchChr)) {
                        categorySearch.add(item)
                    }
                }
                filterResults.count = categorySearch.size
                filterResults.values = categorySearch
            }
            return filterResults
            }

            override fun publishResults(constraint: CharSequence?,
                                        filterResults: FilterResults?) {
                arrayList = filterResults!!.values as ArrayList<CategoryModel>
                notifyDataSetChanged()
            }
        }
    }
}