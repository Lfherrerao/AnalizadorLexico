package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Categoria
import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Retorno(var expresion: Expresion) : Sentencia() {

    override fun toString(): String {
        return "Retorno(expresion=$expresion)"
    }

    override fun getArboVisual(): TreeItem<String> {
        var raiz =TreeItem("Sentencia de retorno")

        raiz.children.add(expresion.getArboVisual())

        return raiz
    }
    
}