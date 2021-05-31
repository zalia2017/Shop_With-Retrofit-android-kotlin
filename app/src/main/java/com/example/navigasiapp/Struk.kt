package com.example.navigasiapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
//import kotlinx.android.synthetic.main.activity_data_pembeli.txtHarga
//import kotlinx.android.synthetic.main.activity_data_pembeli.txtNamaProduk
//import kotlinx.android.synthetic.main.activity_data_pembeli.txtQty
//import kotlinx.android.synthetic.main.activity_data_pembeli.txtTotHarga
import kotlinx.android.synthetic.main.activity_struk.*
import kotlinx.android.synthetic.main.activity_struk.rv_cart
import kotlinx.android.synthetic.main.activity_struk.tvTotalHarga
import kotlinx.android.synthetic.main.activity_struk.tvTotalProduct

class Struk : AppCompatActivity() {

    var myAdapter : strukCartAdapter? = null
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_struk)

        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        val intent      = intent

        val aNama        = intent.getStringExtra("nm")
        val aTelp       = intent.getStringExtra("telp")
        val aAlamat     = intent.getStringExtra("alamat")
        val aProduct    = intent.getStringExtra("name")
        val aHarga      = intent.getIntExtra("price", 0)
        val aQty        = intent.getIntExtra("qty", 0)
        val aTot        = intent.getIntExtra("tot", 0)


        actionBar.setTitle("Struk Pembelian")

        sharedPref = getSharedPreferences("SharePref", Context.MODE_PRIVATE)

        var idUser = sharedPref.getInt("idUser", 0)

        val databaseHandler: DatabaseHandler_bk = DatabaseHandler_bk(this)
        val cardList: ArrayList<CartModel> = databaseHandler.viewCartsByUser(idUser)
        myAdapter = strukCartAdapter(this)
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

        txtnama.text             = aNama
        txtTelp.text             = aTelp
        txtAlamat.text           = aAlamat

        }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

