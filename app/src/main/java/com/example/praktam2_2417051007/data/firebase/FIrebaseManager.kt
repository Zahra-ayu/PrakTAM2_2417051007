package com.example.praktam2_2417051007.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseManager {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    val currentUserId: String?
        get() = auth.currentUser?.uid

    val currentUserEmail: String?
        get() = auth.currentUser?.email

    suspend fun register(name: String, email: String, password: String, phone: String): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("UID null setelah register")
            // Simpan profil ke Firestore
            db.collection("users").document(uid).set(
                mapOf(
                    "name" to name,
                    "email" to email,
                    "phone" to phone
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun getUserProfile(): UserProfile? {
        val uid = currentUserId ?: return null
        return try {
            val doc = db.collection("users").document(uid).get().await()
            UserProfile(
                name = doc.getString("name") ?: "",
                email = doc.getString("email") ?: currentUserEmail ?: "",
                phone = doc.getString("phone") ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getFavorites(): Set<String> {
        val uid = currentUserId ?: return emptySet()
        return try {
            val doc = db.collection("users").document(uid)
                .collection("favorites").document("list").get().await()
            @Suppress("UNCHECKED_CAST")
            (doc.get("items") as? List<String>)?.toSet() ?: emptySet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    suspend fun setFavorite(namaWisata: String, isFav: Boolean) {
        val uid = currentUserId ?: return
        val ref = db.collection("users").document(uid)
            .collection("favorites").document("list")
        try {
            val doc = ref.get().await()
            val current = @Suppress("UNCHECKED_CAST")
            (doc.get("items") as? MutableList<String>) ?: mutableListOf()
            if (isFav) { if (!current.contains(namaWisata)) current.add(namaWisata) }
            else current.remove(namaWisata)
            ref.set(mapOf("items" to current)).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveTransaksi(transaksi: com.example.praktam2_2417051007.data.model.Transaksi) {
        val uid = currentUserId ?: return
        try {
            db.collection("users").document(uid)
                .collection("transaksi")
                .add(
                    mapOf(
                        "namaWisata" to transaksi.namaWisata,
                        "jumlahTiket" to transaksi.jumlahTiket,
                        "totalHarga" to transaksi.totalHarga,
                        "tanggal" to transaksi.tanggal,
                        "imageUrl" to transaksi.imageUrl,
                        "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                    )
                ).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getTransaksiList(): List<com.example.praktam2_2417051007.data.model.Transaksi> {
        val uid = currentUserId ?: return emptyList()
        return try {
            val snapshot = db.collection("users").document(uid)
                .collection("transaksi")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get().await()
            snapshot.documents.map { doc ->
                com.example.praktam2_2417051007.data.model.Transaksi(
                    namaWisata = doc.getString("namaWisata") ?: "",
                    jumlahTiket = (doc.getLong("jumlahTiket") ?: 0).toInt(),
                    totalHarga = (doc.getLong("totalHarga") ?: 0).toInt(),
                    tanggal = doc.getString("tanggal") ?: "",
                    imageUrl = doc.getString("imageUrl") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getReviews(namaWisata: String): List<com.example.praktam2_2417051007.data.model.Review> {
        return try {
            val snapshot = db.collection("reviews")
                .document(namaWisata)
                .collection("list")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get().await()
            snapshot.documents.map { doc ->
                com.example.praktam2_2417051007.data.model.Review(
                    username = doc.getString("username") ?: "",
                    rating = (doc.getLong("rating") ?: 5).toInt(),
                    komentar = doc.getString("komentar") ?: "",
                    tanggal = doc.getString("tanggal") ?: ""
                )
            }.ifEmpty { getMockReviews() }
        } catch (e: Exception) {
            getMockReviews()
        }
    }

    suspend fun saveReview(namaWisata: String, review: com.example.praktam2_2417051007.data.model.Review) {
        try {
            db.collection("reviews").document(namaWisata).collection("list").add(
                mapOf(
                    "username" to review.username,
                    "rating" to review.rating,
                    "komentar" to review.komentar,
                    "tanggal" to review.tanggal,
                    "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getMockReviews() = listOf(
        com.example.praktam2_2417051007.data.model.Review("Andi Saputra", 5, "Sangat indah dan menakjubkan! Wajib dikunjungi bersama keluarga.", "05-06-2026"),
        com.example.praktam2_2417051007.data.model.Review("Budi Hartono", 4, "Tempatnya bersih dan pemandangannya luar biasa, tapi akses jalannya perlu sedikit perbaikan.", "01-06-2026")
    )
}