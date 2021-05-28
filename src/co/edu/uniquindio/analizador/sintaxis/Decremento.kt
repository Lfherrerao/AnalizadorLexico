package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token
import javafx.scene.control.TreeItem

class Decremento (var nombre: Token, var operador: Token): Sentencia() {

    override fun toString(): String {
        return "Decremento(nombre=$nombre, operador=$operador)"
    }

    override fun getArboVisual(): TreeItem<String> {
        var raiz = TreeItem("decremento")
        return TreeItem<String>("decremento: \n identificador: ${nombre.lexema}  \n oprador:${operador.lexema} ")

    }
}