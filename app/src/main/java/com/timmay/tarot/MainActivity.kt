package com.timmay.tarot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import com.timmay.tarot.ui.TarotAppRoot
import com.timmay.tarot.ui.theme.TarotReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(value=26)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TarotReaderTheme {
                Surface {
                    TarotAppRoot()
                }
            }
        }
    }
}
