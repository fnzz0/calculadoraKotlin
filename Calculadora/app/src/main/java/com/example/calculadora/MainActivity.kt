package com.example.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var txtResultado: TextView? = null
    private val calculadora = calculadora()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtResultado = findViewById(R.id.txtResultado)
        txtResultado?.text = "0"
    }

    fun calcular(view: View) {
        val botao = view as Button
        val textButton = botao.text.toString()

        val resultado = calculadora.calcular(textButton, txtResultado?.text.toString())
        txtResultado?.text = resultado
    }
}
