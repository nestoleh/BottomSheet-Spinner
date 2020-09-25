package com.nestoleh.bottomsheetspinner.sample

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.nestoleh.bottomsheetspinner.OnBottomSheetSpinnerItemSelected
import com.nestoleh.bottomsheetspinner.sample.spinner.ShapeSpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerAdapter: ShapeSpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    private fun initUI() {
        initSpinner()

        clearButton.setOnClickListener { spinnerAdapter.updateData(emptyList()) }

        resetButton.setOnClickListener { resetSpinnerAdapter() }

        shuffleButton.setOnClickListener {
            val shapes = Shape.values().apply { shuffle() }
            spinnerAdapter.updateData(shapes.toList())
        }

        setPositionButton.setOnClickListener {
            hideKeyboard()
            val position = positionEditText.text.toString().toIntOrNull() ?: 0
            try {
                spinner.setSelection(position)
                positionInputLayout.error = null
            } catch (e: Exception) {
                positionInputLayout.error = e.message
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    private fun initSpinner() {
        spinnerAdapter = ShapeSpinnerAdapter(emptyList())
        spinner.onItemSelectedListener = object : OnBottomSheetSpinnerItemSelected {

            override fun onItemSelected(position: Int) {
                selectedItemTextView.text = spinner.getSelectedItem<Shape>()?.name ?: "null"
                selectedItemPositionTextView.text = spinner.getSelectedItemPosition().toString()
            }

            override fun onNothingSelected() {
                selectedItemTextView.text = "null"
                selectedItemPositionTextView.text = "null"
            }
        }
        spinner.setAdapter(spinnerAdapter)
        resetSpinnerAdapter()
    }

    private fun resetSpinnerAdapter() {
        spinnerAdapter.updateData(Shape.values().toList())
    }
}