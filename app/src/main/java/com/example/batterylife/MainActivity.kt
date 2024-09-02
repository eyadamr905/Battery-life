package com.example.batterylife

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.batterylife.ui.theme.BatteryLifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BatteryLifeTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    BatteryLevelMonitor(
                        modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BatteryLevelMonitor(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var batteryLevel by remember { mutableStateOf(100) }

    val batteryReceiver = remember {
        BatteryLevelReceiver { level ->
            batteryLevel = level
        }
    }

    DisposableEffect(Unit) {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(batteryReceiver, intentFilter)

        onDispose {
            context.unregisterReceiver(batteryReceiver)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            batteryLevel < 20 -> {
                LowBatteryAlert()
            }

            else -> {
                NormalBatteryLevel()
            }
        }
    }
}
@Composable
fun LowBatteryAlert() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.battery_low),
            contentDescription = "Low Battery",
            modifier = Modifier.size(300.dp)
        )
        Text(
            text = "Battery is low! Please charge your device.",
            color = Color.Red
        )
    }
}

@Composable
fun NormalBatteryLevel() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.battery_full),
            contentDescription = "Normal Battery",
            modifier = Modifier.size(300.dp)
        )
        Text(text = "Battery level is normal.")
    }
}