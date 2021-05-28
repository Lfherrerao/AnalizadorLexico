package co.edu.uniquindio.analizador.lexico

import co.edu.uniquindio.analizador.lexico.Categoria
/**
 * @author leonardo Fabio Herrera o.
 *@author John Alexander Martinez N.
 *
 * Class que reprecenta un Token.
 */
class Token (var lexema:String, var categoria: Categoria, var fila:Int, var columna:Int) {
    override fun toString(): String {
        return "Token(lexema='$lexema', categoria=$categoria, fila=$fila, columna=$columna)"
    }
}