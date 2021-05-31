package com.example.navigasiapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_product.view.*
import kotlinx.android.synthetic.main.cart_row.view.*

class cartAdapter (val context: Context) : RecyclerView.Adapter<cartAdapter.ViewHolder>(){
    var arrayList = ArrayList<CartModel>()

    fun setData(arrayList: ArrayList<CartModel>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(model: CartModel, position: Int) {
            var no: Int = position + 1
            itemView.tvNo.text = no.toString()
            itemView.tvProduct.text  = model.nmProduct.toString()
            itemView.tvPrice.text  = model.priceofProduct.toString()
            itemView.tvTotal.text = model.totalProduct.toString()
            val gTotal = (model.priceofProduct)*(model.totalProduct)
            itemView.tvGTotal.text = gTotal.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cart_row, parent, false)
        return cartAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(arrayList[position], position)

    }

}