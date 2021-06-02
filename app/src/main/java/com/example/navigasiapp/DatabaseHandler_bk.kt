package com.example.navigasiapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.navigasiapp.model.CartModel
import com.example.navigasiapp.model.ProductModel
import com.example.navigasiapp.model.UserModel


class DatabaseHandler_bk(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "DBPenjualan"

        private val TABLE_USERS = "tb_user"
        private val KEY_USER_ID = "id"
        private val KEY_USER_NAME = "name"
        private val KEY_USER_PASS = "password"

        private val TABLE_PRODUCTS = "tb_product"
        private val KEY_PRODUCT_ID = "idProduct"
        private val KEY_PRODUCT_NAME = "nmProduct"
        private val KEY_PRODUCT_DESC = "dsProduct"
        private val KEY_PRODUCT_PIC = "picProduct"
        private val KEY_PRODUCT_PRICE = "priceofProduct"

        private val TABLE_CARTS = "tb_cart"
        private val KEY_CART_ID = "id"
        private val KEY_CART_USER_ID = "idUser"
        private val KEY_CART_PRODUCT_ID = "idProduct"
        private val KEY_CART_PRODUCT_NAME = "nmProduct"
        private val KEY_CART_PRODUCT_PRICE = "priceofProduct"
        private val KEY_CART_PRODUCT_TOTAL = "totalProduct"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_USER = ("CREATE TABLE $TABLE_USERS('$KEY_USER_ID' INTEGER PRIMARY KEY, '$KEY_USER_NAME' TEXT, '$KEY_USER_PASS' TEXT)")
        val CREATE_TABLE_PRODUCT = ("CREATE TABLE $TABLE_PRODUCTS('$KEY_PRODUCT_ID' INTEGER PRIMARY KEY," +
                " '$KEY_PRODUCT_NAME' TEXT," +
                " '$KEY_PRODUCT_DESC' TEXT," +
                " '$KEY_PRODUCT_PIC' INTEGER," +
                " '$KEY_PRODUCT_PRICE' INTEGER)")
        val CREATE_TABLE_CART = ("CREATE TABLE $TABLE_CARTS('$KEY_CART_ID' INTEGER PRIMARY KEY," +
                " '$KEY_CART_USER_ID' INTEGER," +
                " '$KEY_CART_PRODUCT_ID' INTEGER," +
                " '$KEY_CART_PRODUCT_NAME' TEXT, " +
                " '$KEY_CART_PRODUCT_PRICE' INTEGER," +
                " '$KEY_CART_PRODUCT_TOTAL' INTEGER," +
                " FOREIGN KEY($KEY_CART_USER_ID) REFERENCES $TABLE_USERS($KEY_USER_ID)," +
                " FOREIGN KEY($KEY_CART_PRODUCT_ID) REFERENCES $TABLE_PRODUCTS($KEY_PRODUCT_ID))")

        db?.execSQL(CREATE_TABLE_USER)
        db?.execSQL(CREATE_TABLE_PRODUCT)
        db?.execSQL(CREATE_TABLE_CART)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db!!.execSQL("DROP TABLE IF EXISTS ${TABLE_PRODUCTS}")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CARTS")
        onCreate(db)
    }

    /**
     * Method to get user
     */
    fun getUser(namaUser: String): Int{
        val selectQuery = "SELECT * FROM $TABLE_USERS WHERE $KEY_USER_NAME = '${namaUser}'"

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException){
            db.execSQL(selectQuery)
        }
        if(cursor!!.count == 0){
            cursor!!.close()
            return 0
        }else{
            cursor.moveToFirst()
            var id: Int = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID))
            return id
        }
    }
    fun checkAuth(namaUser: String, passwordUser: String): Boolean {
//        val userList: ArrayList<UserModel> = ArrayList<UserModel>()

        val selectQuery = "SELECT * FROM $TABLE_USERS WHERE $KEY_USER_NAME = '${namaUser}' AND $KEY_USER_PASS = '${passwordUser}'"

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException){
            db.execSQL(selectQuery)
        }
        if(cursor!!.count == 0){
            cursor!!.close()
            return false
        }else{
            cursor!!.close()
            return true
        }
    }

    fun checkUser(namaUser: String): Boolean {
        val selectQuery = "SELECT * FROM $TABLE_USERS WHERE $KEY_USER_NAME = '${namaUser}'"

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException){
            db.execSQL(selectQuery)
        }
        if(cursor!!.count == 0){
            cursor!!.close()
            return false
        }else{
            cursor!!.close()
            return true
        }
    }
    /**
     * Method to insert data
     */
    fun addUser(user: UserModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_USER_NAME, user.namaUser)
        contentValues.put(KEY_USER_PASS, user.passwordUser)

        //inserting employee details using insert query.
        val success = db.insert(TABLE_USERS, null, contentValues)

        db.close()
        return success
    }

    fun addProducts(product: ProductModel):Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
//        contentValues.put(KEY_PRODUCT_NAME, product.nmProduct)
//        contentValues.put(KEY_PRODUCT_DESC, product.dsProduct)
//        contentValues.put(KEY_PRODUCT_PIC, product.picProduct)
//        contentValues.put(KEY_PRODUCT_PRICE, product.priceofProduct)

        var success = db.insert(TABLE_PRODUCTS, null, contentValues)
        db.close()
        return success

    }

    /**
     * Function to read the records
     */
//    fun viewProducts(): ArrayList<ProductModel> {
//        val productList: ArrayList<ProductModel> = ArrayList<ProductModel>()
//
//        val selectQuery = "SELECT * FROM $TABLE_PRODUCTS"
//
//        val db = this.readableDatabase
//
//        var cursor: Cursor? = null
//
//        try {
//            cursor = db.rawQuery(selectQuery, null)
//        }catch (e: SQLiteException){
//            db.execSQL(selectQuery)
//            return ArrayList()
//        }
//        var id: Int
//        var nmProduct: String
//        var dsProduct: String
//        var picProduct: Int
//        var priceofProduct: Int
//
//        if(cursor.moveToFirst()){
//            do{
//                id = cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_ID))
//                nmProduct = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_NAME))
//                dsProduct = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_DESC))
//                picProduct = cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_PIC))
//                priceofProduct = cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_PRICE))
//                val product = ProductModel(
//                    id,
//                    nmProduct,
//                    dsProduct,
////                    picProduct,
//                    priceofProduct
//                )
//                productList.add(product)
//            }while (cursor.moveToNext())
//        }
//        return productList
//    }
//    fun viewCartsByUser(userId: Int): ArrayList<CartModel> {
//        val cartList: ArrayList<CartModel> = ArrayList<CartModel>()
//
//        val selectQuery = "SELECT * FROM $TABLE_CARTS WHERE $KEY_CART_USER_ID=$userId"
//
//        val db = this.readableDatabase
//
//        var cursor: Cursor? = null
//
//        try {
//            cursor = db.rawQuery(selectQuery, null)
//        }catch (e: SQLiteException){
//            db.execSQL(selectQuery)
//            return ArrayList()
//        }
//        var id: Int
//        var idUser: Int
//        var idProduct: Int
//        var nmProduct: String
//        var priceofProduct: Int
//        var totalProduct: Int
//
//        if(cursor.moveToFirst()){
//            do{
//                id = cursor.getInt(cursor.getColumnIndex(KEY_CART_ID))
//                idUser = cursor.getInt(cursor.getColumnIndex(KEY_CART_USER_ID))
//                idProduct = cursor.getInt(cursor.getColumnIndex(KEY_CART_PRODUCT_ID))
//                nmProduct = cursor.getString(cursor.getColumnIndex(KEY_CART_PRODUCT_NAME))
//                priceofProduct = cursor.getInt(cursor.getColumnIndex(KEY_CART_PRODUCT_PRICE))
//                totalProduct = cursor.getInt(cursor.getColumnIndex(KEY_CART_PRODUCT_TOTAL))
//                val product = CartModel(
//                    id,
//                    idUser,
//                    idProduct,
//                    nmProduct,
//                    priceofProduct,
//                    totalProduct
//                )
//                cartList.add(product)
//            }while (cursor.moveToNext())
//        }
//        return cartList
//    }

//    fun addProductToCart(cart: CartModel):Long {
//        val db = this.writableDatabase
//
//        val contentValues = ContentValues()
//        contentValues.put(KEY_CART_USER_ID, cart.idUser)
//        contentValues.put(KEY_CART_PRODUCT_ID, cart.idProduct)
//        contentValues.put(KEY_CART_PRODUCT_NAME, cart.nmProduct)
//        contentValues.put(KEY_CART_PRODUCT_PRICE, cart.priceofProduct)
//        contentValues.put(KEY_CART_PRODUCT_TOTAL, cart.totalProduct)
//
//        var success = db.insert(TABLE_CARTS, null, contentValues)
//        return success
//        db.close()
//    }
}