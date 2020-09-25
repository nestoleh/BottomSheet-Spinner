package com.nestoleh.bottomsheetspinner.sample

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.nestoleh.bottomsheetspinner.OnBottomSheetSpinnerItemSelected
import com.nestoleh.bottomsheetspinner.sample.spinner.ShapeSpinnerAdapter
import com.nestoleh.bottomsheetspinner.sample.spinner.ShapeSpinnerEntity
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

        shuffleButton.setOnClickListener { shuffleData() }

        setPositionButton.setOnClickListener {
            hideKeyboard()
            val position = positionEditText.text.toString().toIntOrNull() ?: 0
            if (spinner.setSelectionIfPossible(position)) {
                positionInputLayout.error = null
            } else {
                positionInputLayout.error =
                    "Position $position can't be selected (out of range or item disabled)"
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
                selectedItemTextView.text =
                    spinner.getSelectedItem<ShapeSpinnerEntity.Item>()?.value?.name ?: "null"
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

    private fun shuffleData() {
        val shuffled = getSpinnerList().shuffled()
        spinnerAdapter.updateData(shuffled)
    }

    private fun resetSpinnerAdapter() {
        spinnerAdapter.updateData(getSpinnerList())
    }

    private fun getSpinnerList(): List<ShapeSpinnerEntity> {
        var index = 1
        val entities = ArrayList<ShapeSpinnerEntity>()
        Shape.values().forEach { shape ->
            entities.add(ShapeSpinnerEntity.Header("Group $index"))
            index++
            entities.add(ShapeSpinnerEntity.Item(shape))
        }
        return entities
    }
}