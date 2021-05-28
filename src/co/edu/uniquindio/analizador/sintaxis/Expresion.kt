package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

open class Expresion {

   open  fun getArboVisual(): TreeItem<String> {
        return TreeItem("expresion")
    }

    open fun obtenerTipo(tablaSimbolos: TablaSimbolos,ambito:String, listaErrores: ArrayList<Error>):String {
        return ""
    }
    open fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {}
}