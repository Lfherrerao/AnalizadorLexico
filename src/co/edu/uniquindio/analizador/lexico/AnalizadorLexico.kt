package co.edu.uniquindio.analizador.lexico

import co.edu.uniquindio.analizador.sintaxis.Error
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author leonardo fabio herrera o.
 * @author john Alexander Martinez
 *
 * Analizador Lexico, donde se encuentran todos los automatas programados.
 */
class AnalizadorLexico(var codigoFuente: String) {


    // variables globales que se usaran en los automatas.
    var posicionActual = 0
    var caracterActual = codigoFuente[0]
    var listaTokens = ArrayList<Token>()
    var listaErrores = ArrayList<Error>()
    var finCodigo = 0.toChar()
    var filaActual = 0
    var columnaActual = 0
    var posicionInicial = posicionActual
    var palabrasReservadas = ArrayList<String>()


    /**
     * funcion con la que analizaremos los automatas
     */
    fun analizar() {
        while (caracterActual != finCodigo) {

            inicializarPalabraReservada()


            if (caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n') {
                obtenerSiguienteCaracter()
                continue
            }
            if (esEntero()) continue
            if (esComa()) continue
            if (esLlaves()) continue
            if (esCorchete()) continue
            if (esParentesis()) continue
            if (esOperadorRelacional()) continue
            if (esDecimal()) continue
            if (esIdentificador()) continue
            if (esFinSentencia()) continue
            if (esOperadorLogico()) continue
            if (esPunto()) continue
            if (esDosPuntos()) continue
            if (esOperadorDecremento()) continue
            if (esOperadorIncremento()) continue
            if (esOperadorAsignacion()) continue
            if (esOperadorArtimetico()) continue
            if (esCadena()) continue
            if (esComentarioBloque()) continue
            if (esComentarioLinea()) continue



            almacenarToken("" + caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual)
            obtenerSiguienteCaracter()
        }
    }

    fun inicializarPalabraReservada() {
        palabrasReservadas.add("_function")
        palabrasReservadas.add("_abstract")
        palabrasReservadas.add("_continue")
        palabrasReservadas.add("_for")
        palabrasReservadas.add("_new")
        palabrasReservadas.add("_switch")
        palabrasReservadas.add("_default")
        palabrasReservadas.add("_boolean")
        palabrasReservadas.add("_do")
        palabrasReservadas.add("_if")
        palabrasReservadas.add("_private")
        palabrasReservadas.add("_this")
        palabrasReservadas.add("_break")
        palabrasReservadas.add("_double")
        palabrasReservadas.add("_implements")
        palabrasReservadas.add("_protected")
        palabrasReservadas.add("_thow")
        palabrasReservadas.add("_byte")
        palabrasReservadas.add("_else")
        palabrasReservadas.add("_import")
        palabrasReservadas.add("_public")
        palabrasReservadas.add("_throws")
        palabrasReservadas.add("_case")
        palabrasReservadas.add("_enum")
        palabrasReservadas.add("_return")
        palabrasReservadas.add("_catch")
        palabrasReservadas.add("_extends")
        palabrasReservadas.add("_int")
        palabrasReservadas.add("_short")
        palabrasReservadas.add("_try")
        palabrasReservadas.add("_char")
        palabrasReservadas.add("_final")
        palabrasReservadas.add("_print")
        palabrasReservadas.add("_interface")
        palabrasReservadas.add("_static")
        palabrasReservadas.add("_void")
        palabrasReservadas.add("_string")
        palabrasReservadas.add("_class")
        palabrasReservadas.add("_long")
        palabrasReservadas.add("_float")
        palabrasReservadas.add("_while")
        palabrasReservadas.add("_true")
        palabrasReservadas.add("_false")
        palabrasReservadas.add("_make")
        palabrasReservadas.add("_case")
        palabrasReservadas.add("_call")
        palabrasReservadas.add("_var")
        palabrasReservadas.add("_read")
    }

    /**
     * Bactraking para cuando una funcion utiliza los caracteres que se usan en otro automata
     */
    fun hacerBT(pocicionInicial: Int, filaInicial: Int, columnaInicial: Int) {
        posicionActual = posicionInicial
        filaActual = filaInicial
        columnaActual = columnaInicial
        caracterActual = codigoFuente[posicionActual]
    }

    /**
     * funcion encargada de almacenar los tokens encontrados en un array
     */
    fun almacenarToken(lexema: String, categoria: Categoria, fila: Int, columna: Int) =
        listaTokens.add(Token(lexema, categoria, fila, columna))

    /**
     * funcion que reprecenta el automata para los numeros enteros.
     */
    fun esEntero(): Boolean {
        if (caracterActual.isDigit() || caracterActual == '.') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual

            if (caracterActual == '.') {
                return false
            }


            if (caracterActual.isDigit()) {

                obtenerSiguienteCaracter()


                while (caracterActual.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()

                    if (caracterActual == '.') {
                        hacerBT(posicionInicial, filaInicial, columnaInicial)
                        return false
                    }

                }
                almacenarToken(lexema, Categoria.ENTERO, filaInicial, columnaInicial)
                return true

            }

        }
        return false
    }

    /**
     * funcion que que reprecenta el automata  de los  numeros decimales.
     */
    fun esDecimal(): Boolean {

        if (caracterActual == '.' || caracterActual.isDigit()) {
            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual


            if (caracterActual == '.') {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                if (!(caracterActual.isDigit())) {
                    hacerBT(posicionInicial, filaInicial, columnaInicial)
                    return false

                } else
                    lexema += caracterActual
                obtenerSiguienteCaracter()
            } else {
                lexema += caracterActual;
                obtenerSiguienteCaracter()

                while (caracterActual.isDigit()) {
                    lexema += caracterActual;
                    obtenerSiguienteCaracter()
                }
                if (caracterActual != '.') {
                    hacerBT(posicionInicial, filaInicial, columnaInicial)
                    return false
                } else {
                    lexema += caracterActual;
                    obtenerSiguienteCaracter()
                }
            }

            while (Character.isDigit(caracterActual)) {
                lexema += caracterActual;
                obtenerSiguienteCaracter()
            }

            almacenarToken(lexema, Categoria.DECIMAL, filaInicial, columnaInicial)
            return true
        }
        return false
    }

    /**
     * funcion que que reprecenta el automata  para las comas.
     *
     * @return true si es coma, false en caso contrario
     */
    fun esComa(): Boolean {
        if (caracterActual == ',') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            almacenarToken(lexema, Categoria.ES_COMA, filaInicial, columnaInicial)
            return true
        }
        // Rechazo inmediato
        return false
    }

    /**
     * funcion que que reprecenta el automata  para las llaves tanto izquierda como derecha.
     */
    fun esLlaves(): Boolean {
        if (caracterActual == '{' || caracterActual == '}') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual

            if (caracterActual == '{') {

                almacenarToken(lexema, Categoria.LLAVE_IZQUIERDA, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            } else {
                almacenarToken(lexema, Categoria.LLAVE_DERECHA, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            return false
        }
        return false
    }

    /**
     * funcion que que reprecenta el automata para los corchetes derecho e izquierdo
     */
    fun esCorchete(): Boolean {
        if (caracterActual == '[' || caracterActual == ']') {
            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual

            if (caracterActual == '[') {

                almacenarToken(lexema, Categoria.CORCHETE_IZQUIERDO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            } else {
                almacenarToken(lexema, Categoria.CORCHETE_DEREHO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
        }
        return false
    }

    /**
     *    Metodo que indica si el lexema es un parentesis derecho u izquierdo
     */
    fun esParentesis(): Boolean {
        if (caracterActual == '(' || caracterActual == ')') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual

            if (caracterActual == '(') {

                almacenarToken(lexema, Categoria.PARENTESIS_IZQUIERDO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            } else {
                almacenarToken(lexema, Categoria.PARENTESIS_DEREHO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
        }
        return false
    }

    /**
     *funcion que que reprecenta el automata  que define los operadores relacionales
     */
    fun esOperadorRelacional(): Boolean {
        if (caracterActual == '=' || caracterActual == '>' || caracterActual == '<' || caracterActual == '¡') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual

            if (caracterActual == '=') {
                obtenerSiguienteCaracter()
                if (caracterActual == '=') {
                    lexema += caracterActual
                    almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
                    obtenerSiguienteCaracter()
                    return true

                } else hacerBT(posicionInicial, filaInicial, columnaInicial)
                return false

            }

            if (caracterActual == '>') {
                obtenerSiguienteCaracter()
                if (caracterActual == '>') {
                    lexema += caracterActual;
                    obtenerSiguienteCaracter()
                    if (caracterActual == '=') {
                        lexema += caracterActual

                        almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
                        obtenerSiguienteCaracter()
                        return true
                    }
                    return false
                }
                return false
            }
            if (caracterActual == '<') {
                obtenerSiguienteCaracter()
                if (caracterActual == '<') {
                    lexema += caracterActual;
                    obtenerSiguienteCaracter()
                    if (caracterActual == '=') {
                        lexema += caracterActual

                        almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
                        obtenerSiguienteCaracter()
                        return true
                    }
                    return false
                }
            }
            if (caracterActual == '¡') {
                obtenerSiguienteCaracter()
                if (caracterActual == '=') {
                    lexema += caracterActual
                    almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
                    obtenerSiguienteCaracter()
                    return true
                }
            }
            return false
        }
        return false
    }

    /**
     *funcion que que reprecenta el automata  que define los identificadores
     */
    fun esIdentificador(): Boolean {
        if (caracterActual == '_') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (Character.isDigit(caracterActual) || Character.isLetter(caracterActual)) {

                while (caracterActual.isDigit() || caracterActual.isLetter()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
                if (esPalabraReservada(lexema)) {
                    almacenarToken(lexema, Categoria.PALABRA_RESERVADA, filaInicial, columnaInicial)
                    obtenerSiguienteCaracter()
                    return true

                }
                almacenarToken(lexema, Categoria.IDENTIFICADOR, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true


            }
            hacerBT(posicionInicial, filaInicial, columnaInicial)
            return false

        }
        return false
    }

    /**
     * Metodo que indica si el lexema es un fin de sentancia
     */
    fun esFinSentencia(): Boolean {
        if (caracterActual == '!') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual

            almacenarToken(lexema, Categoria.FIN_SENTENCIA, filaInicial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }

    /**
     *funcion que que reprecenta el automata  que define los operadores logicos
     */
    fun esOperadorLogico(): Boolean {
        if (caracterActual == '|' || caracterActual == '&') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual


            if (caracterActual == '|') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == '|') {
                    lexema += caracterActual

                    almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaInicial, columnaInicial)
                    obtenerSiguienteCaracter()
                    return true
                }
                return false
            }

            if (caracterActual == '&') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == '&') {
                    lexema += caracterActual
                    almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaInicial, columnaInicial)
                    obtenerSiguienteCaracter()
                    return true
                }
                return false
            }
        }

        return false
    }



    fun esOperadorIncremento():Boolean{
        if (caracterActual == '¡' ){

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual

            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if(caracterActual == '+'){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == '+'){
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()

                    almacenarToken(lexema, Categoria.INCREMENTO, filaInicial, columnaInicial)
                    return true
                }else{
                    hacerBT(posicionInicial, filaInicial, columnaInicial)
                    return false
                }
            }else{
                reportarError("error em lexico incremento")
                return false
            }

        }
        return false
    }
    fun esOperadorDecremento():Boolean{
        if (caracterActual == '!' ){

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual

            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if(caracterActual == '-'){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == '-'){
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()

                    almacenarToken(lexema, Categoria.DECREMENTO, filaInicial, columnaInicial)
                    return true
                }else{
                    hacerBT(posicionInicial, filaInicial, columnaInicial)
                    return false
                }
            }else{
                reportarError("error en lexico incremento")
                return false
            }

        }
        return false
    }


    /**
    /*
    * Metodo que verifica si el lexema es un operador de incremento
     */
    fun esOperadorIncremento(): Boolean {
        if (caracterActual == '+') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '+') {
                lexema += caracterActual
                almacenarToken(lexema, Categoria.INCREMENTO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true


            } else {
                hacerBT(posicionInicial, filaInicial, columnaInicial)
                return false
            }
            return false
        }
        return false
    }
    /*
    * Metodo que verifica si el lexema es un operador de decremento
     */
    fun esOperadorDecremento(): Boolean {
        if (caracterActual == '-') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual == '-') {
                lexema += caracterActual
                almacenarToken(lexema, Categoria.DECREMENTO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            hacerBT(posicionInicial, filaInicial, columnaInicial)
            return false
        }
        return false
    } */



    /*
    * Metodo que verifica si el lexema es un punto
     */
    fun esPunto(): Boolean {
        if (caracterActual == '.') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual.isDigit()) {
                hacerBT(posicionInicial, filaInicial, columnaInicial)
                return false
            }
            almacenarToken(lexema, Categoria.PUNTO, filaInicial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }

    /*
    * Metodo que verifica si el lexema es un dos puntos
     */
    fun esDosPuntos(): Boolean {
        if (caracterActual == ':') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual

            almacenarToken(lexema, Categoria.DOS_PUNTOS, filaInicial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }

    /**
     *Metodo que verifica si el caracter a analizar en un operador de asignacion
     */
    fun esOperadorAsignacion(): Boolean {

        if (caracterActual == '=') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual
            obtenerSiguienteCaracter()



            if (caracterActual == '+' || caracterActual == '*' || caracterActual == '-' || caracterActual == '/' || caracterActual == '%') {

                lexema += caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_ASIGNACION, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            hacerBT(posicionInicial, filaInicial, columnaInicial)
            return false
        }
        return false
    }

    /**
     *Metodo que verifica si el lexema que se analiza es un operador aritmetrico
     */
   fun esOperadorArtimetico():Boolean{
       if (caracterActual == '%' ){

           var lexema = ""
           var filaInicial = filaActual
           var columnaInicial = columnaActual
           var posicionInicial = posicionActual

           lexema+=caracterActual
           obtenerSiguienteCaracter()

           if(caracterActual == '+' || caracterActual == '-' || caracterActual == '*' || caracterActual == '/'){
               lexema+=caracterActual
               obtenerSiguienteCaracter()
               almacenarToken(lexema, Categoria.OPERADOR_ARITMETICO, filaInicial, columnaInicial)
               return true
           }
           else{
               reportarError("ERROR LEXICO")
               return false
           }

       }
       return false
   }

    /*
    Metodo que indica si el lexema ingresado es una cadena
     */
    fun esCadena(): Boolean {
        if (caracterActual == '\'') {


            var bandera = true
            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            lexema += caracterActual

            obtenerSiguienteCaracter()
            while (!(caracterActual == '\'' || caracterActual == finCodigo) && bandera) {

                if (caracterActual == '\'') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (!(caracterActual == 'n' || caracterActual == 't' || caracterActual == '\'')) {
                        bandera = false
                    }
                    return false
                }
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }

            if (!bandera) {
                while (!(caracterActual == '\'' || caracterActual == finCodigo)) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
                if (caracterActual == '\'') {
                    lexema += caracterActual
                }
                obtenerSiguienteCaracter()
            } else {
                if (caracterActual == '\'') {
                    lexema += caracterActual
                    almacenarToken(lexema, Categoria.CADENA, filaInicial, columnaInicial)
                    obtenerSiguienteCaracter()
                } else {

                    hacerBT(posicionInicial, filaInicial, columnaInicial)
                    return false
                }
            }
            return true
        }
        return false
    }

    /*
	 * Metodo encargado de indicar si lo que se esta analizando es o no un
	 * comentario de bloque
     */
    fun esComentarioLinea(): Boolean {
        if (caracterActual != '#') {
            return false
        }
        var lexema = ""
        var filaInicial = filaActual
        var columnaInicial = columnaActual

        lexema += caracterActual
        obtenerSiguienteCaracter()
        while (caracterActual != '\n' && caracterActual !== finCodigo) {
            lexema += caracterActual
            obtenerSiguienteCaracter()
        }
        almacenarToken(lexema, Categoria.COMENTARIO_DE_LINEA, filaInicial, columnaInicial)
        return true

    }

    /*
	 * Metodo encargado de indicar si lo que se esta analizando es o no un
	 * comentario de bloque
     */
    fun esComentarioBloque(): Boolean {
        if (caracterActual == '$') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual

            lexema += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '$') {
                lexema += caracterActual
                obtenerSiguienteCaracter()



                while (!(caracterActual == '$' || caracterActual == finCodigo)) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }


                if (caracterActual == '$') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    if (caracterActual == '$') {
                        lexema += caracterActual
                        almacenarToken(lexema, Categoria.COMENTARIO_DE_BLOQUE, filaInicial, columnaInicial)
                        obtenerSiguienteCaracter()
                        return true
                    }
                   reportarError("mensaje error en comentario de bloque ")
                    return true
                }

              reportarError("error en comentario de bloque ")
                return true


            }
            hacerBT(posicionInicial, filaInicial, columnaInicial)
            return false
        }
        return false
    }

    fun esPalabraReservada(palabra: String): Boolean {

        if (!palabra.isEmpty()) {
            if (palabrasReservadas.contains(palabra)) {
                return true
            }
            return false
        }
        return false
    }


    /**
     * funcion encargada de obtener el siguiente caracter en otras funciones.
     */
    fun obtenerSiguienteCaracter() {
        if (posicionActual == codigoFuente.length - 1) {
            caracterActual = finCodigo
        } else {
            if (caracterActual == '\n') {
                filaActual++
                columnaActual = 0


            } else {
                columnaActual++
            }

            posicionActual++
            caracterActual = codigoFuente[posicionActual]
        }

    }

    fun reportarError(mensaje: String) {
        listaErrores.add(Error(mensaje, filaActual, columnaActual))
    }

}


