package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token
import javafx.scene.control.TreeItem

class Parametro(var tipoDato:Token?,var nombre:Token?) {
    override fun toString(): String {
        return "Parametro(tipoDato=$tipoDato, nombre=$nombre)"
    }


    fun getArboVisual(): TreeItem<String> {
        return TreeItem<String>("Tipo de dato= ${tipoDato?.lexema} \n Nombre Parametro=${nombre?.lexema} ")
    }
}