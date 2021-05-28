package co.edu.uniquindio.analizador.sintaxis

import javafx.scene.control.TreeItem

class Impresion(var expresion:ExpresionCadena) :Sentencia() {

    override fun toString(): String {
        return "Impresion(expresion=$expresion)"
    }


    override fun getArboVisual(): TreeItem<String> {

        var  raiz=TreeItem("imprimir")
        raiz.children.add(expresion.getArboVisual())
        return raiz
    }


}