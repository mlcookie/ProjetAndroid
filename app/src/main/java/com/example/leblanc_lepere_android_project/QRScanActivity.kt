package com.example.leblanc_lepere_android_project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import android.os.VibrationEffect
import android.os.Vibrator



class QRScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var scannerView: ZXingScannerView
    private val CAMERA_PERMISSION_REQUEST_CODE = 123


    suspend fun resolveShortUrl(shortUrl: String): String? = withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(shortUrl)
                .head()
                .build()
            val response = client.newCall(request).execute()
            response.close()
            response.request.url.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("QRScan", "onCreate called")
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
    }

    override fun onResume() {
        super.onResume()
        Log.d("QRScan", "onResume called")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            Log.d("QRScan", "Camera permission not granted, requesting it")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            Log.d("QRScan", "Camera permission granted, starting camera")
            scannerView.setResultHandler(this)
            scannerView.startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("QRScan", "onPause called, stopping camera")
        scannerView.stopCamera()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("QRScan", "Camera permission granted by user")
                scannerView.setResultHandler(this)
                scannerView.startCamera()
            } else {
                Log.e("QRScan", "Camera permission denied by user")
                Toast.makeText(this, "Permission caméra refusée", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }



    override fun handleResult(rawResult: Result?) {
        val scannedUrl = rawResult?.text
        Log.d("QRScan", "Texte scanné : $scannedUrl")
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(150)
        }


        if (scannedUrl != null) {
            lifecycleScope.launch {
                val finalUrl = resolveShortUrl(scannedUrl) ?: scannedUrl
                Log.d("QRScan", "URL finale : $finalUrl")

                val productIdStr = finalUrl.substringAfterLast("/")
                val productId = productIdStr.toIntOrNull()

                if (productId != null) {
                    val intent = Intent(this@QRScanActivity, ProductDetailActivity::class.java)
                    intent.putExtra("PRODUCT_ID", productId)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@QRScanActivity, "QR code invalide : ID produit non trouvé", Toast.LENGTH_LONG).show()
                    scannerView.resumeCameraPreview(this@QRScanActivity)
                }
            }
        } else {
            Toast.makeText(this, "QR code vide", Toast.LENGTH_LONG).show()
            scannerView.resumeCameraPreview(this)
        }
    }



}
