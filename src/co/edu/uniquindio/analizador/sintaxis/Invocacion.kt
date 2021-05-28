package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token
import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Invocacion(var nombreFuncion: Token, var argumentos: ArrayList<Argumento>?) : Sentencia() {

    override fun toString(): String {
        return "Invocacion(nombreFuncion=$nombreFuncion, argumentos=$argumentos)"
    }

    override fun getArboVisual(): TreeItem<String> {
        var raiz = TreeItem("invocacion")
        raiz.children.add(TreeItem("nombreFuncion: ${nombreFuncion.lexema}"))
        return raiz

    }

    fun obtenerTipoArgumentos(tablaSimbolos: TablaSimbolos, ambito: String): ArrayList<String> {
        var listaArg = ArrayList<String>()
        for (a in argumentos!!) {
            listaArg.add(a.expresionA.obtenerTipo(tablaSimbolos, ambito, ArrayList()))
        }
        return listaArg
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var listaTiposArgs= obtenerTipoArgumentos(tablaSimbolos, ambito)
        var s = tablaSimbolos.buscarSimboloFuncion(nombreFuncion.lexema,listaTiposArgs)

        if (s== null){
            listaErrores.add(Error("la funcion ${nombreFuncion.lexema} no existe ",nombreFuncion.fila,nombreFuncion.columna))
        }
    }
}