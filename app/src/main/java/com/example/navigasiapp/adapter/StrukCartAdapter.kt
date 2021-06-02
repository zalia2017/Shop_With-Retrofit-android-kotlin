package com.example.navigasiapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.navigasiapp.R
import com.example.navigasiapp.model.CartModel
import kotlinx.android.synthetic.main.cart_row.view.*

class StrukCartAdapter (val context: Context) : RecyclerView.Adapter<StrukCartAdapter.ViewHolder>(){
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
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cart_struk_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(arrayList[position], position)

    }


}