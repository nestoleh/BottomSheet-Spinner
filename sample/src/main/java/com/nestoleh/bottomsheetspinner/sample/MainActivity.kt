package com.nestoleh.bottomsheetspinner.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    private fun initUI() {
        checkButton.setOnClickListener {
            // TODO: show toast with information about selected item in spinner
            Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
        }
    }
}