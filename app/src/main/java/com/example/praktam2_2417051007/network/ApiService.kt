package com.example.praktam2_2417051007.network
import com.example.praktam2_2417051007.model.Wisata
import retrofit2.http.GET

interface ApiService {
    @GET("menu_wisata.json")
    suspend fun getWisata(): List<Wisata>
}