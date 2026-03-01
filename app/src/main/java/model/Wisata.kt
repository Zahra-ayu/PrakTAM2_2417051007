package model
import androidx.annotation.DrawableRes

data class Wisata(
    val nama: String,
    val deskripsi: String,
    val lokasi: String,
    val hargaTiketAwal: Int,
    val hargaTiketAkhir: Int,
    @DrawableRes val imageRes: Int
)