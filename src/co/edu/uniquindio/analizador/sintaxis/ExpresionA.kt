package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token

class ExpresionA() {

    var expresio: Expresion?= null
    var token: Token?= null
    var termino:Token?=null

    constructor( expresio: Expresion?) : this(){
        this.expresio = expresio
    }

    constructor( token: Token?) : this(){
        this.token= token
    }
}