package com.example.navigasiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_cart.*

class Cart : AppCompatActivity() {
    var myAdapter : cartAdapter? = null
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        btn_checkout.setOnClickListener {
            goToDataPembeli()
        }

        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setTitle("Keranjang Belanja")

        sharedPref = getSharedPreferences("SharePref", Context.MODE_PRIVATE)

        var idUser = sharedPref.getInt("idUser", 0)

        val databaseHandler: DatabaseHandler_bk = DatabaseHandler_bk(this)
        val cardList: ArrayList<CartModel> = databaseHandler.viewCartsByUser(idUser)
        myAdapter = cartAdapter(this)
        myAdapter!!.setData(cardList)
        var totalItem: Int = 0
        var totalHargaPerItem: Int = 0
        var grandTotal: Int = 0
        cardList.forEach {
            totalHargaPerItem = (it.totalProduct)*(it.priceofProduct)
            totalItem += it.totalProduct
            grandTotal += totalHargaPerItem
        }
        tvTotalProduct.text = totalItem.toString()
        tvTotalHarga.text = grandTotal.toString()

        rv_cart.layoutManager = LinearLayoutManager(this)
        rv_cart.adapter = myAdapter
    }

    fun goToDataPembeli(){
        val intent = Intent(applicationContext, DataPembeli ::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}