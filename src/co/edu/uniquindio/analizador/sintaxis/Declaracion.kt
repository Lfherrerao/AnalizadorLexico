package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token
import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Declaracion(var tipoDato: Token, var nombre: Token) : Sentencia() {

    override fun toString(): String {
        return "Declaracion(tipoDato=$tipoDato, nombre=$nombre)"
    }


    override fun getArboVisual(): TreeItem<String> {
        var raiz = TreeItem("Declaracion ")
        raiz.children.add(TreeItem("Tipo de dato:${tipoDato.lexema}"))
        raiz.children.add(TreeItem("Nombre Variable:${nombre.lexema}"))

        return raiz
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        tablaSimbolos.guardarSimboloValor(nombre.lexema, tipoDato.lexema, true, ambito, nombre.fila, nombre.columna)

    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var s= tablaSimbolos.buscarSimboloValor(nombre.lexema, ambito)
        if (s != null && s.tiposParametros == null){
            listaErrores.add(Error("el campo ${nombre.lexema}  ya exixte en el ambito   $ambito ", nombre.fila, nombre.columna))
        }
    }
}
