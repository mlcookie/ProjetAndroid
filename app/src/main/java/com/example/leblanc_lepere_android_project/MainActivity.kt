package com.example.leblanc_lepere_android_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.leblanc_lepere_android_project.ui.theme.LEBLANC_LEPERE_Android_projectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LEBLANC_LEPERE_Android_projectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        onGoToProductList = {
                            val intent = Intent(this, ListProductActivity::class.java)
                            startActivity(intent)
                        },
                        onGoToQRScanner = {
                            val intent = Intent(this, QRScanActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(onGoToProductList: () -> Unit,
               onGoToQRScanner: () -> Unit,
               modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Bienvenue !",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Explorez notre boutique en ligne 🛒",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = onGoToProductList) {
            Text("Voir la liste des produits")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onGoToQRScanner) {
            Text("Scanner un QR Code 📷")
        }

    }
}
