package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Decision(
    var exprecionR: ExpresionRelacional,
    var sentenciasSi: ArrayList<Sentencia>?,
    var sentenciasNo: ArrayList<Sentencia>?
) : Sentencia() {
    override fun toString(): String {
        return "Decision(exprecionR=$exprecionR, sentenciasSi=$sentenciasSi, sentenciasNo=$sentenciasNo)"
    }

    override
    fun getArboVisual(): TreeItem<String> {
        var raiz = TreeItem("Decision ")

        var condicion = TreeItem("condicion")
        condicion.children.add(exprecionR.getArbolVisual())
        raiz.children.add(condicion)


        var raizTrue = TreeItem("Sentencias si")
        for (s in sentenciasSi!!) {
            raizTrue.children.add(s.getArboVisual())
        }
        raiz.children.add(raizTrue)

        if (sentenciasNo != null) {
            var raizFalse = TreeItem("Sentencias falsas")
            for (s in sentenciasNo!!) {
                raizFalse.children.add(s.getArboVisual())
            }
            raiz.children.add(raizFalse)
        }

        return raiz
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        for (s in sentenciasSi!!) {
            s.llenarTablaSimbolos(tablaSimbolos, listaErrores, ambito)

        }

        if (sentenciasNo != null) {
            for (s in sentenciasNo!!) {
                s.llenarTablaSimbolos(tablaSimbolos, listaErrores, ambito)
            }
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if (exprecionR != null ) {
            exprecionR.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        for (s in sentenciasSi!!){
            s.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
            if (sentenciasNo != null){
                for (s in sentenciasNo!!){
                    s.analizarSemantica(tablaSimbolos, listaErrores, ambito)
                }
            }
        }
    }
}