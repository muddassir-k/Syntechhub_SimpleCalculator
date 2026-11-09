package com.syntechhub.calculator

import java.math.BigDecimal
import java.math.RoundingMode

class CalculatorEngine {
    var display: String = "0"
        private set

    var expression: String = ""
        private set

    var hasError: Boolean = false
        private set

    private var storedValue: BigDecimal? = null
    private var pendingOperator: Char? = null
    private var startsNewInput = false

    fun inputDigit(digit: Char) {
        require(digit in '0'..'9') { "Digit must be between 0 and 9." }
        if (hasError) clear()

        val digitCount = display.count(Char::isDigit)
        if (!startsNewInput && digitCount >= MAX_INPUT_DIGITS) return

        display = when {
            startsNewInput -> digit.toString()
            display == "-0" -> "-$digit"
            display == "0" -> digit.toString()
            else -> display + digit
        }
        startsNewInput = false
    }

    fun inputDecimal() {
        if (hasError) clear()
        if (startsNewInput) {
            display = "0."
            startsNewInput = false
        } else if ('.' !in display) {
            display += "."
        }
    }

    fun chooseOperator(operator: Char) {
        require(operator in OPERATORS) { "Unsupported operator: $operator" }
        if (hasError) clear()

        val pending = pendingOperator
        val stored = storedValue
        if (pending != null && stored != null && !startsNewInput) {
            val result = calculate(stored, currentValue(), pending) ?: return
            display = format(result)
            storedValue = result
        } else if (storedValue == null || pendingOperator == null) {
            storedValue = currentValue()
        }

        pendingOperator = operator
        expression = "${format(storedValue ?: currentValue())} $operator"
        startsNewInput = true
    }

    fun calculateResult() {
        val operator = pendingOperator ?: return
        val left = storedValue ?: return
        val result = calculate(left, currentValue(), operator) ?: return
        display = format(result)
        expression = ""
        storedValue = null
        pendingOperator = null
        startsNewInput = true
    }

    fun toggleSign() {
        if (hasError) clear()
        display = if (startsNewInput && pendingOperator != null) {
            "-0"
        } else {
            format(currentValue().negate())
        }
        startsNewInput = false
    }

    fun deleteLast() {
        if (hasError) {
            clear()
            return
        }
        if (startsNewInput) return

        display = when {
            display.length <= 1 || (display.length == 2 && display.startsWith("-")) -> "0"
            else -> display.dropLast(1).let { if (it == "-") "0" else it }
        }
    }

    fun clear() {
        display = "0"
        expression = ""
        storedValue = null
        pendingOperator = null
        startsNewInput = false
        hasError = false
    }

    private fun currentValue(): BigDecimal = display.toBigDecimalOrNull() ?: BigDecimal.ZERO

    private fun calculate(left: BigDecimal, right: BigDecimal, operator: Char): BigDecimal? {
        if (operator == '/' && right.compareTo(BigDecimal.ZERO) == 0) {
            display = "Cannot divide by zero"
            expression = ""
            storedValue = null
            pendingOperator = null
            startsNewInput = true
            hasError = true
            return null
        }

        return when (operator) {
            '+' -> left.add(right)
            '-' -> left.subtract(right)
            '×' -> left.multiply(right)
            '/' -> left.divide(right, DIVISION_SCALE, RoundingMode.HALF_UP)
            else -> error("Unsupported operator: $operator")
        }
    }

    private fun format(value: BigDecimal): String =
        value.stripTrailingZeros().toPlainString().let { if (it == "-0") "0" else it }

    private companion object {
        const val MAX_INPUT_DIGITS = 12
        const val DIVISION_SCALE = 10
        val OPERATORS = setOf('+', '-', '×', '/')
    }
}
