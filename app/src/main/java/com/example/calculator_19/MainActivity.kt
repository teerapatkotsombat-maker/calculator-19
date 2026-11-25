package com.example.calculator_19

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var current = ""
    private var operator = ""
    private var firstNumber = 0.0
    private var secondNumber = 0.0
    private var result = 0.0
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        display = findViewById(R.id.txtDisplay)
        val numberbutton = listOf(
            R.id.btnZero,
            R.id.btnOne,
            R.id.btnTwo,
            R.id.btnThree,
            R.id.btnFour,
            R.id.btnFive,
            R.id.btnSix,
            R.id.btnSeven,
            R.id.btnEight,
            R.id.btnNine,
        )
        // กำหนดอีเว้นท์ให้ปุ่มตัวเลขทุกปุ่ม
        for (id in numberbutton) {
            findViewById<Button>(id).setOnClickListener {
                numberclicked((it as Button).text.toString())
            }
        }
        // เชื่อมปุ่มคำนวณ
        findViewById<Button>(R.id.btnPlus).setOnClickListener { operatorclicked("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { operatorclicked("-") }
        findViewById<Button>(R.id.btnX).setOnClickListener { operatorclicked("*") }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { operatorclicked("/") }
        findViewById<Button>(R.id.btnMod).setOnClickListener { operatorclicked("%") }

        // เชื่อมโยงปุ่มพิเศษ
        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.btnAc).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnPoint).setOnClickListener { addDot() }
        findViewById<Button>(R.id.btnMP).setOnClickListener { toggleSign() }
        findViewById<Button>(R.id.btnDel).setOnClickListener { deleteLastDigit() }
    }

    // เมื่อกดปุ่มตัวเลข
    private fun numberclicked(value: String) {
        if (isNewOperation) {
            current = ""
            isNewOperation = false
        }
        if (current == "0") {
            current = value
        } else {
            current += value
        }
        display.text = current
    }

    private fun operatorclicked(op: String) {
        if (current.isNotEmpty()) {
            firstNumber = current.toDoubleOrNull() ?: 0.0
            operator = op
            current = ""
            isNewOperation = false
        }
    }

    private fun calculateResult() {
        if (current.isNotEmpty() && operator.isNotEmpty()) {
            secondNumber = current.toDoubleOrNull() ?: 0.0
            result = when (operator) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "*" -> firstNumber * secondNumber
                "/" -> if (secondNumber != 0.0) firstNumber / secondNumber else Double.NaN
                "%" -> firstNumber % secondNumber
                else -> 0.0
            }
            if (result.isNaN()) {
                display.text = "Cannot divide by zero"
                current = ""
            } else {
                val resultText = if (result % 1 == 0.0 && !result.isInfinite()) {
                    result.toLong().toString()
                } else {
                    result.toString()
                }
                display.text = resultText
                current = resultText
            }
            isNewOperation = true
        }
    }

    private fun clearAll() {
        current = ""
        operator = ""
        firstNumber = 0.0
        secondNumber = 0.0
        result = 0.0
        display.text = "0"
        isNewOperation = true
    }

    private fun addDot() {
        if (isNewOperation) {
            current = "0"
            isNewOperation = false
        }
        if (!current.contains(".")) {
            if (current.isEmpty()) current = "0"
            current += "."
            display.text = current
        }
    }

    private fun toggleSign() {
        if (current.isNotEmpty() && current != "0") {
            current = if (current.startsWith("-")) {
                current.substring(1)
            } else {
                "-$current"
            }
            display.text = current
        }
    }

    private fun deleteLastDigit() {
        if (current.isNotEmpty() && !isNewOperation) {
            current = current.dropLast(1)
            if (current.isEmpty() || current == "-") {
                current = ""
                display.text = "0"
            } else {
                display.text = current
            }
        }
    }
}
