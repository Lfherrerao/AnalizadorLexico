package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Categoria
import co.edu.uniquindio.analizador.lexico.Token
import javafx.beans.binding.ObjectBinding


class AnalizadorSintactico(var listaTokens: ArrayList<Token>) {

    var posicionActual = 0
    var tokenActual = listaTokens[0]
    val listaDeErrores = ArrayList<Error>()

    /**
     * Obtiene el siguiente token de la tabla de tokens
     */
    fun obtenerSiguienteToken() {
        posicionActual++
        if (posicionActual < listaTokens.size) {
            tokenActual = listaTokens[posicionActual]
        }
        /** else {
        tokenActual = Token("", Categoria.REPORTE_DE_ERROR, 0, 0);
        }*/
    }

    /**
     * Obtiene el anterior token de la tabla de tokens
     */
    fun obtenerAnteriorToken() {
        posicionActual--
        if (posicionActual < listaTokens.size) {
            tokenActual = listaTokens[posicionActual]
        }
    }


    /**
     * <UnidadDeCompilacion> ::= <ListaFunciones>
     */
    fun esUnidadDeCompilacion(): UnidadDeCompilacion? {
        val listaFunciones: ArrayList<Funcion> = esListaFuncion()
        return if (listaFunciones.size > 0) {

            UnidadDeCompilacion(listaFunciones)


        } else null

    }

    /**
     * <listaFunciones>::= <funcion>[<lsitaFunciones>]
     */
    fun esListaFuncion(): ArrayList<Funcion> {

        var listaFunciones = ArrayList<Funcion>()
        var funcion = esFuncion()

        while (funcion != null) {

            listaFunciones.add(funcion)
            funcion = esFuncion()
        }
        return listaFunciones

    }

    /**
     * <Funcion> ::= _function <tipoRetorno> identificador "(" [<ListaParametros>] ")" "{" [<BloqueSentencias>] "}"
     */
    fun esFuncion(): Funcion? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "_function") {
            obtenerSiguienteToken()
            var tipoRetorno = esTipoRetorno()
            if (tipoRetorno != null) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                    var nombreIdentificador = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                        obtenerSiguienteToken()
                    } else {
                        reportarError("falta el parentesis izquierdo en la funcion")
                    }
                    var listaParametro = esListaParametro()

                    if (tokenActual.categoria == Categoria.PARENTESIS_DEREHO) {
                        obtenerSiguienteToken()
                    } else {
                        reportarError("falta el parentesis derecho")
                    }
                    var bloqueDeSentencias = esBloqueDeSentencia()

                    return Funcion(nombreIdentificador, tipoRetorno, listaParametro, bloqueDeSentencias!!)


                } else {
                    reportarError("problema con el identificador, recuerde que los identificadores enpiezan con un guion bajo _")
                }

            } else {
                reportarError("error sintactico")
            }


        }
        return null
    }

    /**
     * <TipoRetorno> ::= _int | _string | _double | _char | _void | _boolean
     */
    fun esTipoRetorno(): Token? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            if (tokenActual.lexema == "_int" || tokenActual.lexema == "_string" || tokenActual.lexema == "_double" || tokenActual.lexema == "_char") {
                return tokenActual
            }
        }
        return null
    }

    /**
     * <ListaParametros> ::= <Parametro>[","<ListaParametros>]
     */
    fun esListaParametro(): ArrayList<Parametro> {

        var parametros = ArrayList<Parametro>()
        var parametro = esParametro()

        while (parametro != null) {
            parametros.add(parametro)
            if (tokenActual.categoria == Categoria.ES_COMA) {
                obtenerSiguienteToken()
                parametro = esParametro()
            } else {

                if (tokenActual.categoria != Categoria.PARENTESIS_DEREHO) {
                    reportarError("Falta una coma en la lista de parmetros. Error en esListaParemetro")
                }
                break
            }
        }
        return parametros
    }

    /**
     * <BloqueDeSentencias> ::=  "{"[<ListaSentencias>]"}"
     */
    fun esBloqueDeSentencia(): ArrayList<Sentencia>? {

        if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
            obtenerSiguienteToken()
            var listaSentencia = esListaSentencia()

            if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                obtenerSiguienteToken()
                return listaSentencia
            } else {
                reportarError("falta llave derecha en el bloque de sentencias ")
            }
            reportarError("falta llave izquierda en el bloque de sentencias ")
        }
        return null
    }

    /**
     * <ListaSentencias> ::= <Sentencia>[<ListaSentencias>]
     */
    fun esListaSentencia(): ArrayList<Sentencia>? {
        var sentencias = ArrayList<Sentencia>()
        var s = esSentencia()


        while (s != null) {
            sentencias.add(s)
            s = esSentencia()
        }

        return sentencias
    }

    /**
     * <Sentencia> ::= <Decision> | <Declaracion> | <Invocacion> | <Impresion> |
     * <Ciclo> | <Retorno> | <Lectura>| <ExpresionAsignacion>|
     */
    fun esSentencia(): Sentencia? {
        var s: Sentencia? = esCiclo()
        if (s != null) {
            return s
        }

        s = esDeclaracion()
        if (s != null) {
            return s
        }

        s = esDecision()
        if (s != null) {
            return s;
        }

        s = esImpresion();
        if (s != null) {
            return s;
        }

        s = esLectura()
        if (s != null) {
            return s;
        }

        s = esRetorno()
        if (s != null) {
            return s
        }

        s = esExpresionAsignacion()
        if (s != null) {
            return s;
        }
        s = esIncremento()
        if (s != null) {
            return s
        }

        s = esDecremento()

        if (s != null) {
            return s
        }

        s = esInvocacion();

        if (s != null) {
            return s
        }


        return null
    }

    /**
     * <Parametro> ::= <TipoDeDato> identificador
     */
    fun esParametro(): Parametro? {

        var tipoDato: Token? = esTipoDeDato()
        if (tipoDato != null) {
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {

                var nombre = tokenActual
                obtenerSiguienteToken()
                return Parametro(tipoDato, nombre)
            } else {
                reportarError("Algo anda mal en la lista de parametros falta el nombre")
                obtenerSiguienteToken()
                return null

            }


        }
        return null
    }

    /**
     * <TipoDato> ::= _int | _string | _double | _char| _boolean
     */
    fun esTipoDeDato(): Token? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            if (tokenActual.lexema == "_boolean" || tokenActual.lexema == "_double"
                || tokenActual.lexema == "_int" || tokenActual.lexema == "_char" || tokenActual.lexema == "_boolean" || tokenActual.lexema == "_string"
            ) {
                return tokenActual
            }
        }
        return null
    }

    /**
     * <Ciclo> ::= _while "("<ExpresionRelacional>")""["[<listaSentencias>]"]"
     */
    fun esCiclo(): Ciclo? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "_while") {
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                obtenerSiguienteToken()
                var expresionR = esExpresionRelacional()

                if (expresionR != null) {

                    if (tokenActual.categoria == Categoria.PARENTESIS_DEREHO) {
                        obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.CORCHETE_IZQUIERDO) {
                            obtenerSiguienteToken()
                            var sentencias: ArrayList<Sentencia>?

                            sentencias = esListaSentencia()


                            if (tokenActual.categoria == Categoria.CORCHETE_DEREHO) {

                                obtenerSiguienteToken()
                                return Ciclo(expresionR, sentencias)
                            } else {
                                reportarError("Falta corchete derecho en el ciclo")
                            }
                        } else {
                            reportarError("Falta corchete izquierdo en el ciclo")
                        }
                    } else {
                        reportarError("Falta parentesis derecho en el ciclo")
                    }
                } else {
                    reportarError("Falta expresion relacional en el ciclo")
                }
            } else {
                reportarError("Falta parentesis izquierdo en el ciclo")
            }
        }

        return null
    }

    /**
     * <Decision>::= _if "(" <ExpresionRelacional> ")" "[" [<ListaSentencias>] "]"[_else "[" [<ListaSentencias>] "]"]
     */
    fun esDecision(): Decision? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "_if") {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                obtenerSiguienteToken()
                var expresionR = esExpresionRelacional()



                if (expresionR != null) {
                    if (tokenActual.categoria == Categoria.PARENTESIS_DEREHO) {
                        obtenerSiguienteToken()
                        var sentenciasSI: ArrayList<Sentencia>?
                        if (tokenActual.categoria == Categoria.CORCHETE_IZQUIERDO) {
                            obtenerSiguienteToken()
                            sentenciasSI = esListaSentencia()
                            if (tokenActual.categoria == Categoria.CORCHETE_DEREHO) {
                                obtenerSiguienteToken()
                                var sentenciasNO: ArrayList<Sentencia>?
                                if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "_else") {
                                    obtenerSiguienteToken()

                                    if (tokenActual.categoria == Categoria.CORCHETE_IZQUIERDO) {
                                        obtenerSiguienteToken()
                                        sentenciasNO = esListaSentencia()

                                        if (tokenActual.categoria == Categoria.CORCHETE_DEREHO) {
                                            obtenerSiguienteToken()
                                            return Decision(expresionR, sentenciasSI, sentenciasNO)
                                        }
                                        reportarError("Falta corchete derecho en el No de la decisi�n")
                                    }
                                    reportarError("Falta corchete izquierdo en el No de la decisi�n")
                                }


                                return Decision(expresionR, sentenciasSI, null)
                                obtenerSiguienteToken()


                            }
                            reportarError("Falta corchete derecho en el SI de la decisi�n")
                        }
                        reportarError("Falta corchete izquierdo en el SI de la decisi�n")
                    }
                    reportarError("Falta parentesis derecho en la decisi�n")
                }
                reportarError("Falta expresion relacional en la decisi�n")
            }
            reportarError("Falta parentesis izquierdo en la decisi�n")
        }
        return null
    }

    /**
     * <Impresion>::= _print <ExpresionCadena> "!"
     */
    fun esImpresion(): Impresion? {
        if (tokenActual.categoria === Categoria.PALABRA_RESERVADA && tokenActual.lexema == "_print") {
            obtenerSiguienteToken()
            val cadena = esExpresionCadena()
            if (cadena != null) {
                if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                    obtenerSiguienteToken()
                    return Impresion(cadena)
                } else {
                    reportarError("Falta fin de sentencia en la impresiOn")
                }
            } else {
                reportarError("Falta la expresi�n cadena en la impresion")
            }
        }
        return null
    }

    /**
     * <ExpresionCadena>::= cadenacaracteres ["#"<ExpresionCadena>]
     */
    fun esExpresionCadena(): ExpresionCadena? {
        var cadenas = ArrayList<Token>()

        if (tokenActual.categoria == Categoria.CADENA) {
            cadenas.add(tokenActual)
            obtenerSiguienteToken()

            while (tokenActual.lexema == "#") {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.CADENA) {
                    cadenas.add(tokenActual)
                    obtenerSiguienteToken()
                } else {
                    reportarError("Falta concatenar en la cadena de caracteres")
                }
            }

            return ExpresionCadena(cadenas);
        }
        return null
    }

    /**
     * <Lectura>::= _read identificador "!"
     */
    fun esLectura(): Lectura? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == ("_read")) {
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var nombre = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                    obtenerSiguienteToken()
                    return Lectura(nombre);
                } else {
                    reportarError("Falta fin de sentencia en la Lectura")
                }

            } else {
                reportarError("Falta la identificador en la lectura")
            }

        }

        return null;
    }

    /**
     * <Declaracion>::= _var <TipoDato> identificador ":" <expresionAritmetica>"!"
     */
    fun esDeclaracion(): Declaracion? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "_var") {
            obtenerSiguienteToken()

            var tipoDato: Token?
            tipoDato = esTipoDeDato()
            if (tipoDato != null) {
                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {

                    var nombre: Token?
                    nombre = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {

                        obtenerSiguienteToken()
                        return Declaracion(tipoDato, nombre)


                    } else {
                        reportarError("Falta el fin de sentencia");
                    }
                } else {
                    reportarError("Falta el identificador");
                }

            } else {
                reportarError("Falta el tipo de dato");
            }
        }

        return null;
    }

    /**
     * <ExpresionRelacional>::= <Termino> operadorRelacional <Termino>
     */
    fun esExpresionRelacional(): ExpresionRelacional? {

        val termino1 = esTermino()


        if (termino1 != null) {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.OPERADOR_RELACIONAL) {

                var operador = tokenActual
                obtenerSiguienteToken()
                var termino2 = esTermino()

                if (termino2 == null) {
                    reportarError("Falta segundo termino en la expresiòn aritmetica. Error en esExpresionRelacional")
                    while (!(tokenActual.lexema == "!")) {
                        obtenerSiguienteToken()
                    }

                }
                obtenerSiguienteToken()
                return ExpresionRelacional(termino1, operador, termino2)

            } else {
                obtenerAnteriorToken()
                reportarError("falta el operador relacional en la expresion. Error en esExpresionRelacional")
            }
        }
        reportarError("$tokenActual no es considerado un termino. error en  esExpresionRelacional")
        return null
    }

    /**
     * <Termino>::= identificador | numero| decimal
     *
     */
    fun esTermino(): Token? {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR || tokenActual.categoria == Categoria.ENTERO
            || tokenActual.categoria == Categoria.DECIMAL
        ) {
            return tokenActual
        }
        return null
    }

    /**
     * <Retorno>::= _return <Expresion> "!"
     */
    fun esRetorno(): Retorno? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == ("_return")) {
            obtenerSiguienteToken()


            var expresion: Expresion?

            expresion = esExpresion()


            if (expresion != null) {
                if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                    obtenerSiguienteToken()
                    return Retorno(expresion)
                } else {
                    reportarError("Falta fin de sentencia en el retorno")
                }

            } else {
                reportarError("Falta la expresi�n en el retorno")

            }

        }
        return null
    }

    /**
     * <Expresion>::= <ExpresionAritmetica> | <ExpresionRelacional> |
     * <ExpresionCadena>
     */
    fun esExpresion(): Expresion? {

        var e: Expresion? = esExpresionAritmetica()
        if (e != null) {
            return e
        }

        e = esExpresionRelacional()
        if (e != null) {
            return e
        }

        e = esExpresionCadena()
        if (e != null) {
            return e
        }

        return null
    }

    /**
     * <ExpresionAritmetica>::= <Termino> operador aritmetico <Termino>
     *     <ExpresionAritmetica>::= "("  <ExpresionAritmetica> [ operadorAritmetico  <ExpresionAritmetica> ] | <Termino> [ operadorAritmetico  <ExpresionAritmetica> ]
     */
    fun esExpresionAritmetica(): ExpresionAritmetica? {
        var posicionToken = posicionActual
        if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
            obtenerSiguienteToken()
            val exp1 = esExpresionAritmetica()
            if (exp1 != null) {
                if (tokenActual.categoria == Categoria.PARENTESIS_DEREHO) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO) {
                        val operador = tokenActual
                        obtenerSiguienteToken()
                        val expr2 = esExpresionAritmetica()
                        if (expr2 != null) {
                            return ExpresionAritmetica(exp1, operador, expr2)
                        }
                    } else {
                        return ExpresionAritmetica(exp1)
                    }
                }
            }
        } else {
            val valor = esValorNumerico()
            if (valor != null) {
                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO) {
                    val operador = tokenActual
                    obtenerSiguienteToken()
                    val expr = esExpresionAritmetica()
                    if (expr != null) {
                        return ExpresionAritmetica(valor, operador, expr)
                    }
                } else if (tokenActual.categoria == Categoria.OPERADOR_RELACIONAL ) {
                    obtenerAnteriorToken()
                    return null
                } else {
                    return ExpresionAritmetica(valor)
                }
            }
        }

        return null

    }

    /**
     * <valorNumerico>::= [<signo>] decimal |[<signo>] entero| |[<signo>] identificador
     */
    fun esValorNumerico(): ValorNumerico? {
        var signo: Token? = null
        if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO && (tokenActual.lexema == "-" || tokenActual.lexema == "=")) {
            signo = tokenActual
        }
        if (tokenActual.categoria == Categoria.ENTERO || tokenActual.categoria == Categoria.DECIMAL || tokenActual.categoria == Categoria.IDENTIFICADOR) {

            val termino = tokenActual
            return ValorNumerico(signo, termino)
        }
        return null
    }

    /**
     * <ExpresionAsignacion>::= identificador ":" "!"
     * <Asignacion>::=  palabraReservada indentificador operadorAsignacion
     */
    fun esExpresionAsignacion(): ExpresionAsignacion? {

        if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
            var nombre = tokenActual
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                obtenerSiguienteToken()

                var expresion = esExpresion()
                var termino = esTermino()

                println("en analizador Semanticco lin 684: expresion ${expresion.toString()}  y termino ${termino.toString()}")
                if (expresion != null) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                        obtenerSiguienteToken()

                        return ExpresionAsignacion(nombre, expresion)
                    } else {
                        reportarError("Falta fin de sentencia en la expresi�n de asignaci�n")
                    }

                } else if (termino != null) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                        obtenerSiguienteToken()
                        return ExpresionAsignacion(nombre, termino);
                    } else {
                        reportarError("Falta fin de sentencia en la expresi�n de asignaci�n")
                    }
                } else if (tokenActual.categoria == Categoria.PALABRA_RESERVADA
                    && tokenActual.lexema == ("_true")
                    || tokenActual.categoria == Categoria.PALABRA_RESERVADA
                    && tokenActual.lexema == ("_false")
                ) {
                    var expresionA = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                        obtenerSiguienteToken()
                        return ExpresionAsignacion(nombre, expresionA)
                    } else {
                        reportarError("Falta fin de sentencia en la expresi�n de asignaci�n");
                    }

                } else {
                    reportarError("Falta expresi�n en la expresi�n de asignaci�n");
                }

            } else {
                obtenerAnteriorToken()
                return null

            }
        }
        return null;
    }

    /**
     * <Incremento>::=operadorincremento identificador "!"
     */
    fun esIncremento(): Incremento? {
        if (tokenActual.categoria == Categoria.INCREMENTO) {
            var operador: Token = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var nombre = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                    obtenerSiguienteToken()
                    return Incremento(nombre, operador)
                } else {
                    reportarError("Falta fin de sentencia en el incremento")
                }
            } else {
                reportarError("Falta identificador en el incremento")
            }
        }

        return null
    }

    /**
     * <Decremento>::=operadordecremento identificador "!"
     */
    fun esDecremento(): Decremento? {
        if (tokenActual.categoria == Categoria.DECREMENTO) {
            var operador: Token = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var nombre = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                    obtenerSiguienteToken()
                    return Decremento(nombre, operador)
                } else {
                    reportarError("Falta fin de sentencia en el incremernto")
                }
            } else {
                reportarError("Falta identificador en el incremento")
            }
        }

        return null
    }

    /**
     * <Invocacion>::= _call identificador "(" ")"!"
     */
    fun esInvocacion(): Invocacion? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == ("_call")) {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var nombreFuncion = tokenActual

                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                    obtenerSiguienteToken()
                    var argumentos: ArrayList<Argumento>?
                    argumentos = esListaArgumentos()


                    if (tokenActual.categoria == Categoria.PARENTESIS_DEREHO) {
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                            obtenerSiguienteToken()
                            return Invocacion(nombreFuncion, null)
                        } else {
                            reportarError("Falta fin de sentencia en la invocaci�n");
                        }
                    } else {
                        reportarError("Falta par�ntesis derecho en la invocaci�n");
                    }
                } else {
                    reportarError("Falta par�ntesis izquierdo en la invocaci�n");
                }

            } else {
                reportarError("Falta identificador en la invocaci�n");
            }
        }

        return null;
    }

    /**
     * <Argumento> ::= <ExpresionA>
     */
    fun esArgumento(): Argumento? {
        var expresionA = esExpresion()

        if (expresionA != null) {
            obtenerSiguienteToken()
            return Argumento(expresionA)
        }

        return null
    }

    /**
     * <ExpresionA>::= <Termino> | <Expresion> | _true | _false
     */
    fun esExpresionA(): ExpresionA? {

        var expresion = esExpresion()

        if (expresion != null) {
            obtenerSiguienteToken()
            return ExpresionA(expresion)
        }

        var termino = esTermino()

        if (termino != null) {
            obtenerSiguienteToken()
            return ExpresionA(termino)
        }

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == ("_false")
            || tokenActual.categoria == Categoria.PALABRA_RESERVADA
            && tokenActual.lexema == ("_true")
        ) {
            var logico = tokenActual
            obtenerSiguienteToken()
            return ExpresionA(logico)

        }

        return null;
    }

    /**
     * <ListaArgumentos> ::= <Argumento>[","<ListaArgumentos>]
     */
    fun esListaArgumentos(): ArrayList<Argumento>? {

        var argumentos = ArrayList<Argumento>()
        var a = esArgumento()



        while (a != null) {
            argumentos?.add(a)

            if (tokenActual.categoria == Categoria.COMA) {
                obtenerSiguienteToken()
                a = esArgumento()
            } else {

                if (tokenActual.categoria != Categoria.PARENTESIS_DEREHO) {
                    reportarError("Falta una como en la lista de argumento. Error en esLiArgumento")
                }
                break
            }
        }
        obtenerSiguienteToken()
        return argumentos

    }

    /**
     * Obtiene el token en la posicion indicada sin alterar el token actual de la
     * ejecucion.
     */
    fun obtenerTokenPosicionN(posicionToken: Int) {
        var posActual = posicionToken;
        if (posicionToken < listaDeErrores.size) {
            var tokenActual = listaDeErrores.get(posicionToken)
        } else {
            tokenActual = Token("", Categoria.REPORTE_DE_ERROR, 0, 0);
        }
    }

    /**
     * funcion encargada de guardar los errores ocurridos en el analizador sintactico
     */
    fun reportarError(mensaje: String) {
        listaDeErrores.add(Error(mensaje, tokenActual.fila, tokenActual.columna))
    }

}







