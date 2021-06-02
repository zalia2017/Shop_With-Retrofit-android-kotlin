package com.example.navigasiapp

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigasiapp.adapter.CartAdapter
import com.example.navigasiapp.adapter.StrukCartAdapter
import com.example.navigasiapp.api.ApiClient
import com.example.navigasiapp.api.ApiInterface
import com.example.navigasiapp.model.CartModel
import com.google.gson.JsonObject
//import kotlinx.android.synthetic.main.activity_data_pembeli.txtHarga
//import kotlinx.android.synthetic.main.activity_data_pembeli.txtNamaProduk
//import kotlinx.android.synthetic.main.activity_data_pembeli.txtQty
//import kotlinx.android.synthetic.main.activity_data_pembeli.txtTotHarga
import kotlinx.android.synthetic.main.activity_struk.*
import kotlinx.android.synthetic.main.activity_struk.rv_cart
import kotlinx.android.synthetic.main.activity_struk.tvTotalHarga
import kotlinx.android.synthetic.main.activity_struk.tvTotalProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Struk : AppCompatActivity() {

    var myAdapter : StrukCartAdapter? = null
    lateinit var sharedPref: SharedPreferences
    var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_struk)

        var progressDialog: ProgressDialog = ProgressDialog(this@Struk)
        progressDialog.setMessage("Loading")
        progressDialog.show();

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

            requestCall.enqueue(object: Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    Log.d("gagal", t.toString())
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val myJson = response.body()
                    val myData = myJson!!.getAsJsonArray("data")

                    Log.d("My Log", myData.size().toString())
                    myAdapter = StrukCartAdapter(this@Struk)

                    val cartList = ArrayList<CartModel>()
                    for (i in 0 until myData.size()) {
                        var myRecord: JsonObject = myData.get(i).asJsonObject

                        var product = myRecord!!.getAsJsonObject("product")
                        var nmProduct = product!!.get("name").asString

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
                    rv_cart.layoutManager = LinearLayoutManager(this@Struk)
                    rv_cart.adapter = myAdapter

                    progressDialog.dismiss();

                }


            })
        }
//        val databaseHandler: DatabaseHandler_bk = DatabaseHandler_bk(this)
////        val cardList: ArrayList<CartModel> = databaseHandler.viewCartsByUser(idUser)
//        myAdapter = strukCartAdapter(this)
//        myAdapter!!.setData(cardList)
//        var totalItem: Int = 0
//        var totalHargaPerItem: Int = 0
//        var grandTotal: Int = 0
//        cardList.forEach {
//            totalHargaPerItem = (it.totalProduct)*(it.priceofProduct)
//            totalItem += it.totalProduct
//            grandTotal += totalHargaPerItem
//        }
//        tvTotalProduct.text = totalItem.toString()
//        tvTotalHarga.text = grandTotal.toString()

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

