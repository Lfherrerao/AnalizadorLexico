package co.edu.uniquindio.analizador.Pruebas

import co.edu.uniquindio.analizador.lexico.AnalizadorLexico
import co.edu.uniquindio.analizador.sintaxis.AnalizadorSintactico


fun main(){
    val lexico= AnalizadorLexico( "  ")
   val sintactico= AnalizadorSintactico(lexico.listaTokens)
    val evaluacion = sintactico.esExpresionCadena()

    if (evaluacion != null){
      println("si funciona ")
    }
    else{
        println("no funciona ")
    }


    val sintaxis= AnalizadorSintactico(lexico.listaTokens)
   println( sintaxis.esUnidadDeCompilacion())
    print(sintaxis.listaDeErrores)

}