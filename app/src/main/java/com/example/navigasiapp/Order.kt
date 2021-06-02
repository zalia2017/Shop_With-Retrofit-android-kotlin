package com.example.navigasiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.navigasiapp.api.ApiClient
import com.example.navigasiapp.api.ApiInterface
import com.example.navigasiapp.model.DefaultResponse
import kotlinx.android.synthetic.main.activity_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

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
        val aPrice      = intent.getIntExtra("pPrice", 0)
        val aImg        = intent.getStringExtra("pImg")

        actionBar.setTitle("Order " +aProduct)
        //id pada activity_order.xml
        OrdId.text          = aId.toString()
        OrdProduct.text     = aProduct
        OrdPrice.text       = aPrice.toString()
        val url: URL = URL("http://ins2.teknisitik.com/"+aImg)
        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        imageOrd.setImageBitmap(bmp)
//        imageOrd.setImageResource(aImg)

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

        var token = sharedPref.getString("token","")!!
        var idProduct = OrdId.text.toString().toInt()
//        var nmProduct = OrdProduct.text.toString()
        var price = OrdPrice.text.toString().toInt()
        var qty = JmlOrd.text.toString().toInt()
        if(qty == 0){
            return Toast.makeText(this, "Jumlah beli masih kosong", Toast.LENGTH_SHORT).show()
        }else{
//            val databaseHandler: DatabaseHandler_bk = DatabaseHandler_bk(this)
//            val status = databaseHandler.addProductToCart(CartModel(0, idUser, idProduct, nmProduct, harga, totalBeli))
//            if(status > -1) {
//                Toast.makeText(this, "Data Product tersimpan di keranjang", Toast.LENGTH_LONG).show()
//                etUsername.text.clear()
//                etPassword.text.clear()
//                etPassword2.text.clear()
//            }else{
//                Toast.makeText(this, "Data Product gagal tersimpan di keranjang", Toast.LENGTH_SHORT).show()
//            }
            var apiInterface: ApiInterface = ApiClient()
                .getApiClient()!!
                .create(ApiInterface::class.java)
            var requestCall: Call<DefaultResponse> = apiInterface
                .addProductToCart("Bearer "+token, idProduct, price, qty)

            requestCall.enqueue(object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(baseContext, "Data gagal ditambahkan ke keranjang ",
                        Toast.LENGTH_SHORT).show()
                    Log.d("log register", t.toString())
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    Log.d("logggg", response.toString())
                    if(response.isSuccessful){
                        Toast.makeText(this@Order,
                            "Data berhasil ditambahkan ke keranjang",
                            Toast.LENGTH_SHORT).show()
                        Log.d("log", response.body()?.success.toString())
                        Log.d("Log ordersss", response.body()?.message.toString())
                        val intent = Intent(this@Order,
                            MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@Order,
                            "Data gagal ditambahkan ke keranjang ya ",
                            Toast.LENGTH_SHORT).show()
                        Log.d("log order", response.body().toString()+token)
                    }
                }

            })
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}