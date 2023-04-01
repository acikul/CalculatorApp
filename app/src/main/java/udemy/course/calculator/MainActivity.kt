package udemy.course.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var inputDisplay: TextView? = null

    private var lastNumeric = false
    private var decimalPointExists = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputDisplay = findViewById(R.id.input_display)
    }

    fun onDigit(view: View) {
        inputDisplay?.append((view as Button).text)
        lastNumeric = true
    }

    fun onClear(view: View) {
        inputDisplay?.text = ""
        lastNumeric = false
        decimalPointExists = false
    }

    fun onDecimalPoint(view: View) {
        if (lastNumeric && !decimalPointExists) {
            inputDisplay?.append(".")
            lastNumeric = false
            decimalPointExists = true
        }
    }

    fun onOperator(view: View) {
        if (inputDisplay?.text.isNullOrEmpty() && (view as Button).text.equals("-")) {
            inputDisplay?.append(view.text)
        } else if (lastNumeric && !operatorExists(inputDisplay?.text.toString())) {
            inputDisplay?.append((view as Button).text)
            lastNumeric = false
            decimalPointExists = false
        }
    }

    fun onEqual(view: View) {
        val inputDisplayText = inputDisplay?.text

        if (lastNumeric && operatorExists(inputDisplayText.toString()) && !inputDisplayText.toString().contains("E")) {

            var equation = inputDisplayText.toString()

            val firstIsNegative = equation.startsWith("-")
            equation = equation.removePrefix("-")

            try {
                val splitEquation = equation.split("/", "*", "+", "-")

                val first = ((if (firstIsNegative) "-" else "") + splitEquation[0]).toDouble()
                val second = splitEquation[1].toDouble()

                val result = when {
                    equation.contains("/") -> first / second
                    equation.contains("*") -> first * second
                    equation.contains("+") -> first + second
                    equation.contains("-") -> first - second
                    else -> 0
                }
                if (result.toString().contains("E")) {
                    Toast.makeText(this, "number too big/small to continue calculations - clear to continue", Toast.LENGTH_SHORT).show()
                }
                inputDisplay?.text = removeTrailingZeros(result.toString())

            } catch (e: ArithmeticException) {
                e.printStackTrace()
                Toast.makeText(this, "What have you done :S", Toast.LENGTH_SHORT).show()
                onClear(view)
            }
        }
    }

    private fun operatorExists(value: String) : Boolean {
        return value.removePrefix("-").let {
                it.contains("/") ||
                it.contains("*") ||
                it.contains("+") ||
                it.contains("-")
        }
    }

    private fun removeTrailingZeros(value: String): String{
        val splitValue = value.split(".")
        return when {
            splitValue[1] == ("0") -> splitValue[0]
            else -> "${splitValue[0]}.${splitValue[1].substring(0, 4)}"
        }
    }
}