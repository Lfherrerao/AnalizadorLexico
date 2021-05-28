package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token
import co.edu.uniquindio.analizador.semantica.TablaSimbolos

class ExpresionAsignacion() : Sentencia() {

    var nombre: Token? = null
    var expresione: Expresion? = null
    var expresionToken: Token? = null



    constructor(nombre: Token, expresion: Token) : this() {
        this.nombre = nombre
        this.expresionToken = expresion
    }

    constructor(nombre: Token, expresion: Expresion) : this() {
        println("constructor con expresion")
        this.nombre = nombre
        this.expresione = expresion
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var s = tablaSimbolos.buscarSimboloValor(nombre!!.lexema, ambito)
        if (s == null) {
            listaErrores.add(
                Error(
                    "el campo ${nombre!!.lexema} no existe dentro del ambito$ambito,",
                    nombre!!.fila,
                    nombre!!.columna
                )
            )
        } else if ( expresionToken != null) {
            var tipo = s.tipo
                if ("_boolean" != tipo) {
                    listaErrores.add(
                        Error(
                            "el tipo de dato de la expresion ${nombre!!.lexema} no coincide con el tipo de dato del campo ${expresionToken!!.lexema} ",
                            nombre!!.fila,
                            nombre!!.columna
                        )
                    )

                }

        }else {
            var tipo = s.tipo
            if (expresione != null) {

                expresione!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
                var tipoExp = expresione!!.obtenerTipo(tablaSimbolos, ambito, listaErrores)
                if (tipoExp != tipo) {
                    listaErrores.add(
                        Error(
                            "el tipo de daato de la expresion $tipo no coincide con el tipo de dato del campo ${nombre!!.lexema} ",
                            nombre!!.fila,
                            nombre!!.columna
                        )
                    )

                }
            }
        }
    }
}


