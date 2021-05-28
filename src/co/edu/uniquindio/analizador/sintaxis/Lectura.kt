package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token
import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Lectura(var nombre: Token) :Sentencia() {

    override fun toString(): String {
        return "Lectura(nombre=$nombre)"
    }

    override fun getArboVisual(): TreeItem<String> {
        var raiz =TreeItem("Lectura ")
        raiz.children.add(TreeItem("Nombre: ${nombre.lexema}"))

        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var s= tablaSimbolos.buscarSimboloValor(nombre.lexema, ambito)
        if (s == null){
            listaErrores.add(Error("el campo ${nombre.lexema}  no exixte en el ambiro $ambito ", nombre.fila, nombre.columna))
        }
    }
}