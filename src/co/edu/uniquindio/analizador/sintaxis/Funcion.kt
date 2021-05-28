package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token
import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Funcion(
    var nombreFuncion: Token,
    var tipoRetorno: Token,
    var listaParametros: ArrayList<Parametro>,
    var listaSentencias: ArrayList<Sentencia>
) {

    override fun toString(): String {
        return "Funcion(nombreFuncion=$nombreFuncion, tipoRetorno=$tipoRetorno, listaParametros=$listaParametros, listaSentencias=$listaSentencias)"
    }

    fun getArboVisual(): TreeItem<String> {
        var raiz = TreeItem<String>("funcion")
        raiz.children.add(TreeItem("nombre:${nombreFuncion.lexema}"))
        raiz.children.add(TreeItem("Tipo Retorno:${tipoRetorno.lexema}"))

        var raizParametros = TreeItem<String>("parametro")

        for (p in listaParametros) {
            raizParametros.children.add(p.getArboVisual())
        }
        raiz.children.add(raizParametros)


        var raizSentencias = TreeItem("Sentencias")

        for (p in listaSentencias) {
            raizSentencias.children.add(p.getArboVisual())
        }
        raiz.children.add(raizSentencias)

        return raiz

    }

    fun obtenerTiposParametros(): ArrayList<String> {


        var lista = ArrayList<String>()
        for (p in listaParametros) {
            lista.add(p.tipoDato!!.lexema)
        }
        return lista
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        tablaSimbolos.guardarSimboloFuncion(nombreFuncion.lexema, tipoRetorno.lexema, obtenerTiposParametros(), ambito, nombreFuncion.fila, nombreFuncion.columna)

        for (p in listaParametros) {
            tablaSimbolos.guardarSimboloValor(p.nombre!!.lexema, p.tipoDato!!.lexema, true,nombreFuncion.lexema, p.nombre!!.fila,p.nombre!!.columna)
        }
        for (s in listaSentencias) {
            s.llenarTablaSimbolos(tablaSimbolos, listaErrores, nombreFuncion.lexema)
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>){

        for (f in listaSentencias){
            f.analizarSemantica(tablaSimbolos, listaErrores, nombreFuncion.lexema)
        }
    }
}