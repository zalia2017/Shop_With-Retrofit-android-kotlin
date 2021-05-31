package com.example.navigasiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.navigasiapp.api.ApiClient
import com.example.navigasiapp.api.ApiInterface
import com.example.navigasiapp.model.LoginResponse
import com.example.navigasiapp.model.ProductModel
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tvSignUp.setOnClickListener {
            goToSignUpPage()
//            addProduct()
        }
        btn_login.setOnClickListener {
            loginMasuk()
        }
//        addUser()
    }

    fun loginMasuk() {
        val Uname   = username.text.toString()
        val Upass   = password.text.toString()

        if (Uname.isEmpty() || Upass.isEmpty()){
            Toast.makeText(this, "Isikan Username atau Password Dahulu !", Toast.LENGTH_SHORT).show()
        } else {
            var apiInterface: ApiInterface = ApiClient().getApiClient()!!
                .create(ApiInterface::class.java)
            var requestCall: Call<LoginResponse> = apiInterface
                .login(Uname, Upass)
            requestCall.enqueue(object : Callback<LoginResponse>{
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(baseContext, "Gagal Masuk ",
                        Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful){
//                        Log.d("log", response.body()?.token.toString())

                        val token: String = response.body()?.token.toString()
                        //Penyimpanan token ke sharepreferences
                        sharedPref = getSharedPreferences("SharePref",Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPref.edit()
                        editor.putString("token", token)
                        editor.apply()
                        //batas proses penyimpanan
                        Toast.makeText(this@Login, "Login Sukses!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@Login, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }

            })
//            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
//            val checkAuth: Boolean = databaseHandler.checkAuth(Uname, Upass)
//            if (checkAuth){
//                val getUser: Int = databaseHandler.getUser(Uname)
//                sharedPref = getSharedPreferences("SharePref",Context.MODE_PRIVATE)
//                val editor: SharedPreferences.Editor = sharedPref.edit()
//                editor.putBoolean("loginStatus", true)
//                editor.putInt("idUser", getUser)
//                editor.apply()
//                Toast.makeText(this, "Login Sukses!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
////            true
//            } else {
//                Toast.makeText(this, "Username atau Password Salah", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    fun goToSignUpPage(){
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }

}