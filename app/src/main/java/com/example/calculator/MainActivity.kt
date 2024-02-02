package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.data.text = binding.result.text.toString().drop(1)
    }
    fun onDigitClick(view: View) {
        if (stateError){
            binding.data.text = (view as Button).text
            stateError = false
        }else{
            binding.data.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }
    fun onAllClearClick(view: View) {
        binding.data.text = ""
        binding.result.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.result.visibility = View.GONE
    }
    fun onOperatorClick(view: View) {
        if (!stateError && lastNumeric){
            binding.data.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }
    fun onClearClick(view: View) {
        binding.data.text = ""
        lastNumeric = false
    }
    fun onBackClick(view: View) {
        binding.data.text = binding.data.text.toString().dropLast(1)

        try {
            val lastChar = binding.data.text.toString().last()
            if (lastChar.isDigit()){
                onEqual()
            }
        }catch (e: Exception){
            binding.result.text = ""
            binding.result.visibility = View.GONE
            Log.e("Last char error",e.toString())
        }
    }

    fun onEqual(){
        if (lastNumeric && !stateError){
            val txt = binding.data.text.toString()
            expression = ExpressionBuilder(txt).build()

            try{
                val result = expression.evaluate()
                binding.result.visibility = View.VISIBLE
                binding.result.text = "="+result.toString()
            }catch (ex: ArithmeticException){
                Log.e("evaulate error",ex.toString())
                binding.result.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}