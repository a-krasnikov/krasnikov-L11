package com.example.application

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.application.databinding.ActivityCongratulationBinding

class CongratulationActivity : AppCompatActivity() {

    companion object {
        private const val TEXT_KEY = "TEXT_KEY"
        private const val YEAR_KEY = "YEAR_KEY"

        fun start(context: Context, text: String, year: Int) {
            val intent = Intent(context, CongratulationActivity::class.java)
            intent.putExtra(TEXT_KEY, text)
            intent.putExtra(YEAR_KEY, year)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityCongratulationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupData()
    }

    private fun setupBinding() {
        binding = ActivityCongratulationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupData() {
        val text = intent.getStringExtra(TEXT_KEY)
        val year = intent.getIntExtra(YEAR_KEY, 2021)
        binding.tvText.text = text
        binding.tvYear.text = year.toString()
    }
}