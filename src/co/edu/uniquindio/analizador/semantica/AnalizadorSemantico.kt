package co.edu.uniquindio.analizador.semantica

import co.edu.uniquindio.analizador.sintaxis.UnidadDeCompilacion
import co.edu.uniquindio.analizador.sintaxis.Error

class AnalizadorSemantico(var unidadDeCompilacion: UnidadDeCompilacion) {

    var listaErrores: ArrayList<Error> = ArrayList()
    var tablaSimbolos: TablaSimbolos = TablaSimbolos(listaErrores)

    fun llenarTablaSimbolo(){
        unidadDeCompilacion.llenarTablaSimbolo(tablaSimbolos,listaErrores)
    }
    fun analizarSemantica(){
        unidadDeCompilacion.analizarSemantica(tablaSimbolos,listaErrores)
    }

}