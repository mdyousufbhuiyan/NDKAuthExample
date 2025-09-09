package com.loc.kotlinassignmentproject.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import com.loc.kotlinassignmentproject.NativeLib
import com.loc.kotlinassignmentproject.data.crypto.CryptoStore
import com.loc.kotlinassignmentproject.data.remote.AuthInterceptor
import com.loc.apikeyencritiopdemp.ui.theme.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var cryptoStore: CryptoStore
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // First-run: assemble key from native and store encrypted.
        if (!cryptoStore.contains(AuthInterceptor.KEY_NAME)) {
            val rawKey = NativeLib.encryptApiKey("my api key 12345")
            // Optionally obfuscate or transform before storing
            cryptoStore.putString(AuthInterceptor.KEY_NAME, rawKey)
        }

        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(vm::callApi, vm)
                }
            }
        }
    }
}

@Composable
fun MainScreen(onCall: () -> Unit, vm: MainViewModel) {
    val text by vm.text.collectAsState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onCall) { Text("Call API") }
        }
    }
}
