package co.edu.uniquindio.analizador.sintaxis

import co.edu.uniquindio.analizador.lexico.Token
import co.edu.uniquindio.analizador.semantica.TablaSimbolos
import com.sun.source.tree.Tree
import javafx.scene.control.TreeItem

class ExpresionCadena(var cadenas: ArrayList<Token>): Expresion() {

    override fun toString(): String {
        return "ExpresionCadena(cadenas=$cadenas)"
    }

   override fun getArboVisual(): TreeItem<String> {
         var raiz = TreeItem("cadena de expresion")

        if (cadenas != null) {


            var raizC=TreeItem(" ")
            for (c in cadenas){
                raizC = TreeItem(c.lexema)
            }
            raiz.children.add(raizC)


        }else{
            var raizNull = TreeItem("cadena vacia")
            raiz.children.add(raizNull)
        }
        return raiz
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String, listaErrores:ArrayList<Error>): String {
        return "_string"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if (cadenas!= null){
        }
    }
}