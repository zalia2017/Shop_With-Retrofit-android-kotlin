package com.example.navigasiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.navigasiapp.api.ApiClient
import com.example.navigasiapp.api.ApiInterface
import com.example.navigasiapp.model.DefaultResponse
import com.example.navigasiapp.model.LoginResponse
import com.example.navigasiapp.model.UserModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_signUp.setOnClickListener {
            register()
        }
        tvSignIn.setOnClickListener {
            goToLoginPage()
        }
        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setTitle("Sign Up Form")
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)
    }

    fun register(){
        var name = etName.text.toString()
        var email = etEmail.text.toString()
        var password = etPassword.text.toString()
        var password2 = etPassword2.text.toString()
        if(password != password2){
            return Toast.makeText(this,
                "Password tidak sesuai",
                Toast.LENGTH_SHORT).show()
        }
        if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
            return Toast.makeText(this,
                "Masih ada field yang kosong",
                Toast.LENGTH_SHORT).show()
        }

        var apiInterface: ApiInterface = ApiClient()
            .getApiClient()!!
            .create(ApiInterface::class.java)
        var requestCall: Call<DefaultResponse> = apiInterface
            .register(name, email, password)
        requestCall.enqueue(object : Callback<DefaultResponse>{
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(baseContext, "Proses Register gagal ya ",
                    Toast.LENGTH_SHORT).show()
                Log.d("log register", t.toString())
            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if(response.isSuccessful){
                    Toast.makeText(this@SignUp,
                        "Proses Register berhasil",
                        Toast.LENGTH_SHORT).show()
                    Log.d("log", response.body()?.success.toString())
                    val intent = Intent(this@SignUp,
                        MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this@SignUp,
                        "Proses Register Gagal",
                        Toast.LENGTH_SHORT).show()
                }
            }

        })
//        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
//        val checkUser = databaseHandler.checkUser(name)
//        if(checkUser){
//            Toast.makeText(this,"Data user sudah ada ", Toast.LENGTH_SHORT).show()
//        }else{
//            val status =
//                databaseHandler.addUser(UserModel(0, name, password))
//            if(status > -1) {
//                Toast.makeText(this, "Data user tersimpan", Toast.LENGTH_LONG).show()
//                etUsername.text.clear()
//                etPassword.text.clear()
//                etPassword2.text.clear()
//            }else{
//                Toast.makeText(this, "Data user gagal tersimpan", Toast.LENGTH_SHORT).show()
//            }
//        }

    }
    fun goToLoginPage(){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}