//Martín\eclipse-workspace\Compiler\src\compiler>yacc -J -v gramatica.y


/*practico 3

para enzo:
-dividir <bloque> entre <bloque_if> y <bloque_else>
-pensar switch como una cadena de if

para mi:
-falta chequear que los tipos del switch sean el mismo
-que cuando se hace una invocacion a funcion, la funcion exista
-no tenemos conversiones, por lo tanto no se puede hacer una operacion entre operands de diferentes tipos (mas errores)
dudas:
-si en una funcion hay un error, tengo que ejecutar lo de adentro?
-si se redeclara una funcion las variables locales de la segunda funcion pueden usar las variables locales de la primer funcion, NO SE DEBERIA PODER

*/

%{

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Iterator;
import java.util.Stack;
%}


%token IF THEN ELSE ENDIF BEGIN END OUT SWITCH CASE FUNCTION MOVE RETURN UINT DOUBLE CADMULTI COMP_DIFERENTE COMP_MAYOR_IGUAL COMP_MENOR_IGUAL COMP_IGUAL COMP_MAYOR COMP_MENOR CTE ID
%left '+' '-'
%left '*' '/'
%nonassoc ELSE
%start programa

%%

programa : contenidoPrograma {addRule( "Programa leido correctamente."); System.out.println("Programa leido correctamente.");
                                  arb = new Node("PROGRAM",(Arbol)$1.obj,null);
                                  st.update();
                                  }
;

elementoPrograma: sentencia { $$ = $1;}
                | funcion {addRule( "Funcion."); $$ = $1; }
;

contenidoPrograma : elementoPrograma {  $$ = $1;  }
                  | contenidoPrograma elementoPrograma{  $$ = new ParserVal(new Node("LS",(Arbol)$1.obj,(Arbol)$2.obj)); }
;

tipo : UINT | DOUBLE
;

headerFUNCTION : tipo FUNCTION ID {
                                    $$ = $3;
                                    lastType = ((Token)$1.obj).getId();

                                  }
              | tipo MOVE FUNCTION ID
                                  {
                                    inFunctionMove=true;
                                    $$=$4;
                                    lastType = ((Token)$1.obj).getId();
                                  }

            //  | tipo error ID {addSyntacticError("Se espera FUNCION o MOVE FUNCTION.");}


              | error FUNCTION ID {
              addSyntacticError("Tipo mal definido.");
              $$.obj=$3.obj;
              lastType = ((Token)$1.obj).getId();
            }

              //| tipo FUNCTION error {addSyntacticError("Identificador de función mal definido."); }

              | tipo MOVE error ID {
                inFunctionMove=true;
              addSyntacticError("Se espera la palabra reservada FUNCTION luego de MOVE");
              $$.obj=$4.obj;
              lastType = ((Token)$1.obj).getId();
            }
;

funcion : headerFUNCTION {
                          String id = ((Token)$1.obj).getId();
                          changeIdDeclaratedFunction( id );
                          updateFunctionUse( id );
                          updateFunctionType(id , lastType);
                          ((Token)$1.obj).setPointer(st.get( id,"@main"));
                          startFunction(id);
                         } '{' conjuntoSentencias RETURN '(' expresion ')' '.' '}' {
                                                                        checkTypeError("Todos los operandos de la expresión a retornar deben ser del mismo tipo.");
                                                                        if (!this.type.equals("error") && !this.type.equals(lastType))
                                                                            addSemanticError("El tipo de retorno debe ser igual al tipo de la expresión.");
                                                                        this.type="";
                                                                        endFunction();
                                                                        String id = ((Token)$1.obj).getId();
                                                                        Node nodereturn = new Node("RETURN",(Arbol)$7.obj,null);
                                                                        Node cuerpo = new Node("CUERPO_FUNCION", (Arbol)$4.obj, nodereturn);
                                                                        Node func = new Node("FUNCION",new NodeHoja( (Token)$1.obj , st.getVariableScope(id,ambito)),cuerpo);
                                                                        $$ = new ParserVal(func);
                                                                      }
;

invokeFUNCTION: ID '(' ')' { if (checkFunctionInvocationCorrectness(((Token)$1.obj).getId())) {checkType((String)st.getType( ((Token)$1.obj).getId(), "@main")); $$ = new ParserVal(new NodeHoja( ((Token)$1.obj).getId()+"@main", st.getType( ((Token)$1.obj).getId()+"@main"), "invocacion" ));}
                              else $$ = new ParserVal(new NodeHoja( ((Token)$1.obj).getId(), "", ""));}
              | ID '(' error {addSyntacticError("Falta paréntesis en la invocación a función.");
                              $$ = new ParserVal(new NodeHoja( ((Token)$1.obj).getId(), "", ""));}
              | error '(' ')' {addSyntacticError("Identificador mal definido en la invocación a función.");
                              $$ = new ParserVal(new NodeHoja( "error", "", ""));}

;

sentenciaDeclarativa: declaracionVariables {addRule("sentencia Declarativa."); }
;


declaracionVariables: listaVariables ':' tipo '.'
                      {
                        changeIdDeclaratedVariables();
                        setDeclaratedType(((Token)$3.obj).getId());

                      }
                    | listaVariables ':' error '.' {addSyntacticError("Tipo inválido."); listaVariablesDeclaracion.clear();
                                                      st.deleteReg(((Token)$3.obj).getId());
                                                    }
;

listaVariables: ID
                {
                  listaVariablesDeclaracion.add(((Token)$1.obj).getId());
                }
              | listaVariables ',' ID {listaVariablesDeclaracion.add(((Token)$3.obj).getId());}
;

sentenciaEjecutable : sentenciaIF {addRule( "sentencia IF."); $$.obj= $1.obj; }
                    | sentenciaSalida { $$ = $1;}
                    | sentenciaSWITCH {addRule( "sentencia SWITCH CASE"); $$ = $1;  }
                    | asignacion '.' { addRule( "Asignación.");  checkTypeError("Tipos diferentes en asignación."); this.type=""; $$ = $1; }
;

sentencia : sentenciaDeclarativa {$$ = new ParserVal(new Node("DECLARACION",null,null)); }
          | sentenciaEjecutable { $$ = $1;  }
;

conjuntoSentencias: sentencia {$$ = $1;}
                  | conjuntoSentencias sentencia  {   Node node = new Node ("LS",(Arbol)$1.obj,(Arbol)$2.obj) ;
                                                      $$ = new ParserVal(node); }
;

conjuntoSentencia: sentencia {$$ = $1; }
                            | conjuntoSentencia sentencia  { $$ = new ParserVal(new Node ("LS",(Arbol)$1.obj,(Arbol)$2.obj));}
                            | error {addSyntacticError("Conjunto de sentencias ejecutables mal definida."); $$ = new ParserVal(new NodeHoja ("LS (error)","",""));}
;


bloqueSentenciaThen : BEGIN conjuntoSentencia END '.' {
                                                                  Node node =  new Node("BLOQUE_THEN",(Arbol)$2.obj,null);
                                                                  $$= new ParserVal(node);                                                     }
                | error conjuntoSentencia END '.' {addSyntacticError("Falta BEGIN.");
                                                              Node node =  new Node("BLOQUE_THEN",(Arbol)$2.obj,null);
                                                              $$= new ParserVal(node);}
                | BEGIN conjuntoSentencia error '.' {addSyntacticError("Falta END.");
                                                                Node node =  new Node("BLOQUE_THEN",(Arbol)$2.obj,null);
                                                                $$= new ParserVal(node);}
;

bloqueSentenciaElse : BEGIN conjuntoSentencia END '.' {$$ = new ParserVal(new Node("BLOQUE_ELSE",(Arbol)$2.obj,null));}
                | error conjuntoSentencia END '.' {addSyntacticError("Falta BEGIN."); $$ = new ParserVal(new Node("BLOQUE_ELSE",(Arbol)$2.obj,null));}
                | BEGIN conjuntoSentencia error '.' {addSyntacticError("Falta END."); $$ = new ParserVal(new Node("BLOQUE_ELSE",(Arbol)$2.obj,null));}
;

bloqueSentencia : BEGIN conjuntoSentencia END '.' {  Node node =   new Node("BLOQUE",(Arbol)$2.obj,null);
                                                                  $$= new ParserVal(node);  }
                | error conjuntoSentencia END '.' {addSyntacticError("Falta BEGIN.");Node node =   new Node("BLOQUE",(Arbol)$2.obj,null);
                                                                                  $$= new ParserVal(node); }
                | BEGIN conjuntoSentencia error '.' {addSyntacticError("Falta END.");Node node =   new Node("BLOQUE",(Arbol)$2.obj,null);
                                                                                  $$= new ParserVal(node); }
;

comparador: COMP_IGUAL {$$.obj = "==";}
          | COMP_MAYOR{$$.obj = ">";}
          | COMP_MENOR{$$.obj = "<";}
          | COMP_MAYOR_IGUAL{$$.obj = ">=";}
          | COMP_MENOR_IGUAL{$$.obj = "<=";}
          | COMP_DIFERENTE{$$.obj = "<>";}
;

//SENTENCIA IF

condicion : expresion comparador expresion {checkTypeError("Tipo inconcistente en la condición del IF.");this.type=""; Node node1 = new Node((String)$2.obj,(Arbol)$1.obj,(Arbol)$3.obj);
                                                                                                                      Node node2 = new Node("CONDICION_IF",node1,null);
                                                                                                                      $$ = new ParserVal(node2);}
          | expresion comparador error { addSyntacticError("Se espera una expresion luego del operador de comparacion.");
                                                                                                                                NodeHoja node = new NodeHoja("CONDICION_IF (error)",null,null);
                                                                                                                                $$ = new ParserVal(node);}
          | expresion error expresion { addSyntacticError("Se espera un comparador."); checkTypeError("Tipo inconcistente en la condición del IF.");
                                                                                        NodeHoja node = new NodeHoja("CONDICION_IF (error)","","");
                                                                                        $$ = new ParserVal(node);}
          | error comparador expresion { addSyntacticError("Se espera una expresion antes del operador de comparacion.");
                                                                                        NodeHoja node = new NodeHoja("CONDICION_IF (error)","","");
                                                                                        $$ = new ParserVal(node); }
;


sentenciaIF : IF '(' condicion  ')' THEN bloqueSentenciaThen ELSE bloqueSentenciaElse ENDIF {
                Node cuerpo = new Node("CUERPO_IF",(Arbol)$6.obj,(Arbol)$8.obj);
                Node fi = new Node("IF",(Arbol)$3.obj,cuerpo);
                $$ = new ParserVal(fi);
             }
            | IF '(' condicion ')' THEN bloqueSentenciaThen ENDIF
                                { Node cuerpo = new Node("CUERPO_IF",(Arbol)$6.obj,null);
                                  Node node = new Node("IF",(Arbol)$3.obj,cuerpo);
                                    $$ = new ParserVal(node);
                                  }
            | IF '(' condicion ')' error bloqueSentenciaThen ENDIF { addSyntacticError("Falta THEN.");
                                                                    Node node = new Node("IF",(Arbol)$3.obj,(Arbol)$6.obj);
                                                                      $$ = new ParserVal(node);}
            | IF '(' condicion ')' THEN bloqueSentenciaThen error { addSyntacticError("falta ENDIF");
                                                                    Node node = new Node("IF",(Arbol)$3.obj,(Arbol)$6.obj);
                                                                    $$ = new ParserVal(node);}
  ;

//SENTENCIA CASE

        lineaCASE : CASE cte ':' {this.type="";} bloqueSentencia '.' {
                                                        if ( !tipoCase.peek().equals("") && !((NodeHoja)$2.obj).getType().equals(tipoCase.peek())){
                                                          addSemanticError("La constante debe ser del tipo '" + tipoCase.peek() + "'.");
                                                        }
                                                    else{
                                                      Node node = new Node("CASE",(Arbol)$2.obj,(Arbol)$5.obj);
                                                      $$  = new ParserVal(node);
                                                  }
                                                  }
                                                      //$$.obj=$2.obj;}
                  | error cte ':' bloqueSentencia '.' {addSyntacticError("Se espera palabra reservada CASE.");}  //{$$.obj=$2.obj;}}
                  | CASE error ':' bloqueSentencia '.' {addSyntacticError("Se espera una constante luego de CASE."); }
                  | CASE cte error bloqueSentencia '.' {addSyntacticError("Se espera ':' luego de la constante.");} // {$$.obj=$2.obj;}}
        ;

        bloqueCASE: lineaCASE {
                                  $$ = $1;
                                        }
                  | bloqueCASE lineaCASE { $$ = new ParserVal ( new Node( "CUERPO_CASE", (Arbol)$1.obj,(Arbol)$2.obj)); }
        ;

        cuerpoSwitch : '{' bloqueCASE '}' {$$ = $2;}
                      |'{' bloqueCASE error {addSyntacticError("Falta llave inicial del bloqueCASE."); }
                      | error bloqueCASE '}' {addSyntacticError("Falta llave inicial del bloqueCASE."); }

        encabezadoSWITCH : SWITCH '(' ID ')' {
                                            String id = ((Token)$3.obj).getId();
                                            if ( checkVariableCorrectness(id) ){
                                              tipoCase.push(st.getType( id, ambito));
                                              ((Token)$3.obj).setPointer(st.get(id,ambito));
                                            }
                                            else {
                                              tipoCase.push("error");
                                            }
                                            $$ = new ParserVal(new NodeHoja( (Token)$3.obj, st.getVariableScope(id,ambito)) );
                                            }

                        | SWITCH error ID ')' {addSyntacticError("Falta paréntesis inicial del identificador."); }
                        | SWITCH '(' ID error {addSyntacticError("Falta paréntesis final del identificador."); }
                        | SWITCH '(' error ')' {addSyntacticError("Se espera un identificador entre paréntesis."); }

        ;

        sentenciaSWITCH : encabezadoSWITCH cuerpoSwitch '.' {
                                                              Node swich = new Node("SWITCH",(Arbol)$1.obj,(Arbol)$2.obj);
                                                              tipoCase.pop();
                                                              $$ = new ParserVal (swich);}
                        | encabezadoSWITCH cuerpoSwitch error {addSyntacticError("Falta '.' final en la sentencia switch."); }

sentenciaSalida : OUT '(' CADMULTI ')' '.' {addRule( "sentencia OUT"); $$ =  new ParserVal (new Node( "OUT", new NodeHoja( (Token)$3.obj, "" ) , null));}
;

// EXPRESION

factor : ID { String id = ((Token)$1.obj).getId();
              if (checkVariableCorrectness(id)){
                checkType(st.getType(id, ambito));
                ((Token)$1.obj).setPointer(st.get(id,ambito));
                }
                $$ = new ParserVal(new NodeHoja((Token)$1.obj,st.getVariableScope(id,ambito)));
              }
      | cte {
            $$ = $1;}
      | invokeFUNCTION { $$ = new ParserVal(new Node("INVOCACION",(Arbol)$1.obj,null)); }
;



cte : CTE  {
            checkType( st.getType( ((Token)$1.obj).getId() ) );
            NodeHoja node =  new NodeHoja((Token)$1.obj,"");
            $$ = new ParserVal(node);
            }

    | '-' CTE { checkType( st.getType( ((Token)$2.obj).getId() ) );
                st.setNegativeDouble( (Token)$2.obj );
                String negative = "-" + ((Token)$2.obj).getId();
                NodeHoja node =  new NodeHoja(negative, st.get(negative).getType(), st.get(negative).getUse());
                $$ = new ParserVal(node); }
;

termino : termino '*' factor {$$ = new ParserVal(new Node("*",(Arbol)$1.obj,(Arbol)$3.obj));}
        | termino '/' factor {$$ = new ParserVal(new Node("/",(Arbol)$1.obj,(Arbol)$3.obj));}
        | factor {$$ = $1;}
;

expresion : expresion '+' termino { $$ = new ParserVal(new Node("+", (Arbol)$1.obj,(Arbol)$3.obj));}
          | expresion '-' termino { $$ = new ParserVal(new Node("-",(Arbol)$1.obj,(Arbol)$3.obj));}
          | termino { $$= $1;}
;

//ASIGNACION
asignacion: ID '=' expresion {
                              String id = ((Token)$1.obj).getId();
                              if (checkVariableCorrectness(id)) {
                                  checkType( st.getType( id , ambito) );
                                  ((Token)$1.obj).setPointer(st.get(id,ambito));
                              }
                                $$  =  new ParserVal (new Node("=",new NodeHoja((Token)$1.obj, st.getVariableScope(id,ambito)),(Arbol)$3.obj));
                              }
          | ID error expresion {  addSyntacticError("Se espera '=' en la asignacion.");
                                  $$  =  new ParserVal (new Node("=",new NodeHoja((Token)$1.obj, ambito),(Arbol)$3.obj));}
          | ID '=' error { addSyntacticError("Expresion invalida en asignación.");
                          $$  =  new ParserVal (new Node("asignacion(error)",null,null)); }
      //  |error '=' expresion { addSyntacticError("Se espera un identificador válido al comienzo de la asignación."); }
       // | ID ASIGN expresion error {addRule( ": ");}
;

%%
private Arbol arb;
private LexicAnalyzer lexic;
private Code code;
private ArrayList<String> lexicErrors;
private ArrayList<String> rules;
private ArrayList<String> tokens;
private ArrayList<String> syntacticErrors;
private ArrayList<String> semanticErrors;
private SymbolTable st = new SymbolTable();
private ArrayList<String> listaVariablesDeclaracion;;
private String ambito="@main";
private boolean inFunctionMove=false;
private String lastType = ""; //se utiliza para chquear devolucion en funciones
private Stack<String> tipoCase = new Stack<String>(); //se utiliza para chequear tipo de constantes en switch
private String type = ""; // se utiliza para chequear que todas las variables constantes y invocaciones a funcion tengan el mismo tipo.

public Parser(String source){
	code = new Code(source);
	lexic = new LexicAnalyzer(code);
	rules = new ArrayList <String>();
	tokens = lexic.getTokens();
  lexicErrors = lexic.getErrors();
  syntacticErrors = new ArrayList <String>();
	semanticErrors = new ArrayList <String>();
  listaVariablesDeclaracion = new ArrayList <String>();
}

public int yylex(){
	Token token = lexic.getToken();
  if (token!=null){
        yylval = new ParserVal(token);
		switch (token.getUse()) {
      case "<" : {return COMP_MENOR;}
      case ">" : {return COMP_MAYOR;}
			case "<=" : {return COMP_MENOR_IGUAL;}
			case ">=" : {return COMP_MAYOR_IGUAL;}
			case "<>" : {return COMP_DIFERENTE;}
      case "==" : {return COMP_IGUAL;}
      case "=" : {return '=';}
      case "." : {return '.';}
      case ":" : {return ':';}
			case "+" : {return '+';}
			case "-" : {return '-';}
      case "*" : {return '*';}
      case "/" : {return '/';}
			case "(" : {return '(';}
			case ")" : {return ')';}
			case "," : {return ',';}
			case "{" : {return '{';}
			case "}" : {return '}';}
			case "if":{return IF;}
      case "then":{return THEN;}
      case "begin":{return BEGIN;}
      case "end":{return END;}
      case "out":{return OUT;}
      case "switch":{return SWITCH;}
      case "case":{return CASE;}
      case "move":{return MOVE;}
			case "endif":{return ENDIF;}
			case "else":{return ELSE;}
			case "uinteger":{return UINT;}
      case "double" :{return DOUBLE;}
			case "cte" : {return CTE;}
			case "id": {return ID;}
			case "cadMulti":{return CADMULTI;}
			case "function":{return FUNCTION;}
			case "return":{return RETURN;}
    	}
    }
	return 0;
}
public Arbol getArbol(){
  return this.arb;
}

public void setDeclaratedType(String type){
  for (String id : listaVariablesDeclaracion){
      st.setType(id+ambito,type);
  }
  listaVariablesDeclaracion.clear();
}

public void changeIdDeclaratedVariables(){
  Iterator<String> i = listaVariablesDeclaracion.iterator();
  while (i.hasNext()) {
    String id = i.next();
    String newId = id + ambito;
    if ( (st.get(newId)!=null) && (st.getUse(newId)=="variable") ){
      i.remove();
      addSemanticError("Variable '" + id + "' redeclarada.");
      //puede quedar un id que estar redeclarado pero queda en la tabla de simbolos, lo elimino?
    }
    else{
      st.decrease(id);
      st.add(newId,"variable", null);
    }
 }
}

public void changeIdDeclaratedFunction(String functionName){
  String newFunctionName = functionName + ambito;
  if ( (st.get(newFunctionName)!=null) && (st.getUse(newFunctionName)=="funcion") ){
    addSemanticError("Función '" + functionName + "' redeclarada.");
  }
  else{
    st.decrease(functionName);
    st.add(newFunctionName,"funcion", null);
  }
}

public boolean checkVariableCorrectness(String id){
  if  (inFunctionMove){
		if ( !st.exists(id+ambito) ){addSemanticError( "Identificador '" + id + "' no declarado localmente."); return false;}
    else if ( st.exists(id+ambito) && st.getUse(id+ambito)=="funcion" ) {addSemanticError("Identificador '" + id + "' es una función. Faltan los paréntesis.");  return false;}
	}
	else {
		if ( !st.exists(id + ambito) && !st.exists(id+"@main") ) {
			addSemanticError( "Identificador '" + id + "' no declarado.");
      return false;}
    else if ( ( (st.exists(id+ambito) && st.getUse(id+ambito)=="funcion") ) || ( (st.exists(id+"@main") && st.getUse(id+"@main")=="funcion") )){
      addSemanticError("Identificador '" + id + "' es una función. Faltan los paréntesis.");
      return false;
		}
	}
  return true;
}

public boolean checkFunctionInvocationCorrectness (String id){
  String aux = id + "@main";
  if (!st.exists(aux) || !st.isFunction(aux)){
    addSemanticError( "Función '" + id + "' no declarada."); return false;
  }
  return true;
}

public void yyerror(String error){
}

public int yyparser(){
	return yyparse();
}

private String composeMsg(String error){
  String msg = "Linea "+ code.getLine() + ": " + error ;
  return msg;
}

private void addLexicError(String error) {
	lexicErrors.add(composeMsg(error));
}

private ArrayList<String> getLexicErrors() {
	return lexicErrors;
}


private void addSyntacticError(String error){
		syntacticErrors.add(composeMsg(error));
}

public ArrayList<String> getSyntacticErrors(){
        return syntacticErrors;
}

private void addSemanticError(String error){
		semanticErrors.add(composeMsg(error));
}

public ArrayList<String> getSemanticErrors(){
        return semanticErrors;
}

private void addRule(String rule) {
	rules.add(composeMsg(rule));
}

public ArrayList<String> getRules(){
	return rules;
}

public ArrayList<String> getTokens(){
  return tokens;
}


public SymbolTable getST(){
  return st;
}

public LexicAnalyzer getLexicAnalyzer(){
  return lexic;
}

public void updateFunctionUse(String id){
  st.get(id + "@main").setUse("funcion");
}

public void updateFunctionType(String id, String type){
  st.setType(id + "@main" ,type);
}


public void startFunction(String name) {
  ambito += "@" + name;
}

public void endFunction() {
  ambito = "@main";
  inFunctionMove=false;
}

	public String getFunctionName() {
		return ambito.substring(ambito.lastIndexOf("@")+1,ambito.length());
}

public String getAmbito(){
  return ambito;
}

public void checkType(String type){
//  System.out.println(this.type + " " + type);
  if (type==null);
  else if (this.type.equals("")) this.type=type;
  else if (this.type.equals("error"));
  else if (!type.equals(this.type)) this.type="error";
//  System.out.println(this.type + " " + type + "\n");
}


public void checkTypeError(String error){
  if (this.type==null) addSemanticError("Variable/s sin tipo en esta linea. Imposible chequear consistencia de tipos.");
  else if (this.type.equals("error")) addSemanticError(error);
}

public boolean thereIsSomethingWrong(){
  return !(lexicErrors.isEmpty() && syntacticErrors.isEmpty() && semanticErrors.isEmpty());
}

public void showMessages(String name){
		try
		{
			File file = new File(name+"_mensajes.txt");
			Files.deleteIfExists(file.toPath());
			PrintWriter pw = new PrintWriter(file);
			pw.println("REGLAS:");
			pw.println("------------------------------");
			pw.println("");
			for(String e:rules)
			{
				pw.println(e);
			}
			pw.println("");

			pw.println("ERRORES DEL ANALIZADOR LEXICO:");
			pw.println("------------------------------");
			pw.println("");
			if (lexicErrors.isEmpty())
				pw.println("No se encontraron errores lexicos.");
			else
				for(String e:lexicErrors)
				{
					pw.println(e);
				}
			pw.println("");

			pw.println("ERRORES SINTACTICOS:");
			pw.println("--------------------");
			pw.println("");
			if (syntacticErrors.isEmpty())
				pw.println("No se detectaron errores sintacticos.");
			else
				for(String e: syntacticErrors){
				pw.println(e);
				}
			pw.println("");

      pw.println("ERRORES SEMANTICOS:");
			pw.println("--------------------");
			pw.println("");
			if (semanticErrors.isEmpty())
				pw.println("No se detectaron errores semanticos.");
			else
				for(String e: semanticErrors){
				pw.println(e);
				}
			pw.println("");

			pw.close();
		}catch(IOException ex){ex.printStackTrace();}
	}
