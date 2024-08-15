package com.example.calculadora

class calculadora {

    private var resultadoCalculado = false

    fun calcular(textButton: String, textoAtual: String): String {
        return when {
            textButton == "=" -> calcularResultado(textoAtual)
            textButton == "C" -> "0"
            else -> {
                if (resultadoCalculado) {
                    if (textButton in listOf("+", "-", "×", "÷")) {
                        resultadoCalculado = false
                        textoAtual + textButton
                    } else {
                        resultadoCalculado = false
                        textButton
                    }
                } else {
                    val concatenar = textoAtual + textButton
                    removeZerosEsquerda(concatenar)
                }
            }
        }
    }

    private fun calcularResultado(expressao: String): String {
        return try {
            var expressao = expressao.replace(',', '.')

            val resultado = eval(expressao)

            resultadoCalculado = true
            resultado.toString().replace('.', ',')
        } catch (e: Exception) {
            resultadoCalculado = false
            "Erro"
        }
    }

    private fun removeZerosEsquerda(str: String): String {
        return str.trimStart { it == '0' }.ifEmpty { "0" }
    }

    private fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < str.length) str[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                return if (ch == charToEat) {
                    nextChar()
                    true
                } else {
                    false
                }
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Unexpected: ${ch.toChar()}")
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    x = when {
                        eat('+'.code) -> x + parseTerm()
                        eat('-'.code) -> x - parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    x = when {
                        eat('×'.code) -> x * parseFactor()
                        eat('÷'.code) -> x / parseFactor()
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()

                var x: Double
                val startPos = pos

                when {
                    eat('('.code) -> {
                        x = parseExpression()
                        eat(')'.code)
                    }
                    ch in '0'.code..'9'.code || ch == '.'.code -> {
                        while (ch in '0'.code..'9'.code || ch == '.'.code) nextChar()
                        x = str.substring(startPos, pos).toDouble()
                    }
                    ch in 'a'.code..'z'.code -> {
                        while (ch in 'a'.code..'z'.code) nextChar()
                        val func = str.substring(startPos, pos)
                        x = parseFactor()
                        x = when (func) {
                            "sqrt" -> Math.sqrt(x)
                            "sin" -> Math.sin(Math.toRadians(x))
                            "cos" -> Math.cos(Math.toRadians(x))
                            "tan" -> Math.tan(Math.toRadians(x))
                            else -> throw RuntimeException("Unknown function: $func")
                        }
                    }
                    else -> throw RuntimeException("Unexpected: ${ch.toChar()}")
                }

                if (eat('^'.code)) x = Math.pow(x, parseFactor())
                return x
            }
        }.parse()
    }
}
