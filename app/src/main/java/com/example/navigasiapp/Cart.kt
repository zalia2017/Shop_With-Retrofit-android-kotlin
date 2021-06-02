package com.example.navigasiapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigasiapp.adapter.CartAdapter
import com.example.navigasiapp.api.ApiClient
import com.example.navigasiapp.api.ApiInterface
import com.example.navigasiapp.model.CartModel
import com.example.navigasiapp.model.CategoryModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_item_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Cart : AppCompatActivity() {
    var myAdapter : CartAdapter? = null
    lateinit var sharedPref: SharedPreferences
    var token: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        val SDK_INT = Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
            //your codes here
            sharedPref = getSharedPreferences("SharePref", Context.MODE_PRIVATE)
            token = sharedPref.getString("token", "")!!

            var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)
            var requestCall: Call<JsonObject> = apiInterface.getCartsByUser("Bearer "+token)
            var progressDialog: ProgressDialog = ProgressDialog(this@Cart)
            progressDialog.setMessage("Loading")
            progressDialog.show();
            requestCall.enqueue(object: Callback<JsonObject> {

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d("gagal", t.toString())
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                    Log.d("category log", response.body().toString())
                    progressDialog.dismiss()
                    val myJson = response.body()
                    val myData = myJson!!.getAsJsonArray("data")

                    Log.d("My Log", myData.size().toString())
                    myAdapter = CartAdapter(this@Cart)

                    val cartList = ArrayList<CartModel>()
                    for (i in 0 until myData.size()) {
                        var myRecord: JsonObject = myData.get(i).asJsonObject
//                        var id = myRecord.get("id").asInt

                        var product = myRecord!!.getAsJsonObject("product")
                        var nmProduct = product!!.get("name").asString
//                        var nmProduct = myProduct.get("name").asString

                        var price = myRecord.get("price").asInt

                        var quantity = myRecord.get("quantity").asInt

//                        var name = myRecord.get("name").asString

                        Log.d("Log "+i.toString(), myData.get(i).toString())
                        cartList.add(CartModel(nmProduct, price, quantity))
                    }
                    Log.d("Array Item", cartList.toString())
                    myAdapter!!.setData(cartList)

                    var totalItem: Int = 0
                    var totalHargaPerItem: Int = 0
                    var grandTotal: Int = 0
                    cartList.forEach {
                        totalHargaPerItem = (it.totalProduct)*(it.priceofProduct)
                        totalItem += it.totalProduct
                        grandTotal += totalHargaPerItem
                    }
                    tvTotalProduct.text = totalItem.toString()
                    tvTotalHarga.text = grandTotal.toString()
                    rv_cart.layoutManager = LinearLayoutManager(this@Cart)
                    rv_cart.adapter = myAdapter

                }


            })
        }

        btn_checkout.setOnClickListener {
            goToDataPembeli()
        }

        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setTitle("Keranjang Belanja")


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