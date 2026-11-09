package com.syntechhub.calculator

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : Activity() {
    private val calculator = CalculatorEngine()
    private lateinit var display: TextView
    private lateinit var expression: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)
        expression = findViewById(R.id.expression)
        bindButtons()
        render()
    }

    private fun bindButtons() {
        val digitButtons = mapOf(
            R.id.button_0 to '0',
            R.id.button_1 to '1',
            R.id.button_2 to '2',
            R.id.button_3 to '3',
            R.id.button_4 to '4',
            R.id.button_5 to '5',
            R.id.button_6 to '6',
            R.id.button_7 to '7',
            R.id.button_8 to '8',
            R.id.button_9 to '9',
        )
        digitButtons.forEach { (id, digit) ->
            findViewById<Button>(id).setOnClickListener {
                calculator.inputDigit(digit)
                render()
            }
        }

        val operatorButtons = mapOf(
            R.id.button_add to '+',
            R.id.button_subtract to '-',
            R.id.button_multiply to '×',
            R.id.button_divide to '/',
        )
        operatorButtons.forEach { (id, operator) ->
            findViewById<Button>(id).setOnClickListener {
                calculator.chooseOperator(operator)
                render()
            }
        }

        findViewById<Button>(R.id.button_decimal).setOnClickListener {
            calculator.inputDecimal()
            render()
        }
        findViewById<Button>(R.id.button_sign).setOnClickListener {
            calculator.toggleSign()
            render()
        }
        findViewById<Button>(R.id.button_delete).setOnClickListener {
            calculator.deleteLast()
            render()
        }
        findViewById<Button>(R.id.button_clear).setOnClickListener {
            calculator.clear()
            render()
        }
        findViewById<Button>(R.id.button_equals).setOnClickListener {
            calculator.calculateResult()
            render()
        }
    }

    private fun render() {
        display.text = calculator.display
        expression.text = calculator.expression.ifEmpty { getString(R.string.calculator_hint) }
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        display.textSize = when {
            calculator.hasError -> 22f
            calculator.display.length > 12 -> if (isLandscape) 30f else 36f
            isLandscape -> 44f
            else -> 64f
        }
    }
}
