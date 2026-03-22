package com.example.praktam2_2417051007

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation. Image
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
import com.example.praktam2_2417051007.ui.theme.PrakTAM2_2417051007Theme
import model.Wisata
import model.WisataSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrakTAM2_2417051007Theme {
                DaftarWisataScreen()
            }
        }
    }
}

@Composable
fun DaftarWisataScreen(){
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
                items(WisataSource.dummyWisata) { wisata ->
                    WisataRowItem(wisata = wisata)
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
        items(WisataSource.dummyWisata) { wisata ->
            DetailScreen(wisata = wisata)
        }

    }
}

@Composable
fun WisataRowItem(wisata: Wisata) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = wisata.imageRes),
                contentDescription = wisata.nama,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = wisata.nama,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rp ${wisata.hargaTiketAwal} - ${wisata.hargaTiketAkhir}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun DetailScreen(wisata: Wisata){
    var isFavorite by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Box {
                Image(
                    painter = painterResource(id = wisata.imageRes),
                    contentDescription = wisata.nama,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.align(Alignment.TopEnd)
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

            Text(
                text = wisata.deskripsi,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify,
            )
            Spacer(modifier = Modifier.height(8.dp))

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

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pesan Sekarang")
            }
        }
    }
}
//fun Greeting() {
//    val wisata = WisataSource.dummyWisata[0]
//
//    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
//        Image(
//            painter = painterResource(id = wisata.imageRes),
//            contentDescription = wisata.nama,
//            modifier = Modifier.size(300.dp, 200.dp),
//            contentScale = ContentScale.Crop
//        )
//        Text(text = wisata.nama,
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .padding(16.dp)
//        )
//
//        Card(modifier = Modifier.size(350.dp, 125.dp)) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Row() {
//                    Text(text = "\uD83D\uDCCD Lokasi: ")
//                    Text(text = wisata.lokasi)
//                }
//
//                Row() {
//                    Text(text = "\uD83D\uDCB0 Tiket: ")
//                    Text(text = "Rp ${wisata.hargaTiketAwal} - ${wisata.hargaTiketAkhir}")
//                }
//            }
//        }
//
//        Text(text = wisata.deskripsi,
//            color = Color(0xFFC0C2C9),
//            textAlign = TextAlign.Justify,
//            modifier = Modifier
//                .padding(top = 16.dp))
//
//
//    }
//}

@Preview(showBackground = true)
@Composable
fun DaftarWisataPreview() {
    PrakTAM2_2417051007Theme {
        DaftarWisataScreen()
    }
}