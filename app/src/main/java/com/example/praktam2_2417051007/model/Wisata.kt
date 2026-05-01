package com.example.praktam2_2417051007.model
import com.google.gson.annotations.SerializedName

data class Wisata(
    @SerializedName("nama")
    val nama: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("lokasi")
    val lokasi: String,

    @SerializedName("hargaTiketAwal")
    val hargaTiketAwal: Int,

    @SerializedName("hargaTiketAkhir")
    val hargaTiketAkhir: Int,

    @SerializedName("image_url")
    val imageUrl: String
)