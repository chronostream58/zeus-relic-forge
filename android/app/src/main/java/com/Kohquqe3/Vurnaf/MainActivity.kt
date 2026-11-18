package com.Kohquqe3.Vurnaf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.Kohquqe3.Vurnaf.core.di.ServiceLocator
import com.Kohquqe3.Vurnaf.core.theme.AppTheme
import com.Kohquqe3.Vurnaf.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
