package com.example.praktam2_2417051007.data.repository
import com.example.praktam2_2417051007.data.api.RetrofitClient
import com.example.praktam2_2417051007.data.model.Wisata

class WisataRepository {
    suspend fun getWisata(): List<Wisata> {
        return try {
            RetrofitClient.instance.getWisata()
        } catch (e: Exception) {
            emptyList()
        }
    }
}