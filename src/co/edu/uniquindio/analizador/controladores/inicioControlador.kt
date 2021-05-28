package co.edu.uniquindio.analizador.controladores

import co.edu.uniquindio.analizador.lexico.AnalizadorLexico
import co.edu.uniquindio.analizador.lexico.Categoria
import co.edu.uniquindio.analizador.lexico.Token
import co.edu.uniquindio.analizador.semantica.AnalizadorSemantico
import co.edu.uniquindio.analizador.sintaxis.AnalizadorSintactico
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*
import co.edu.uniquindio.analizador.sintaxis.Error


class InicioControlador : Initializable {


    @FXML
    lateinit var codigoFuente: TextArea
    @FXML
    lateinit var tablaTokens: TableView<Token>
    @FXML
    lateinit var colLexema: TableColumn<Token, String>
    @FXML
    lateinit var colFila: TableColumn<Token, Int>
    @FXML
    lateinit var colCategoria: TableColumn<Token, String>
    @FXML
    lateinit var colColumna: TableColumn<Token, Int>
    @FXML
    lateinit var arbolVisual: TreeView<String>


    @FXML
    lateinit var tablaErroreLexicos: TableView<Error>
    @FXML
    lateinit var colMensaErrorLexico: TableColumn<Error, String>
    @FXML
    lateinit var colFilaErrorLexico: TableColumn<Error, Int>
    @FXML
    lateinit var colColErrorLexico: TableColumn<Error, Int>


       @FXML
    lateinit var tablaErroreSemanticos: TableView<Error>
    @FXML
    lateinit var colMensaErrorSemanticos: TableColumn<Error, String>
    @FXML
    lateinit var colFilaErrorSemanticos: TableColumn<Error, Int>
    @FXML
    lateinit var colColErrorSemanticos: TableColumn<Error, Int>



    @FXML
    lateinit var tablaErroreSintacticos: TableView<Error>
    @FXML
    lateinit var colMensaErrorSintacticos: TableColumn<Error, String>
    @FXML
    lateinit var colFilaErrorSintacticos: TableColumn<Error, Int>
    @FXML
    lateinit var colColErrorSintacticos: TableColumn<Error, Int>





    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        colLexema.cellValueFactory = PropertyValueFactory("lexema")
        colFila.cellValueFactory = PropertyValueFactory("fila")
        colCategoria.cellValueFactory = PropertyValueFactory("categoria")
        colColumna.cellValueFactory = PropertyValueFactory("columna")


        colMensaErrorLexico.cellValueFactory= PropertyValueFactory("Mensaje")
        colFilaErrorLexico.cellValueFactory = PropertyValueFactory("fila")
        colColErrorLexico.cellValueFactory = PropertyValueFactory("Columna")

        colMensaErrorSintacticos.cellValueFactory= PropertyValueFactory("Mensaje")
        colFilaErrorSintacticos.cellValueFactory = PropertyValueFactory("fila")
        colColErrorSintacticos.cellValueFactory = PropertyValueFactory("Columna")


        colMensaErrorSemanticos.cellValueFactory= PropertyValueFactory("Mensaje")
        colFilaErrorSemanticos.cellValueFactory = PropertyValueFactory("fila")
        colColErrorSemanticos.cellValueFactory = PropertyValueFactory("Columna")
    }

    @FXML
    fun analizarCodigo(e: ActionEvent) {

        if (codigoFuente.text.isNotEmpty()) {
            var lexico = AnalizadorLexico(codigoFuente.text)
            lexico.analizar()
            //   println(lexico.listaTokens)
            tablaTokens.items = FXCollections.observableArrayList(lexico.listaTokens)
           tablaErroreLexicos.items = FXCollections.observableArrayList(lexico.listaErrores)



            if (lexico.listaErrores.isEmpty()) {
                val sintaxis = AnalizadorSintactico(lexico.listaTokens)
                val uc = sintaxis.esUnidadDeCompilacion()
                tablaErroreLexicos.items = FXCollections.observableArrayList(lexico.listaErrores)
                tablaErroreSintacticos.items = FXCollections.observableArrayList(sintaxis.listaDeErrores)


                if (uc != null) {
                    arbolVisual.root = uc.getArboVisual()
                    val semantica = AnalizadorSemantico(uc!!)
                    semantica.llenarTablaSimbolo()
                    println("${semantica.tablaSimbolos}")

                    semantica.analizarSemantica()
                    println(semantica.listaErrores)
                    tablaErroreSemanticos.items = FXCollections.observableArrayList(semantica.listaErrores)



                } else {
                    arbolVisual.root = TreeItem("unidad de compilacion")
                }
            } else {
                var alerta = Alert(Alert.AlertType.WARNING)
                alerta.headerText = "Alerta"
                alerta.contentText = "Hay Errores Lexicos En El CodigoFuente"
                alerta.showAndWait()
            }
        }
    }



}
