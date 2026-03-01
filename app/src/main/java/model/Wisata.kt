package model
import androidx.annotation.DrawableRes

data class Wisata(
    val nama: String,
    val deskripsi: String,
    val lokasi: String,
    val harga_tiket_awal: Int,
    val harga_tiket_akhir: Int,
    @DrawableRes val imageRes: Int
)