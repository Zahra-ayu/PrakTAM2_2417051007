package com.example.praktam2_2417051007

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktam2_2417051007.ui.theme.PrakTAM2_2417051007Theme
import model.WisataSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrakTAM2_2417051007Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    val wisata = WisataSource.dummyWisata[0]

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = wisata.imageRes),
            contentDescription = wisata.nama,
            modifier = Modifier.size(300.dp, 200.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = wisata.nama,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
        )
        Card(modifier = Modifier.size(350.dp, 125.dp),) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row() {
                    Text(text = "\uD83D\uDCCD Lokasi: ")
                    Text(text = wisata.lokasi)
                }

                Row() {
                    Text(text = "\uD83D\uDCB0 Tiket: ")
                    Text(text = "Rp ${wisata.harga_tiket_awal} - ${wisata.harga_tiket_akhir}")
                }
            }
        }

        Text(text = wisata.deskripsi,
            color = Color(0xFFC0C2C9),
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(top = 16.dp))


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PrakTAM2_2417051007Theme {
    }
}