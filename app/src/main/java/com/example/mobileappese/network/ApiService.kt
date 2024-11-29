package com.example.mobileappese.network

import retrofit2.Call
import retrofit2.http.*
import com.example.mobileappese.model.User

interface ApiService {
    @GET("my_api/getUsers.php")
    fun getUsers(): Call<List<User>>

    @FormUrlEncoded
    @POST("my_api/addUser.php")
    fun addUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("dob") dob: String
    ): Call<Map<String, String>>

    @FormUrlEncoded
    @POST("my_api/addToCart.php")
    fun addToCart(
        @Field("username") username: String,
        @Field("pname") pname: String,
        @Field("pprice") pprice: Int,
        @Field("quantity") quantity: Int
    ): Call<Map<String, String>>

    @FormUrlEncoded
    @POST("my_api/getItemsFromCart.php")
    fun getItemsFromCart(
        @Field("username") username: String
    ): Call<Map<String, Any>>

    @FormUrlEncoded
    @POST("my_api/deleteFromCart.php")
    fun deleteFromCart(
        @Field("username") username: String
    ): Call<Map<String, String>>
}
