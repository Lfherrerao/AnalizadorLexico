package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Categoria
import co.edu.uniquindio.analizador.lexico.Token
import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import kotlin.math.E

class ExpresionAritmetica() : Expresion() {

    var expresionAritmetica2: ExpresionAritmetica? = null
    var operador: Token? = null
    var expresionAritmetica1: ExpresionAritmetica? = null
    var valorNumerico: ValorNumerico? = null


    override fun getArboVisual(): TreeItem<String> {
        val raiz = TreeItem("expresion Aritmetia")

        if (expresionAritmetica1 != null && operador != null && expresionAritmetica2 != null) {
            raiz.children.add(expresionAritmetica1!!.getArboVisual())
            raiz.children.add(TreeItem(operador!!.lexema))
            raiz.children.add(expresionAritmetica2!!.getArboVisual())
        } else if (expresionAritmetica1 != null && operador != null && expresionAritmetica2 != null && valorNumerico != null) {
            raiz.children.add(expresionAritmetica1!!.getArboVisual())
        } else if (valorNumerico != null && operador != null && expresionAritmetica1 != null) {
            raiz.children.add(valorNumerico!!.getArboVisual())
            raiz.children.add(TreeItem(operador!!.lexema))
            raiz.children.add(expresionAritmetica2!!.getArboVisual())
        } else {
            raiz.children.add(valorNumerico!!.getArboVisual())
        }
        return raiz
    }

    constructor(
        expresionAritmetica1: ExpresionAritmetica?,
        operador: Token?,
        expresionAritmetica2: ExpresionAritmetica?
    ) : this() {

        this.expresionAritmetica1 = expresionAritmetica1
        this.operador = operador
        this.expresionAritmetica2 = expresionAritmetica2
    }

    constructor(expresionAritmetica1: ExpresionAritmetica?) : this() {
        this.expresionAritmetica1 = expresionAritmetica1
    }

    constructor(valorNumerico: ValorNumerico?, operador: Token?, expresionAritmetica2: ExpresionAritmetica?) : this() {
        this.valorNumerico = valorNumerico
        this.operador = operador
        this.expresionAritmetica2 = expresionAritmetica2
    }

    constructor(valorNumerico: ValorNumerico?) : this() {
        this.valorNumerico = valorNumerico
    }

    constructor(valorNumericoA: Token?, operador: Token?, expresionAritmetica2A: Token?) : this() {
        this.valorNumerico = valorNumerico
        this.operador = operador
        this.expresionAritmetica2 = expresionAritmetica2
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String, listaErrores: ArrayList<Error>): String {
        if (expresionAritmetica1 != null && expresionAritmetica2 != null) {

            var tipo1 = expresionAritmetica1!!.obtenerTipo(tablaSimbolos, ambito, listaErrores)
            var tipo2 = expresionAritmetica2!!.obtenerTipo(tablaSimbolos, ambito, listaErrores)
            if (tipo1 == "_double" || tipo2 == "_double") {
                return "_decimal"
            } else {
                return "_int"
            }

        } else if (expresionAritmetica1 != null) {
            return expresionAritmetica1!!.obtenerTipo(tablaSimbolos, ambito, listaErrores)

        } else if (valorNumerico != null && expresionAritmetica1 != null) {

            var tipo1 = obtenerTipoCampo(valorNumerico, ambito, tablaSimbolos, listaErrores)


            var tipo2 = expresionAritmetica1!!.obtenerTipo(tablaSimbolos, ambito, listaErrores)
            if (tipo1 == "_double" || tipo2 == "_double") {
                return "_decimal"
            } else {
                return "_int"
            }


        } else if (valorNumerico != null) {
            return obtenerTipoCampo(valorNumerico, ambito, tablaSimbolos, listaErrores)

        }
        return ""
    }

    fun obtenerTipoCampo(
        valorNumerico: ValorNumerico?,
        ambito: String,
        tablaSimbolos: TablaSimbolos,
        listaErrores: ArrayList<Error>
    ): String {

        println("entra a la clase Exprsion aritmetica obtenerTipoCampo")
        if (valorNumerico!!.termino.categoria == Categoria.ENTERO) {
            return "_int"
        } else if (valorNumerico!!.termino.categoria == Categoria.DECIMAL) {
            return "_double"
        } else {
            var simbolo = tablaSimbolos.buscarSimboloValor(valorNumerico!!.termino.lexema, ambito)
            if (simbolo != null) {
                return simbolo.tipo!!
            } else {
                listaErrores.add(
                    Error(
                        "el campo ${valorNumerico!!.termino.lexema}  no exixte dentro del ambito $ambito",
                        valorNumerico!!.termino.fila,
                        valorNumerico!!.termino.columna
                    )
                )
            }
        }
        return ""
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if (valorNumerico != null) {
            if (valorNumerico!!.termino.categoria == Categoria.IDENTIFICADOR) {

                var simbolo = tablaSimbolos.buscarSimboloValor(valorNumerico!!.termino.lexema, ambito)

                println(simbolo)

                if (simbolo == null) {
                    listaErrores.add(
                        Error(
                            "el campo ${valorNumerico!!.termino.lexema}  no exixte en el ambiro $ambito ",
                            valorNumerico!!.termino.fila,
                            valorNumerico!!.termino.columna
                        )
                    )
                }
            }
        }
        if (expresionAritmetica1 != null) {
            expresionAritmetica1!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
        if (expresionAritmetica2 != null) {
            expresionAritmetica2!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
    }
}