package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token
import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import com.sun.source.tree.Tree
import javafx.scene.control.TreeItem

class ExpresionRelacional(var termino1: Token, var operador: Token, var termino2: Token?) : Expresion() {
    override fun toString(): String {
        return "ExpresionRelacional(termino1=$termino1, operador=$operador, termino2=$termino2)"
    }

    fun getArbolVisual(): TreeItem<String> {

        return TreeItem<String>("expresion: (${termino1.lexema} ${operador.lexema} ${termino2!!.lexema}) ")
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String, listaErrores:ArrayList<Error>): String {
    return "_boolean"
    }
}