package com.example.praktam2_2417051007.data.api

import com.example.praktam2_2417051007.data.model.Wisata
import retrofit2.http.GET

interface ApiService {
    @GET("menu_wisata.json")
    suspend fun getWisata(): List<Wisata>
}