//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 21 "gramatica.y"

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Iterator;
import java.util.Stack;
//#line 27 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short THEN=258;
public final static short ELSE=259;
public final static short ENDIF=260;
public final static short BEGIN=261;
public final static short END=262;
public final static short OUT=263;
public final static short SWITCH=264;
public final static short CASE=265;
public final static short FUNCTION=266;
public final static short MOVE=267;
public final static short RETURN=268;
public final static short UINT=269;
public final static short DOUBLE=270;
public final static short CADMULTI=271;
public final static short COMP_DIFERENTE=272;
public final static short COMP_MAYOR_IGUAL=273;
public final static short COMP_MENOR_IGUAL=274;
public final static short COMP_IGUAL=275;
public final static short COMP_MAYOR=276;
public final static short COMP_MENOR=277;
public final static short CTE=278;
public final static short ID=279;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    2,    2,    1,    1,    5,    5,    6,    6,    6,
    6,    7,    4,   10,   10,   10,   11,   12,   12,   13,
   13,   14,   14,   14,   14,    3,    3,    8,    8,   19,
   19,   19,   20,   20,   20,   21,   21,   21,   22,   22,
   22,   23,   23,   23,   23,   23,   23,   24,   24,   24,
   24,   15,   15,   15,   15,   27,   25,   25,   25,   25,
   28,   28,   29,   29,   29,   30,   30,   30,   30,   17,
   17,   16,   31,   31,   31,   26,   26,   32,   32,   32,
    9,    9,    9,   18,   18,   18,
};
final static short yylen[] = {                            2,
    1,    1,    1,    1,    2,    1,    1,    3,    4,    3,
    4,    0,   10,    3,    3,    3,    1,    4,    4,    1,
    3,    1,    1,    1,    2,    1,    1,    1,    2,    1,
    2,    1,    4,    4,    4,    4,    4,    4,    4,    4,
    4,    1,    1,    1,    1,    1,    1,    3,    3,    3,
    3,    9,    7,    7,    7,    0,    6,    5,    5,    5,
    1,    2,    3,    3,    3,    4,    4,    4,    4,    3,
    3,    5,    1,    1,    1,    1,    2,    3,    3,    1,
    3,    3,    1,    3,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    6,    7,    0,    0,    0,    4,
    2,    3,    0,   12,   26,   17,    0,   27,   22,   23,
   24,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    5,    0,    0,    0,    0,    0,   25,    0,    0,    0,
   10,    0,   76,    0,    0,    0,   75,    0,   74,   80,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    8,
    0,    0,    0,    0,    0,   21,    0,    0,   61,    0,
    0,   71,   70,   47,   45,   46,   42,   43,   44,    0,
    0,    0,   77,    0,    0,    0,    0,    0,    0,    0,
    0,   67,   69,   68,   66,   11,    9,   28,    0,   19,
   18,    0,    0,    0,   65,   62,    0,   63,   16,    0,
   15,   14,    0,    0,    0,    0,    0,    0,    0,   78,
   79,   72,    0,   29,    0,    0,    0,   56,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   32,
   30,    0,    0,   54,   55,    0,   53,    0,    0,    0,
   58,   59,   60,    0,    0,   31,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   57,   34,   35,   33,    0,
    0,   52,    0,   40,   41,   39,    0,    0,    0,   13,
   37,   38,   36,
};
final static short yydgoto[] = {                          8,
    9,   10,  141,   12,   13,   14,   34,   99,   46,   47,
   15,   16,   17,   18,   19,   20,   21,   22,  142,  131,
  161,  136,   81,   48,   69,   49,  139,   70,   40,   23,
   50,   51,
};
final static short yysindex[] = {                      -203,
 -234,    3,   39,  -39,    0,    0,  -51,    0, -203,    0,
    0,    0, -158,    0,    0,    0,  -16,    0,    0,    0,
    0,  -11, -106, -193,  -25, -176, -181, -229,    2,   10,
    0, -155, -192,    7, -211, -147,    0, -226, -226,  -44,
    0,  -40,    0,   95, -131,    1,    0,  110,    0,    0,
   68,  114,  118,  121,  -38,  129,   44,  129,   44,    0,
 -108, -101,  -97,  131,  133,    0,  -23,   26,    0, -104,
 -102,    0,    0,    0,    0,    0,    0,    0,    0,  139,
    2,  -37,    0,    2,    2,    2,   23, -135,    2,    2,
  135,    0,    0,    0,    0,    0,    0,    0, -146,    0,
    0,  125,  126,  -52,    0,    0,  -23,    0,    0,   44,
    0,    0,   44,   68,   68,  129,   44, -191, -191,    0,
    0,    0,  147,    0, -113, -113, -113,    0, -151, -151,
  -72, -187,    2, -151, -151,  143,  144,  145, -113,    0,
    0, -137, -172,    0,    0, -112,    0,  127, -126, -163,
    0,    0,    0,  146,  148,    0,  149,  150, -151, -151,
  -67,  151,  152,  153,  154,    0,    0,    0,    0, -123,
 -160,    0,   76,    0,    0,    0,  156,  157,  160,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,   -7,    0,  207,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -34,    0,    0,    0,    0,    0,    0,
  -27,    0,    0,    0,    0,    0,  162,  163,  164,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,  170,
    0,    0,  172,  -12,   -5,  173,  175,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  211,   15,    0,  186,    0,    0,    0,   -4,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -78,  104,
    0,  -64,  178,    0,   75,  -19,    0,  187,    0,    0,
   84,   90,
};
final static int YYTABLESIZE=304;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         80,
   28,   73,   95,  112,   64,  128,   73,   73,   73,   30,
   73,   73,   73,   83,   11,   83,   39,   83,   83,   45,
  105,   45,  108,   11,   57,   59,   54,   36,   81,   67,
   81,   24,   81,   81,   37,   82,   20,   82,   68,   82,
   82,   35,   25,   85,   64,   86,   45,  102,  104,   55,
   20,  143,    1,    2,   45,  149,  150,    5,    6,    3,
    4,  137,  138,   61,  129,    5,    6,   45,  145,  130,
   45,  146,  147,   62,  154,    7,  110,   98,   26,  113,
  170,  171,  117,  157,    2,   41,   85,  102,   86,  158,
    3,    4,  164,    2,   52,  178,    2,   53,  165,    3,
    4,  179,    3,    4,  140,    2,    7,   32,   33,   89,
    2,    3,    4,  124,   90,    7,    3,    4,    7,    2,
  118,  123,  119,   60,  155,    3,    4,    7,  148,   63,
    2,   66,    7,    2,   82,  163,    3,    4,  177,    3,
    4,    7,  134,  159,  106,  106,   83,  135,  160,   38,
   88,   67,    7,  107,   91,    7,  156,  156,   92,    2,
   68,   93,   68,  156,  156,    3,    4,  162,   80,   85,
   96,   86,  120,  121,  114,  115,  100,   97,  101,  109,
  122,    7,  125,  126,  156,  156,  133,  144,  151,  152,
  153,  166,  172,  167,  168,  169,  173,  174,  175,  176,
  180,  181,  182,  127,   29,  183,    1,   85,   86,   84,
   51,   72,   50,   49,   64,   48,   27,   94,  111,   31,
   65,   73,  132,   87,    0,   71,    0,    0,   83,    0,
   42,   74,   75,   76,   77,   78,   79,   73,   73,   73,
   73,   73,   73,   81,   83,   83,   83,   83,   83,   83,
   82,    0,   43,   44,   43,    0,   84,   56,    0,   81,
   81,   81,   81,   81,   81,   58,   82,   82,   82,   82,
   82,   82,   74,   75,   76,   77,   78,   79,  116,   43,
   44,  103,    0,    0,    0,    0,    0,   43,   44,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   43,   44,    0,   43,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   40,   46,   41,   41,   46,   58,   41,   42,   43,   61,
   45,   46,   47,   41,    0,   43,  123,   45,   46,   45,
  125,   45,  125,    9,   29,   30,  256,   44,   41,  256,
   43,  266,   45,   46,   46,   41,   44,   43,  265,   45,
   46,   58,   40,   43,  256,   45,   45,   67,   68,  279,
   58,  130,  256,  257,   45,  134,  135,  269,  270,  263,
  264,  126,  127,  256,  256,  269,  270,   45,  256,  261,
   45,  259,  260,  266,  139,  279,   81,   63,   40,   84,
  159,  160,   87,  256,  257,  279,   43,  107,   45,  262,
  263,  264,  256,  257,  271,  256,  257,  279,  262,  263,
  264,  262,  263,  264,  256,  257,  279,  266,  267,   42,
  257,  263,  264,   99,   47,  279,  263,  264,  279,  257,
  256,  268,  258,  279,  262,  263,  264,  279,  133,  123,
  257,  279,  279,  257,   40,  262,  263,  264,  262,  263,
  264,  279,  256,  256,   70,   71,  278,  261,  261,  256,
   41,  256,  279,  256,   41,  279,  142,  143,   41,  257,
  265,   41,  265,  149,  150,  263,  264,   41,   40,   43,
  279,   45,   89,   90,   85,   86,   46,  279,   46,   41,
   46,  279,   58,   58,  170,  171,   40,  260,   46,   46,
   46,   46,  260,   46,   46,   46,   46,   46,   46,   46,
  125,   46,   46,  256,  256,   46,    0,   46,   46,   46,
   41,  256,   41,   41,  256,   41,  256,  256,  256,    9,
   35,  256,  119,   46,   -1,   39,   -1,   -1,  256,   -1,
  256,  272,  273,  274,  275,  276,  277,  272,  273,  274,
  275,  276,  277,  256,  272,  273,  274,  275,  276,  277,
  256,   -1,  278,  279,  278,   -1,  256,  256,   -1,  272,
  273,  274,  275,  276,  277,  256,  272,  273,  274,  275,
  276,  277,  272,  273,  274,  275,  276,  277,  256,  278,
  279,  256,   -1,   -1,   -1,   -1,   -1,  278,  279,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  278,  279,   -1,  278,
};
}
final static short YYFINAL=8;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",null,
null,"'='",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IF","THEN","ELSE","ENDIF","BEGIN","END",
"OUT","SWITCH","CASE","FUNCTION","MOVE","RETURN","UINT","DOUBLE","CADMULTI",
"COMP_DIFERENTE","COMP_MAYOR_IGUAL","COMP_MENOR_IGUAL","COMP_IGUAL",
"COMP_MAYOR","COMP_MENOR","CTE","ID",
};
final static String yyrule[] = {
"$accept : programa",
"programa : contenidoPrograma",
"elementoPrograma : sentencia",
"elementoPrograma : funcion",
"contenidoPrograma : elementoPrograma",
"contenidoPrograma : contenidoPrograma elementoPrograma",
"tipo : UINT",
"tipo : DOUBLE",
"headerFUNCTION : tipo FUNCTION ID",
"headerFUNCTION : tipo MOVE FUNCTION ID",
"headerFUNCTION : error FUNCTION ID",
"headerFUNCTION : tipo MOVE error ID",
"$$1 :",
"funcion : headerFUNCTION $$1 '{' conjuntoSentencias RETURN '(' expresion ')' '.' '}'",
"invokeFUNCTION : ID '(' ')'",
"invokeFUNCTION : ID '(' error",
"invokeFUNCTION : error '(' ')'",
"sentenciaDeclarativa : declaracionVariables",
"declaracionVariables : listaVariables ':' tipo '.'",
"declaracionVariables : listaVariables ':' error '.'",
"listaVariables : ID",
"listaVariables : listaVariables ',' ID",
"sentenciaEjecutable : sentenciaIF",
"sentenciaEjecutable : sentenciaSalida",
"sentenciaEjecutable : sentenciaSWITCH",
"sentenciaEjecutable : asignacion '.'",
"sentencia : sentenciaDeclarativa",
"sentencia : sentenciaEjecutable",
"conjuntoSentencias : sentencia",
"conjuntoSentencias : conjuntoSentencias sentencia",
"conjuntoSentencia : sentencia",
"conjuntoSentencia : conjuntoSentencia sentencia",
"conjuntoSentencia : error",
"bloqueSentenciaThen : BEGIN conjuntoSentencia END '.'",
"bloqueSentenciaThen : error conjuntoSentencia END '.'",
"bloqueSentenciaThen : BEGIN conjuntoSentencia error '.'",
"bloqueSentenciaElse : BEGIN conjuntoSentencia END '.'",
"bloqueSentenciaElse : error conjuntoSentencia END '.'",
"bloqueSentenciaElse : BEGIN conjuntoSentencia error '.'",
"bloqueSentencia : BEGIN conjuntoSentencia END '.'",
"bloqueSentencia : error conjuntoSentencia END '.'",
"bloqueSentencia : BEGIN conjuntoSentencia error '.'",
"comparador : COMP_IGUAL",
"comparador : COMP_MAYOR",
"comparador : COMP_MENOR",
"comparador : COMP_MAYOR_IGUAL",
"comparador : COMP_MENOR_IGUAL",
"comparador : COMP_DIFERENTE",
"condicion : expresion comparador expresion",
"condicion : expresion comparador error",
"condicion : expresion error expresion",
"condicion : error comparador expresion",
"sentenciaIF : IF '(' condicion ')' THEN bloqueSentenciaThen ELSE bloqueSentenciaElse ENDIF",
"sentenciaIF : IF '(' condicion ')' THEN bloqueSentenciaThen ENDIF",
"sentenciaIF : IF '(' condicion ')' error bloqueSentenciaThen ENDIF",
"sentenciaIF : IF '(' condicion ')' THEN bloqueSentenciaThen error",
"$$2 :",
"lineaCASE : CASE cte ':' $$2 bloqueSentencia '.'",
"lineaCASE : error cte ':' bloqueSentencia '.'",
"lineaCASE : CASE error ':' bloqueSentencia '.'",
"lineaCASE : CASE cte error bloqueSentencia '.'",
"bloqueCASE : lineaCASE",
"bloqueCASE : bloqueCASE lineaCASE",
"cuerpoSwitch : '{' bloqueCASE '}'",
"cuerpoSwitch : '{' bloqueCASE error",
"cuerpoSwitch : error bloqueCASE '}'",
"encabezadoSWITCH : SWITCH '(' ID ')'",
"encabezadoSWITCH : SWITCH error ID ')'",
"encabezadoSWITCH : SWITCH '(' ID error",
"encabezadoSWITCH : SWITCH '(' error ')'",
"sentenciaSWITCH : encabezadoSWITCH cuerpoSwitch '.'",
"sentenciaSWITCH : encabezadoSWITCH cuerpoSwitch error",
"sentenciaSalida : OUT '(' CADMULTI ')' '.'",
"factor : ID",
"factor : cte",
"factor : invokeFUNCTION",
"cte : CTE",
"cte : '-' CTE",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"asignacion : ID '=' expresion",
"asignacion : ID error expresion",
"asignacion : ID '=' error",
};

//#line 340 "gramatica.y"
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
//#line 697 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 41 "gramatica.y"
{addRule( "Programa leido correctamente."); System.out.println("Programa leido correctamente.");
                                  arb = new Node("PROGRAM",(Arbol)val_peek(0).obj,null);
                                  st.update();
                                  }
break;
case 2:
//#line 47 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 3:
//#line 48 "gramatica.y"
{addRule( "Funcion."); yyval = val_peek(0); }
break;
case 4:
//#line 51 "gramatica.y"
{  yyval = val_peek(0);  }
break;
case 5:
//#line 52 "gramatica.y"
{  yyval = new ParserVal(new Node("LS",(Arbol)val_peek(1).obj,(Arbol)val_peek(0).obj)); }
break;
case 8:
//#line 58 "gramatica.y"
{
                                    yyval = val_peek(0);
                                    lastType = ((Token)val_peek(2).obj).getId();

                                  }
break;
case 9:
//#line 64 "gramatica.y"
{
                                    inFunctionMove=true;
                                    yyval=val_peek(0);
                                    lastType = ((Token)val_peek(3).obj).getId();
                                  }
break;
case 10:
//#line 73 "gramatica.y"
{
              addSyntacticError("Tipo mal definido.");
              yyval.obj=val_peek(0).obj;
              lastType = ((Token)val_peek(2).obj).getId();
            }
break;
case 11:
//#line 81 "gramatica.y"
{
                inFunctionMove=true;
              addSyntacticError("Se espera la palabra reservada FUNCTION luego de MOVE");
              yyval.obj=val_peek(0).obj;
              lastType = ((Token)val_peek(3).obj).getId();
            }
break;
case 12:
//#line 89 "gramatica.y"
{
                          String id = ((Token)val_peek(0).obj).getId();
                          changeIdDeclaratedFunction( id );
                          updateFunctionUse( id );
                          updateFunctionType(id , lastType);
                          ((Token)val_peek(0).obj).setPointer(st.get( id,"@main"));
                          startFunction(id);
                         }
break;
case 13:
//#line 96 "gramatica.y"
{
                                                                        checkTypeError("Todos los operandos de la expresión a retornar deben ser del mismo tipo.");
                                                                        if (!this.type.equals("error") && !this.type.equals(lastType))
                                                                            addSemanticError("El tipo de retorno debe ser igual al tipo de la expresión.");
                                                                        this.type="";
                                                                        endFunction();
                                                                        String id = ((Token)val_peek(9).obj).getId();
                                                                        Node nodereturn = new Node("RETURN",(Arbol)val_peek(3).obj,null);
                                                                        Node cuerpo = new Node("CUERPO_FUNCION", (Arbol)val_peek(6).obj, nodereturn);
                                                                        Node func = new Node("FUNCION",new NodeHoja( (Token)val_peek(9).obj , st.getVariableScope(id,ambito)),cuerpo);
                                                                        yyval = new ParserVal(func);
                                                                      }
break;
case 14:
//#line 110 "gramatica.y"
{ if (checkFunctionInvocationCorrectness(((Token)val_peek(2).obj).getId())) {checkType((String)st.getType( ((Token)val_peek(2).obj).getId(), "@main")); yyval = new ParserVal(new NodeHoja( ((Token)val_peek(2).obj).getId()+"@main", st.getType( ((Token)val_peek(2).obj).getId()+"@main"), "invocacion" ));}
                              else yyval = new ParserVal(new NodeHoja( ((Token)val_peek(2).obj).getId(), "", ""));}
break;
case 15:
//#line 112 "gramatica.y"
{addSyntacticError("Falta paréntesis en la invocación a función.");
                              yyval = new ParserVal(new NodeHoja( ((Token)val_peek(2).obj).getId(), "", ""));}
break;
case 16:
//#line 114 "gramatica.y"
{addSyntacticError("Identificador mal definido en la invocación a función.");
                              yyval = new ParserVal(new NodeHoja( "error", "", ""));}
break;
case 17:
//#line 119 "gramatica.y"
{addRule("sentencia Declarativa."); }
break;
case 18:
//#line 124 "gramatica.y"
{
                        changeIdDeclaratedVariables();
                        setDeclaratedType(((Token)val_peek(1).obj).getId());

                      }
break;
case 19:
//#line 129 "gramatica.y"
{addSyntacticError("Tipo inválido."); listaVariablesDeclaracion.clear();
                                                      st.deleteReg(((Token)val_peek(1).obj).getId());
                                                    }
break;
case 20:
//#line 135 "gramatica.y"
{
                  listaVariablesDeclaracion.add(((Token)val_peek(0).obj).getId());
                }
break;
case 21:
//#line 138 "gramatica.y"
{listaVariablesDeclaracion.add(((Token)val_peek(0).obj).getId());}
break;
case 22:
//#line 141 "gramatica.y"
{addRule( "sentencia IF."); yyval.obj= val_peek(0).obj; }
break;
case 23:
//#line 142 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 24:
//#line 143 "gramatica.y"
{addRule( "sentencia SWITCH CASE"); yyval = val_peek(0);  }
break;
case 25:
//#line 144 "gramatica.y"
{ addRule( "Asignación.");  checkTypeError("Tipos diferentes en asignación."); this.type=""; yyval = val_peek(1); }
break;
case 26:
//#line 147 "gramatica.y"
{yyval = new ParserVal(new Node("DECLARACION",null,null)); }
break;
case 27:
//#line 148 "gramatica.y"
{ yyval = val_peek(0);  }
break;
case 28:
//#line 151 "gramatica.y"
{yyval = val_peek(0);}
break;
case 29:
//#line 152 "gramatica.y"
{   Node node = new Node ("LS",(Arbol)val_peek(1).obj,(Arbol)val_peek(0).obj) ;
                                                      yyval = new ParserVal(node); }
break;
case 30:
//#line 156 "gramatica.y"
{yyval = val_peek(0); }
break;
case 31:
//#line 157 "gramatica.y"
{ yyval = new ParserVal(new Node ("LS",(Arbol)val_peek(1).obj,(Arbol)val_peek(0).obj));}
break;
case 32:
//#line 158 "gramatica.y"
{addSyntacticError("Conjunto de sentencias ejecutables mal definida."); yyval = new ParserVal(new NodeHoja ("LS (error)","",""));}
break;
case 33:
//#line 162 "gramatica.y"
{
                                                                  Node node =  new Node("BLOQUE_THEN",(Arbol)val_peek(2).obj,null);
                                                                  yyval= new ParserVal(node);                                                     }
break;
case 34:
//#line 165 "gramatica.y"
{addSyntacticError("Falta BEGIN.");
                                                              Node node =  new Node("BLOQUE_THEN",(Arbol)val_peek(2).obj,null);
                                                              yyval= new ParserVal(node);}
break;
case 35:
//#line 168 "gramatica.y"
{addSyntacticError("Falta END.");
                                                                Node node =  new Node("BLOQUE_THEN",(Arbol)val_peek(2).obj,null);
                                                                yyval= new ParserVal(node);}
break;
case 36:
//#line 173 "gramatica.y"
{yyval = new ParserVal(new Node("BLOQUE_ELSE",(Arbol)val_peek(2).obj,null));}
break;
case 37:
//#line 174 "gramatica.y"
{addSyntacticError("Falta BEGIN."); yyval = new ParserVal(new Node("BLOQUE_ELSE",(Arbol)val_peek(2).obj,null));}
break;
case 38:
//#line 175 "gramatica.y"
{addSyntacticError("Falta END."); yyval = new ParserVal(new Node("BLOQUE_ELSE",(Arbol)val_peek(2).obj,null));}
break;
case 39:
//#line 178 "gramatica.y"
{  Node node =   new Node("BLOQUE",(Arbol)val_peek(2).obj,null);
                                                                  yyval= new ParserVal(node);  }
break;
case 40:
//#line 180 "gramatica.y"
{addSyntacticError("Falta BEGIN.");Node node =   new Node("BLOQUE",(Arbol)val_peek(2).obj,null);
                                                                                  yyval= new ParserVal(node); }
break;
case 41:
//#line 182 "gramatica.y"
{addSyntacticError("Falta END.");Node node =   new Node("BLOQUE",(Arbol)val_peek(2).obj,null);
                                                                                  yyval= new ParserVal(node); }
break;
case 42:
//#line 186 "gramatica.y"
{yyval.obj = "==";}
break;
case 43:
//#line 187 "gramatica.y"
{yyval.obj = ">";}
break;
case 44:
//#line 188 "gramatica.y"
{yyval.obj = "<";}
break;
case 45:
//#line 189 "gramatica.y"
{yyval.obj = ">=";}
break;
case 46:
//#line 190 "gramatica.y"
{yyval.obj = "<=";}
break;
case 47:
//#line 191 "gramatica.y"
{yyval.obj = "<>";}
break;
case 48:
//#line 196 "gramatica.y"
{checkTypeError("Tipo inconcistente en la condición del IF.");this.type=""; Node node1 = new Node((String)val_peek(1).obj,(Arbol)val_peek(2).obj,(Arbol)val_peek(0).obj);
                                                                                                                      Node node2 = new Node("CONDICION_IF",node1,null);
                                                                                                                      yyval = new ParserVal(node2);}
break;
case 49:
//#line 199 "gramatica.y"
{ addSyntacticError("Se espera una expresion luego del operador de comparacion.");
                                                                                                                                NodeHoja node = new NodeHoja("CONDICION_IF (error)",null,null);
                                                                                                                                yyval = new ParserVal(node);}
break;
case 50:
//#line 202 "gramatica.y"
{ addSyntacticError("Se espera un comparador."); checkTypeError("Tipo inconcistente en la condición del IF.");
                                                                                        NodeHoja node = new NodeHoja("CONDICION_IF (error)","","");
                                                                                        yyval = new ParserVal(node);}
break;
case 51:
//#line 205 "gramatica.y"
{ addSyntacticError("Se espera una expresion antes del operador de comparacion.");
                                                                                        NodeHoja node = new NodeHoja("CONDICION_IF (error)","","");
                                                                                        yyval = new ParserVal(node); }
break;
case 52:
//#line 211 "gramatica.y"
{
                Node cuerpo = new Node("CUERPO_IF",(Arbol)val_peek(3).obj,(Arbol)val_peek(1).obj);
                Node fi = new Node("IF",(Arbol)val_peek(6).obj,cuerpo);
                yyval = new ParserVal(fi);
             }
break;
case 53:
//#line 217 "gramatica.y"
{ Node cuerpo = new Node("CUERPO_IF",(Arbol)val_peek(1).obj,null);
                                  Node node = new Node("IF",(Arbol)val_peek(4).obj,cuerpo);
                                    yyval = new ParserVal(node);
                                  }
break;
case 54:
//#line 221 "gramatica.y"
{ addSyntacticError("Falta THEN.");
                                                                    Node node = new Node("IF",(Arbol)val_peek(4).obj,(Arbol)val_peek(1).obj);
                                                                      yyval = new ParserVal(node);}
break;
case 55:
//#line 224 "gramatica.y"
{ addSyntacticError("falta ENDIF");
                                                                    Node node = new Node("IF",(Arbol)val_peek(4).obj,(Arbol)val_peek(1).obj);
                                                                    yyval = new ParserVal(node);}
break;
case 56:
//#line 231 "gramatica.y"
{this.type="";}
break;
case 57:
//#line 231 "gramatica.y"
{
                                                        if ( !tipoCase.peek().equals("") && !((NodeHoja)val_peek(4).obj).getType().equals(tipoCase.peek())){
                                                          addSemanticError("La constante debe ser del tipo '" + tipoCase.peek() + "'.");
                                                        }
                                                    else{
                                                      Node node = new Node("CASE",(Arbol)val_peek(4).obj,(Arbol)val_peek(1).obj);
                                                      yyval  = new ParserVal(node);
                                                  }
                                                  }
break;
case 58:
//#line 241 "gramatica.y"
{addSyntacticError("Se espera palabra reservada CASE.");}
break;
case 59:
//#line 242 "gramatica.y"
{addSyntacticError("Se espera una constante luego de CASE."); }
break;
case 60:
//#line 243 "gramatica.y"
{addSyntacticError("Se espera ':' luego de la constante.");}
break;
case 61:
//#line 246 "gramatica.y"
{
                                  yyval = val_peek(0);
                                        }
break;
case 62:
//#line 249 "gramatica.y"
{ yyval = new ParserVal ( new Node( "CUERPO_CASE", (Arbol)val_peek(1).obj,(Arbol)val_peek(0).obj)); }
break;
case 63:
//#line 252 "gramatica.y"
{yyval = val_peek(1);}
break;
case 64:
//#line 253 "gramatica.y"
{addSyntacticError("Falta llave inicial del bloqueCASE."); }
break;
case 65:
//#line 254 "gramatica.y"
{addSyntacticError("Falta llave inicial del bloqueCASE."); }
break;
case 66:
//#line 256 "gramatica.y"
{
                                            String id = ((Token)val_peek(1).obj).getId();
                                            if ( checkVariableCorrectness(id) ){
                                              tipoCase.push(st.getType( id, ambito));
                                              ((Token)val_peek(1).obj).setPointer(st.get(id,ambito));
                                            }
                                            else {
                                              tipoCase.push("error");
                                            }
                                            yyval = new ParserVal(new NodeHoja( (Token)val_peek(1).obj, st.getVariableScope(id,ambito)) );
                                            }
break;
case 67:
//#line 268 "gramatica.y"
{addSyntacticError("Falta paréntesis inicial del identificador."); }
break;
case 68:
//#line 269 "gramatica.y"
{addSyntacticError("Falta paréntesis final del identificador."); }
break;
case 69:
//#line 270 "gramatica.y"
{addSyntacticError("Se espera un identificador entre paréntesis."); }
break;
case 70:
//#line 274 "gramatica.y"
{
                                                              Node swich = new Node("SWITCH",(Arbol)val_peek(2).obj,(Arbol)val_peek(1).obj);
                                                              tipoCase.pop();
                                                              yyval = new ParserVal (swich);}
break;
case 71:
//#line 278 "gramatica.y"
{addSyntacticError("Falta '.' final en la sentencia switch."); }
break;
case 72:
//#line 280 "gramatica.y"
{addRule( "sentencia OUT"); yyval =  new ParserVal (new Node( "OUT", new NodeHoja( (Token)val_peek(2).obj, "" ) , null));}
break;
case 73:
//#line 285 "gramatica.y"
{ String id = ((Token)val_peek(0).obj).getId();
              if (checkVariableCorrectness(id)){
                checkType(st.getType(id, ambito));
                ((Token)val_peek(0).obj).setPointer(st.get(id,ambito));
                }
                yyval = new ParserVal(new NodeHoja((Token)val_peek(0).obj,st.getVariableScope(id,ambito)));
              }
break;
case 74:
//#line 292 "gramatica.y"
{
            yyval = val_peek(0);}
break;
case 75:
//#line 294 "gramatica.y"
{ yyval = new ParserVal(new Node("INVOCACION",(Arbol)val_peek(0).obj,null)); }
break;
case 76:
//#line 299 "gramatica.y"
{
            checkType( st.getType( ((Token)val_peek(0).obj).getId() ) );
            NodeHoja node =  new NodeHoja((Token)val_peek(0).obj,"");
            yyval = new ParserVal(node);
            }
break;
case 77:
//#line 305 "gramatica.y"
{ checkType( st.getType( ((Token)val_peek(0).obj).getId() ) );
                st.setNegativeDouble( (Token)val_peek(0).obj );
                String negative = "-" + ((Token)val_peek(0).obj).getId();
                NodeHoja node =  new NodeHoja(negative, st.get(negative).getType(), st.get(negative).getUse());
                yyval = new ParserVal(node); }
break;
case 78:
//#line 312 "gramatica.y"
{yyval = new ParserVal(new Node("*",(Arbol)val_peek(2).obj,(Arbol)val_peek(0).obj));}
break;
case 79:
//#line 313 "gramatica.y"
{yyval = new ParserVal(new Node("/",(Arbol)val_peek(2).obj,(Arbol)val_peek(0).obj));}
break;
case 80:
//#line 314 "gramatica.y"
{yyval = val_peek(0);}
break;
case 81:
//#line 317 "gramatica.y"
{ yyval = new ParserVal(new Node("+", (Arbol)val_peek(2).obj,(Arbol)val_peek(0).obj));}
break;
case 82:
//#line 318 "gramatica.y"
{ yyval = new ParserVal(new Node("-",(Arbol)val_peek(2).obj,(Arbol)val_peek(0).obj));}
break;
case 83:
//#line 319 "gramatica.y"
{ yyval= val_peek(0);}
break;
case 84:
//#line 323 "gramatica.y"
{
                              String id = ((Token)val_peek(2).obj).getId();
                              if (checkVariableCorrectness(id)) {
                                  checkType( st.getType( id , ambito) );
                                  ((Token)val_peek(2).obj).setPointer(st.get(id,ambito));
                              }
                                yyval  =  new ParserVal (new Node("=",new NodeHoja((Token)val_peek(2).obj, st.getVariableScope(id,ambito)),(Arbol)val_peek(0).obj));
                              }
break;
case 85:
//#line 331 "gramatica.y"
{  addSyntacticError("Se espera '=' en la asignacion.");
                                  yyval  =  new ParserVal (new Node("=",new NodeHoja((Token)val_peek(2).obj, ambito),(Arbol)val_peek(0).obj));}
break;
case 86:
//#line 333 "gramatica.y"
{ addSyntacticError("Expresion invalida en asignación.");
                          yyval  =  new ParserVal (new Node("asignacion(error)",null,null)); }
break;
//#line 1307 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
