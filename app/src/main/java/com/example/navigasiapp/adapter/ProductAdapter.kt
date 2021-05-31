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
import com.example.navigasiapp.Order
import com.example.navigasiapp.model.ProductModel
import com.example.navigasiapp.R
import kotlinx.android.synthetic.main.row_item_category.view.*
import kotlinx.android.synthetic.main.row_item_product.view.*
import java.net.URL

class ProductAdapter (val context: Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(), Filterable {
    var arrayList = ArrayList<ProductModel>()
    var ProductListFilter = ArrayList<ProductModel>()

    fun setData(arrayList: ArrayList<ProductModel>) {
        this.arrayList = arrayList
        this.ProductListFilter = arrayList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(model: ProductModel) {
            itemView.productName.text   = "${model.id}. ${model.name}"
            itemView.productDesc.text   = model.description
            itemView.priceProduct.text  = model.price.toString()
            //Menampilkan gambar yang didapatkan dari url
            val url: URL = URL("http://ins2.teknisitik.com/"+model.image_link)
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            itemView.imgProduct.setImageBitmap(bmp)
//            itemView.imgProduct.setImageResource(model.picProduct)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_item_product, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(arrayList[position])
        holder.itemView.setOnClickListener() {
            val model = arrayList.get(position)

//            var gId: Int = model.idProduct
//            var gProduct: String = model.nmProduct
//            var gDesc: String    = model.dsProduct
//            var gHarga: Int      = model.priceofProduct.toString().toInt()
//            var gImg: Int        = model.picProduct
//
//            val intent = Intent(context, Order::class.java)
//            intent.putExtra("pId", gId)
//            intent.putExtra("pProduct", gProduct)
//            intent.putExtra("pDesc", gDesc)
//            intent.putExtra("pHarga", gHarga)
//            intent.putExtra("pImg", gImg)
//            context.startActivity(intent)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint == null || constraint.length < 0) {
                    filterResults.count = ProductListFilter.size
                    filterResults.values = ProductListFilter
                } else {
                var searchChr = constraint.toString()
                val productSearch = ArrayList<ProductModel>()
                for (item in ProductListFilter) {
                    if (item.name.toLowerCase()
                            .contains(searchChr) || item.description.toLowerCase().contains(searchChr)) {
                        productSearch.add(item)
                    }
                }
                filterResults.count = productSearch.size
                filterResults.values = productSearch
            }
            return filterResults
            }

            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
                arrayList = filterResults!!.values as ArrayList<ProductModel>
                notifyDataSetChanged()
            }
        }
    }
}