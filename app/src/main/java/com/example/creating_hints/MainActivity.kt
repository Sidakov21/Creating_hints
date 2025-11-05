package com.example.creating_hints

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.creating_hints.databinding.ActivityMainBinding
import com.example.creating_hints.ui.theme.Creating_hintsTheme

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.post {
            setupTooltips()
        }
    }

    private fun setupTooltips() {
        TooltipCompat.setTooltipText(binding.btnAction, "Эта кнопка выполняет основное действие")

        showCustomTooltip(binding.etEmail, "Введите ваш email адрес", TooltipPosition.TOP)
    }

    private fun showCustomTooltip(anchorView: android.view.View, message: String, position: TooltipPosition) {
        val tooltip = Tooltip.Builder(anchorView, message)
            .setPosition(position)
            .setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark))
            .setTextColor(ContextCompat.getColor(this, android.R.color.white))
            .setCornerRadius(16f)
            .setDismissOnClick(true)
            .build()

        tooltip.show()
    }
}