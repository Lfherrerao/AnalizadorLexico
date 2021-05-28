package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token
import javafx.scene.control.TreeItem

class ValorNumerico (var signo: Token?, var termino: Token ) {

    override fun toString(): String {
        return "ValorNumerico(signo=$signo, termino=$termino)"
    }

     fun getArboVisual(): TreeItem<String> {
        return TreeItem("valor numerico")
    }
}