package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token
import javafx.scene.control.TreeItem

class Incremento(var nombre:Token, var operador:Token): Sentencia() {
    override fun toString(): String {
        return "Incremento(nombre=$nombre, operador=$operador)"
    }

    override fun getArboVisual(): TreeItem<String> {
        var raiz = TreeItem("incremento")
        return TreeItem<String>("incremento: \n identificador:${nombre.lexema}  \n oprador:${operador.lexema} ")

    }
}