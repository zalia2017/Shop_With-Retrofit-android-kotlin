package com.example.navigasiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_order.*

class Order : AppCompatActivity() {


    var totHarga    :Int    = 0
    var minInteger  :Int    = 0
    var MIN_NUMBER          = 0
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)


        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)

        var intent      = intent
        val aId         = intent.getIntExtra("pId", 0)
        val aProduct    = intent.getStringExtra("pProduct")
        val aHarga      = intent.getIntExtra("pHarga", 0)
        val aImg        = intent.getIntExtra("pImg", 0)

        actionBar.setTitle("Order " +aProduct)
        //id pada activity_order.xml
        OrdId.text          = aId.toString()
        OrdProduct.text     = aProduct
        OrdPrice.text       = aHarga.toString()
        imageOrd.setImageResource(aImg)

        fun display(number: Int) {
            val displayInteger = findViewById<View>(R.id.JmlOrd) as TextView

            displayInteger.text = "" + number

            totHarga = OrdPrice.text.toString().toInt() *
                    displayInteger.text.toString().toInt()
            TotHarOrd.text = totHarga.toString()
        }

        decreaseOrd.setOnClickListener(){
            if(minInteger == MIN_NUMBER){
                minInteger = 0
            }
            else{
                minInteger = minInteger - 1
                display(minInteger)
            }
        }

        increaseOrd.setOnClickListener(){
            minInteger = minInteger + 1
            display(minInteger)
        }


        btn_cancel.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity ::class.java)
            startActivity(intent)
        }

        btn_tambahKeranjang.setOnClickListener {
            // GO TO INTENT BAYAR
//            val i = Intent(this, DataPembeli ::class.java)
//            i.putExtra("name", aProduct)
//            i.putExtra("price", aHarga.toString().toInt())
//            i.putExtra("qty", JmlOrd.text.toString().toInt())
//            i.putExtra("tot", totHarga.toString().toInt())
//
//            startActivity(i)
//            true
            addProductToCart()
        }
    }

    fun addProductToCart(){
        sharedPref = getSharedPreferences("SharePref", Context.MODE_PRIVATE)

        var idUser = sharedPref.getInt("idUser", 0)
        var idProduct = OrdId.text.toString().toInt()
        var nmProduct = OrdProduct.text.toString()
        var harga = OrdPrice.text.toString().toInt()
        var totalBeli = JmlOrd.text.toString().toInt()
        if(totalBeli == 0){
            return Toast.makeText(this, "Jumlah beli masih kosong", Toast.LENGTH_SHORT).show()
        }else{
            val databaseHandler: DatabaseHandler_bk = DatabaseHandler_bk(this)
            val status = databaseHandler.addProductToCart(CartModel(0, idUser, idProduct, nmProduct, harga, totalBeli))
            if(status > -1) {
                Toast.makeText(this, "Data Product tersimpan di keranjang", Toast.LENGTH_LONG).show()
//                etUsername.text.clear()
//                etPassword.text.clear()
//                etPassword2.text.clear()
            }else{
                Toast.makeText(this, "Data Product gagal tersimpan di keranjang", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}