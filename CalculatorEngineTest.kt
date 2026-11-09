package com.syntechhub.calculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatorEngineTest {
    @Test
    fun addsAndChainsOperations() {
        val calculator = CalculatorEngine()
        calculator.inputDigit('8')
        calculator.chooseOperator('+')
        calculator.inputDigit('2')
        calculator.chooseOperator('×')
        calculator.inputDigit('3')
        calculator.calculateResult()

        assertEquals("30", calculator.display)
    }

    @Test
    fun dividesAndFormatsDecimalResults() {
        val calculator = CalculatorEngine()
        calculator.inputDigit('1')
        calculator.chooseOperator('/')
        calculator.inputDigit('4')
        calculator.calculateResult()

        assertEquals("0.25", calculator.display)
    }

    @Test
    fun divisionByZeroShowsAnErrorAndCanBeClearedByTyping() {
        val calculator = CalculatorEngine()
        calculator.inputDigit('9')
        calculator.chooseOperator('/')
        calculator.inputDigit('0')
        calculator.calculateResult()

        assertTrue(calculator.hasError)
        assertEquals("Cannot divide by zero", calculator.display)

        calculator.inputDigit('6')
        assertEquals("6", calculator.display)
        assertTrue(!calculator.hasError)
    }

    @Test
    fun decimalAndDeleteHandleEmptyAndRepeatedInput() {
        val calculator = CalculatorEngine()
        calculator.inputDecimal()
        calculator.inputDecimal()
        calculator.inputDigit('5')

        assertEquals("0.5", calculator.display)

        calculator.deleteLast()
        assertEquals("0.", calculator.display)
        calculator.deleteLast()
        assertEquals("0", calculator.display)
    }

    @Test
    fun limitsInputLengthAndSupportsNegativeOperands() {
        val calculator = CalculatorEngine()
        repeat(13) { calculator.inputDigit('7') }
        assertEquals("777777777777", calculator.display)

        calculator.clear()
        calculator.chooseOperator('+')
        calculator.toggleSign()
        calculator.inputDigit('5')
        calculator.calculateResult()

        assertEquals("-5", calculator.display)
    }
}
