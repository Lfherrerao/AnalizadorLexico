package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

open class Sentencia() {

    open fun getArboVisual(): TreeItem<String> {
        return TreeItem<String>("lista sentencias")
    }

    open fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
    }

    open fun analizarSemantica (tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
    }
}