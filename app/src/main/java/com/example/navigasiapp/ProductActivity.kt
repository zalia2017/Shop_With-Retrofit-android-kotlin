package com.example.navigasiapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigasiapp.adapter.CategoryAdapter
import com.example.navigasiapp.adapter.ProductAdapter
import com.example.navigasiapp.api.ApiClient
import com.example.navigasiapp.api.ApiInterface
import com.example.navigasiapp.model.CategoryModel
import com.example.navigasiapp.model.ProductModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBar: ActionBarDrawerToggle
    private lateinit var navDrawerView: NavigationView

    //Initialise the NavigationBottomBar
    private lateinit var bottomNavigation : BottomNavigationView
    lateinit var sharedPref: SharedPreferences
    var token: String = ""

    var myAdapter : ProductAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        val SDK_INT = Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
            sharedPref = getSharedPreferences("SharePref", Context.MODE_PRIVATE)
            token = sharedPref.getString("token", "")!!

            //get category id from intent
            var intent      = intent
            val categoryId  = intent.getIntExtra("categoryId", 0)

            Toast.makeText(this, "category Id "+categoryId, Toast.LENGTH_SHORT).show()
            var apiInterface: ApiInterface = ApiClient().getApiClient()!!.create(ApiInterface::class.java)
            var requestCall: Call<JsonObject> = apiInterface.getProductsByCategory("Bearer "+token, categoryId)
            requestCall.enqueue(object: Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    Log.d("gagal", t.toString())
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("product log", response.body().toString())
                    val myJson = response.body()
                    val myData = myJson!!.getAsJsonArray("data")

                    Log.d("My Log", myData.size().toString())
                    myAdapter = ProductAdapter(this@ProductActivity)

                    val arrayItem = ArrayList<ProductModel>()
                    for (i in 0 until myData.size()) {
                        var myRecord: JsonObject = myData.get(i).asJsonObject
                        var id = myRecord.get("id").asInt
                        var name = myRecord.get("name").asString
                        var price = myRecord.get("price").asInt
                        var image_link = myRecord.get("image_link").asString
                        var description = myRecord.get("description").asString


                        Log.d("Log "+i.toString(), myData.get(i).toString())
                        arrayItem.add(ProductModel(id, name, price, image_link, description))
//
                    }
                    Log.d("Array Item", arrayItem.toString())
//                val myData2 : ArrayList<CategoryModel> = myData as ArrayList<CategoryModel>
                    myAdapter!!.setData(arrayItem)

                    product_recycleview.layoutManager = LinearLayoutManager(this@ProductActivity)
                    product_recycleview.adapter = myAdapter
                }


            })
        }



        //START BOTTOM NAVIGATION
        //START BOTTOM NAVIGATION
        bottomNavigation = findViewById(R.id.navBottom)
        bottomNavigation.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.history -> {
                    Toast.makeText(this, "Go To history", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }
        drawerLayout = findViewById(R.id.drawer)
        actionBar = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar.syncState()

        navDrawerView = findViewById(R.id.navDrawer)
        navDrawerView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.myProfile -> {
                    val intent = Intent(applicationContext, Profile::class.java)
                    startActivity(intent)
                    true
                }
                R.id.myContact -> {
                    Toast.makeText(this, "Go to My Contact", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.myHelp -> {
                    Toast.makeText(this, "Go to Our Help", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
//        sharedPref = getSharedPreferences("SharePref", Context.MODE_PRIVATE)
//        token = sharedPref.getString("token", "")!!
//
//        if(token == ""){
//            Toast.makeText(this, "token "+token, Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, Login::class.java)
//            startActivity(intent)
//        }
    }
    override fun onSupportNavigateUp(): Boolean {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            this.drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu?.findItem(R.id.search)
        if(searchItem != null){
            val searchView = searchItem.actionView as SearchView
            searchView.maxWidth = Int.MAX_VALUE
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(filterString: String?): Boolean {
                    myAdapter!!.filter.filter(filterString)
                    return true
                }

            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.shopping) {
//            Toast.makeText(this, "View Shopping Chart", Toast.LENGTH_SHORT).show()
//            return true
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        } else if (id == R.id.account) {
            Toast.makeText(this, "Account Clicked", Toast.LENGTH_SHORT).show()
            return true
        } else if (id == R.id.logout) {
            sharedPref = getSharedPreferences("SharePref", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPref.edit()
//            editor.putBoolean("loginStatus", false)
            editor.clear()
            editor.apply()

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            Toast.makeText(this, "Logout and go to login form", Toast.LENGTH_SHORT).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}