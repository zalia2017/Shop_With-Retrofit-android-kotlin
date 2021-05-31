package com.example.navigasiapp

//import kotlinx.android.synthetic.main.activity_data_pembeli.txtHarga
//import kotlinx.android.synthetic.main.activity_data_pembeli.txtNamaProduk
//import kotlinx.android.synthetic.main.activity_data_pembeli.txtQty
//import kotlinx.android.synthetic.main.activity_data_pembeli.txtTotHarga
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_data_pembeli.*


class DataPembeli : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pembeli)

        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)

//        val intent      = intent
//        val aProduct    = intent.getStringExtra("name")
//        val aHarga      = intent.getIntExtra("price", 0)
//        val aQty        = intent.getIntExtra("qty", 0)
//        val aTot        = intent.getIntExtra("tot", 0)

        actionBar.setTitle("Form Data Pembeli")

//        txtNamaProduk.text      = aProduct
//        txtHarga.text           = aHarga.toString()
//        txtQty.text             = aQty.toString()
//        txtTotHarga.text        = aTot.toString()

        btnCheckout.setOnClickListener{
            val i = Intent(this, Struk:: class.java)

            i.putExtra("nm", txtnama1.text.toString())
            i.putExtra("telp", txtTelp1.text.toString())
            i.putExtra("alamat", txtAlamat1.text.toString())
//            i.putExtra("name", aProduct)
//            i.putExtra("price", aHarga.toString().toInt())
//            i.putExtra("qty", aQty.toString().toInt())
//            i.putExtra("tot", aTot.toString().toInt())


            startActivity(i)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}