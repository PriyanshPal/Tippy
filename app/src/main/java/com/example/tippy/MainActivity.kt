package com.example.tippy

import android.animation.ArgbEvaluator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var etNumberOfPeople: EditText
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var tvFinalAmount: TextView
    private lateinit var seekBarTip: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        etNumberOfPeople = findViewById(R.id.etNumberOfPeople)
        tvFinalAmount = findViewById(R.id.tvFinalAmount)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        etNumberOfPeople.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(p: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(p: Editable?) {
                Log.i(TAG, "after text changed $p")
                distributeAmountEqually()
            }

        })
        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "after text changed $s")
                computeTipAndTotal()
            }

        })

    }

    private fun distributeAmountEqually() {
        if(etNumberOfPeople.text.isEmpty()) {
            tvFinalAmount.text = ""
            return
        }
        val baseAmount = tvTotalAmount.text.toString().toDouble()
        val numberOfPeople = etNumberOfPeople.text.toString().toInt()
        val EqualAmount = baseAmount / numberOfPeople
        tvFinalAmount.text = "%.2f".format(EqualAmount)
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when(tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription

        //Updating the color of the tipDescription
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if(etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        val tip = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tip

        tvTipAmount.text = "%.2f".format(tip)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}