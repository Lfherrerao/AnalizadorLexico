package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Ciclo(var expresionR: ExpresionRelacional, var listaSentencias: ArrayList<Sentencia>?) : Sentencia() {
    override fun toString(): String {
        return "Ciclo(expresionR=$expresionR, listaSentencias=$listaSentencias)"
    }

    override fun getArboVisual(): TreeItem<String> {

        var raiz = TreeItem<String>("ciclo")
        raiz.children.add(expresionR.getArbolVisual())


        if (listaSentencias != null){
            var raizSentencia = TreeItem<String>("sentencias")
            for (p in listaSentencias!!){
                raizSentencia.children.add(p.getArboVisual())
            }

            raiz.children.add(raizSentencia)
        }

        return raiz
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        for (s in listaSentencias!!) {
            s.llenarTablaSimbolos(tablaSimbolos,  listaErrores, ambito)
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        expresionR.analizarSemantica(tablaSimbolos, listaErrores, ambito)

        for (s in listaSentencias!!){
            s.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
    }
}
