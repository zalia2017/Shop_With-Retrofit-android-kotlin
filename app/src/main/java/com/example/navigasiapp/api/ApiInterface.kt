package com.example.navigasiapp.api

import com.example.navigasiapp.model.DefaultResponse
import com.example.navigasiapp.model.LoginResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email:String,
        @Field("password") password:String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DefaultResponse>

    @GET("categories")
    fun getCategories(@Header("Authorization") authHeader:String)
            : Call<JsonObject>

    @GET("products/searchByCategory/{categoryId}")
    fun getProductsByCategory(@Header("Authorization") authHeader:String,
                              @Path("categoryId") id: Int): Call<JsonObject>

    @FormUrlEncoded
    @POST("carts")
    fun addProductToCart(@Header("Authorization") authHeader: String,
                         @Field("product_id") productId: Int,
                         @Field("price") price: Int,
                         @Field("quantity") quantity: Int) : Call<DefaultResponse>

    @GET("carts/showByUser")
    fun getCartsByUser(@Header("Authorization") authHeader: String): Call<JsonObject>
}