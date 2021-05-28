package co.edu.uniquindio.analizador.sintaxis


import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import java.util.*
import co.edu.uniquindio.analizador.sintaxis.Error


class UnidadDeCompilacion(var listaDeFunciones: ArrayList<Funcion>) {

    override fun toString(): String {
        return "UnidadDeCompilacion(listaDeFunciones=$listaDeFunciones)"
    }

    /**
     * Devuelve el arbor visual de la unidad de compilacion
     *
     * @return
     */
    fun getArboVisual(): TreeItem<String> {
        var raiz = TreeItem<String>("unidad de compilacion")
        for (f in listaDeFunciones) {
            raiz.children.add(f.getArboVisual())

        }
        return raiz
    }

    fun llenarTablaSimbolo(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>) {

        for (f in listaDeFunciones) {
            f.llenarTablaSimbolos(tablaSimbolos, listaErrores, "unidadCompilacion")
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>) {
        for (f in listaDeFunciones){
            f.analizarSemantica(tablaSimbolos, listaErrores)
        }
    }

}


