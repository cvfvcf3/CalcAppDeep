package com.yourapp.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private var currentNumber = ""
    private var firstNumber = ""
    private var operator = ""
    private var isNewOperation = true
    private val historyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)
        
        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        
        numberButtons.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                appendNumber((it as Button).text.toString())
            }
        }
        
        findViewById<Button>(R.id.btnDot).setOnClickListener { appendNumber(".") }
        findViewById<Button>(R.id.btnPlus).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { setOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperator("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperator("÷") }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculate() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { calculatePercentage() }
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener { toggleSign() }
    }
    
    private fun appendNumber(num: String) {
        if (isNewOperation) {
            currentNumber = ""
            isNewOperation = false
        }
        if (num == "." && currentNumber.contains(".")) return
        currentNumber += num
        tvResult.text = currentNumber
    }
    
    private fun setOperator(op: String) {
        if (currentNumber.isEmpty()) return
        firstNumber = currentNumber
        operator = op
        tvExpression.text = "$firstNumber $op"
        isNewOperation = true
    }
    
    private fun calculate() {
        if (firstNumber.isEmpty() || currentNumber.isEmpty() || operator.isEmpty()) return
        val num1 = firstNumber.toDouble()
        val num2 = currentNumber.toDouble()
        val result = when (operator) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "×" -> num1 * num2
            "÷" -> if (num2 != 0.0) num1 / num2 else {
                tvResult.text = "Error"
                return
            }
            else -> 0.0
        }
        val formattedResult = DecimalFormat("#.##########").format(result)
        val fullExpression = "$firstNumber $operator $currentNumber = $formattedResult"
        
        // ✅ Save to History with full detail
        historyList.add(0, fullExpression)
        
        tvExpression.text = fullExpression
        tvResult.text = formattedResult
        firstNumber = formattedResult
        currentNumber = formattedResult
        operator = ""
        isNewOperation = true
    }
    
    private fun clearAll() {
        currentNumber = ""
        firstNumber = ""
        operator = ""
        tvExpression.text = ""
        tvResult.text = "0"
        isNewOperation = true
    }
    
    private fun calculatePercentage() {
        if (currentNumber.isNotEmpty()) {
            val num = currentNumber.toDouble() / 100
            currentNumber = num.toString()
            tvResult.text = currentNumber
        }
    }
    
    private fun toggleSign() {
        if (currentNumber.isNotEmpty()) {
            val num = currentNumber.toDouble() * -1
            currentNumber = num.toString()
            tvResult.text = currentNumber
        }
    }
}
