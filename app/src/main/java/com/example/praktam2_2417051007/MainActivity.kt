package com.example.praktam2_2417051007

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import com.example.praktam2_2417051007.network.RetrofitClient
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.praktam2_2417051007.ui.theme.PrakTAM2_2417051007Theme
import com.example.praktam2_2417051007.model.Wisata
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrakTAM2_2417051007Theme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    var wisataa by remember { mutableStateOf<List<Wisata>>(emptyList()) }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            DaftarWisataScreen(navController) { fetchedWisata ->
                wisataa = fetchedWisata
            }
        }

        composable("detail/{nama}") { backStackEntry ->
            val nama = backStackEntry.arguments?.getString("nama")
            val wisata = wisataa.find {
                it.nama == nama
            }
            if (wisata != null) {
                DetailScreen(wisata = wisata, navController = navController, isFullScreen = true)
            }
        }
    }
}

@Composable
fun DaftarWisataScreen(navController: NavController, onWisataLoaded: (List<Wisata>) -> Unit = {}){
    var wisataa by remember { mutableStateOf<List<Wisata>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try{
            wisataa = RetrofitClient.instance.getWisata()
            onWisataLoaded(wisataa)
            isLoading = false
            isError = false
        } catch (_: Exception) {
            isLoading = false
            isError = true
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (isError || wisataa.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gagal Memuat Data",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pastikan koneksi internet Anda menyala",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "Rekomendasi Populer",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(wisataa) { wisata ->
                        WisataRowItem(wisata = wisata, navController = navController)
                    }
                }
                Spacer(modifier = Modifier.height(45.dp))
                Text(
                    text = "Daftar Wisata Lengkap",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(wisataa) { wisata ->
                WisataItem(wisata = wisata, navController = navController)
            }
        }
    }
}

@Composable
fun WisataRowItem(wisata: Wisata, navController: NavController) {
//    val context = LocalContext.current
//    val resId = WisataSource.getResourceId(context, wisata.imageName)
//    val imageRes = if (resId != 0) resId else R.drawable.gigi_hiu

    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable{navController.navigate("detail/${wisata.nama}")},
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            AsyncImage(
                model = wisata.imageUrl,
                contentDescription = wisata.nama,
                placeholder = painterResource(id = R.drawable.gigi_hiu),
                error = painterResource(id = R.drawable.way_kambas),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = wisata.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rp ${wisata.hargaTiketAwal} - ${wisata.hargaTiketAkhir}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFFF2B2B)
                )
            }
        }
    }
}

@Composable
fun DetailScreen(wisata: Wisata, navController: NavController, isFullScreen: Boolean = false) {
    var isFavorite by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
//    val context = LocalContext.current
//    val resId = WisataSource.getResourceId(context, wisata.imageUrl)
//    val imageRes = if (resId != 0) resId else R.drawable.gigi_hiu


    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box {
                    AsyncImage(
                        model = wisata.imageUrl,
                        contentDescription = wisata.nama,
                        placeholder = painterResource(id = R.drawable.gigi_hiu),
                        error = painterResource(id = R.drawable.way_kambas),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = wisata.nama,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (isFullScreen) {
                    Text(
                        text = wisata.deskripsi,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Lokasi"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Lokasi: ${wisata.lokasi}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = "Tiket"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Harga Tiket: Rp ${wisata.hargaTiketAwal} - ${wisata.hargaTiketAkhir}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (isFullScreen) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                delay(2000)
                                snackbarHostState.showSnackbar(
                                    "Pesanan ${wisata.nama} berhasil diproses!"
                                )
                                isLoading = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Memproses...")
                        } else {
                            Text("Pesan Sekarang")
                        }
                    }
                }
                Button(
                    onClick = {
                        if (isFullScreen) {
                            navController.popBackStack()
                        } else {
                            navController.navigate("detail/${wisata.nama}")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isFullScreen) "Kembali" else "Lihat Detail")
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun WisataItem(wisata: Wisata, navController: NavController) {
    DetailScreen(wisata = wisata,
        navController = navController,
        isFullScreen = false
    )
}

@Preview(showBackground = true)
@Composable
fun DaftarWisataPreview() {
    PrakTAM2_2417051007Theme {
        DaftarWisataScreen(navController = rememberNavController())
    }
}