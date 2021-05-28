package co.edu.uniquindio.analizador.semantica
    import co.edu.uniquindio.analizador.sintaxis.Error

class TablaSimbolos ( var listaErrores:ArrayList<Error> ){

    var listaSimbolos: ArrayList<Simbolo> = ArrayList()



    /**
     * Permite guardar un símbolo de tipo variable, constante o parametro en la tabla de símbolos
     */
    fun guardarSimboloValor(nombre: String, tipoDato: String, modificable: Boolean, ambito: String, fila: Int, columna: Int) {


        var s= buscarSimboloValor(nombre,ambito)

        if (s == null){
            listaSimbolos.add(Simbolo(nombre, tipoDato, modificable, ambito, fila, columna))
        }else{
            listaErrores.add(Error("el campo $nombre ya exixte dentro del ambito $ambito",fila,columna))
        }
    }

    /**
     * permite buscar un valor dentro de la tabla simbolos
     */
    fun buscarSimboloValor(nombre: String, ambito: String): Simbolo? {

        for (s in listaSimbolos) {

            if (s.tiposParametros!=null) {
                if (s.nombre == nombre && s.ambito == ambito) {

                    return s
                }
            }
        }
        return null
    }

    /**
     * permite buscar una fincion dentro de la tabla desimbolos
     */
    fun buscarSimboloFuncion(nombre: String, tiposParametros: ArrayList<String>): Simbolo? {

         for (s in listaSimbolos) {
             if (s.tiposParametros != null ) {

                if (s.nombre == nombre && s.tiposParametros == tiposParametros) {
                    return s

                }
            }
        }
        return null
    }

    /**
     * Permite guardar un símbolo de tipo función en la tabla de símbolos
     */
    fun guardarSimboloFuncion(nombre: String, tipoRetorno: String, tiposParametros: ArrayList<String>, ambito: String, fila: Int, columna: Int) {
        var s = buscarSimboloFuncion(nombre,tiposParametros)
        if (s== null){
            listaSimbolos.add(Simbolo(nombre, tipoRetorno, tiposParametros, ambito))
        }else{
            listaErrores.add(Error("la funcion con nombre  $nombre ya exixte dentro del ambito $ambito",fila,columna))
        }


    }

    override fun toString(): String {
        return "TablaSimbolos( listaSimbolos=$listaSimbolos )"
    }

}