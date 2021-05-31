package com.example.navigasiapp

data class CartModel(
    var id: Int,
    var idUser : Int,
    var idProduct : Int,
    var nmProduct: String,
    var priceofProduct : Int,
    var totalProduct : Int
)