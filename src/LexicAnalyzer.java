import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;

public class LexicAnalyzer {
    
    //variables
	private String buffer;
	private ArrayList<String> lPR = new ArrayList<String>();
    private ArrayList<String> errors = new ArrayList<String>();
    private ArrayList<String> tokens = new ArrayList<String>();
    private Token tokenActual;
    private boolean reset;
    private static int F = 20;
    SymbolTable st = new SymbolTable();
	private Code f;

	private SemanticAction sa1=new AS1();
    private SemanticAction sa2=new AS2();
    private SemanticAction sa3=new AS3();
    private SemanticAction sa4=new AS4();
    private SemanticAction sa5=new AS5();
    private SemanticAction sa6=new AS6();
    private SemanticAction sa7=new AS7();
    private SemanticAction sa8=new AS8();
    private SemanticAction asc=new ASC();
    private SemanticAction sas=new ASS();
    private SemanticAction eci=new ECI();
    private SemanticAction eed=new EED();
    
    private int[][] mTE={ //Matriz de transición de estados.
    		
    	      //L  d  ,  +  -  *  /  <  >  =   "   [   ]   e   .   :  (    )  {   }   _  /n  /t  ' ' otro
    	      //0  1  2  3  4  5  6  7  8  9  10  11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27  28  29
    	       {1, 2,17, F, F, F, F, 8, 9, 10,13, 11, F,  1,  F,  F,  F,  F,  F,  F,  1 , 0,  0,  0, -1},//0
    	       {1, 1, F, F, F, F, F, F, F, F , F, F , F,  1,  F,  F,  F,  F,  F,  F,  1 , F,  F,  F, F},//1
    	       {F, 2, 3, F, F, F, F, F, F, F, F,  F,  F,  F,  F,  F,  F,  F,  F,  F ,  F, F,  F,  F, F},//2
    	       {F, 4, F, F, F, F, F, F, F, F, F,  F,  F,  5,  F,  F,  F,  F,  F,  F ,  F, F,  F,  F, F},//3
    	       {F, 4, F, F, F, F, F, F, F, F, F,  F,  F,  5,  F,  F,  F,  F,  F,  F,   F, F,  F,  F, F},//4
    	       {-1,6,-1, 6, 6,-1,-1,-1,-1,-1,-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  -1,-1, -1, -1, -1},//5
    	       {F, 6, F, F, F, F, F, F, F, F, F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F , F,  F,  F, F},//6
    	       {F, F, F, F, F, F, F, F, F, F, F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F , F,  F,  F,  F},//7
    	       {F, F, F, F, F, F, F, F, 7, 7, F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F , 8,  8,  8,  F },//8
    	       {F, F, F, F, F, F, F, F, F,12, F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F , 9,  9,  9,  F },//9
    	       {F, F, F, F, F, F, F, F, F,16, F,  F,  F,  F,  F,  F,  F,  F,  F,  F,  F , 10,  10,10, F },//10
    	       {11,11,11,11,11,11,11,11,11,11,11,11,  0, 11, 11, 11, 11, 11, 11, 11,  11,11, 11, 11, 11},//11
    	       {F ,F , F, F, F, F, F, F, F, F, F, F,  F,  F,  F,  F,  F,  F,  F,  F,  F , F,  F,  F, F },//12
    	       {13,13,13,13,13,13,13,13,13,13, F,13, 13, 13, 14, 13, 13, 13, 13, 13,  13, 13, 13, 13,13},//13
    	       {13,13,13,13,13,13,13,13,13,13, F,13, 13, 13, 15, 13, 13, 13, 13, 13,  13,13, 13, 13, 13},//14
    	       {13,13,13,13,13,13,13,13,13,13, F,13, 13, 13, 13, 13, 13, 13, 13, 13,  13,13, 13, 13, 13},//15                                                                                                                                                                                                                                                                                                                                                                                                                                            
    	       {F ,F , F, F, F, F, F, F, F, F, F, F,  F,  F,  F,  F,  F,  F,  F,  F,  F , F,  F,  F, F },//16
    	       {F ,4 , F, F, F, F, F, F, F, F, F, F,  F,  F,  F,  F,  F,  F,  F,  F,   F, F,  F,  F, F },//17
    	       
    	      };
    	      
    	      
    	      
    	      private SemanticAction[][] mSA= {
    	    		  
    	        //L   d   ,   +   -   *   /   <   >   =   "  [    ]   e   .    :  (    )  {    }  _  /n  /t  ' ' otro
    	        //0   1   2   3   4   5   6   7   8   9  10  11  12  13  14  15  16  17  18  19  20  21  22  23  24   25 
    	        {sa1,sa1,sa1,sa5,sa5,sa5,sa5,sa1,sa1,sa1,sa1,sa1,sa1,sa1,sa5,sa5,sa5,sa5,sa5,sa5,sa1,asc,asc,sa1,eci},//0 // pedirle a coop que me pase as6
    	        {sa2,sa2,sa3,sa3,sa3,sa3,sa3,sa3,sa3,sa3,sa3,sa3,sa3,sa2,sa3,sa3,sa3,sa3,sa3,sa3,sa2,sa3,sa3,sa3,sa3},//1
    	        {sa4,sa2,sa2,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4,sa4},//2
    	        {sa7,sa2,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa2,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7},//3
    	        {sa7,sa2,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa2,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7},//4
    	        {eed,sa2,eed,sa2,sa2,eed,eed,eed,eed,eed,eed,eed,eed,eed,eed,eed,eed,eed,eed,eed,eed,eed,eed,eed,eed},//5
    	        {sa7,sa2,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7,sa7},//6
    	        {sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6},//7
    	        {sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa2,sa2,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,asc,asc,asc,sa6},//8
    	        {sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa2,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,asc,asc,asc,sa6},//9
    	        {sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa2,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,asc,asc,asc,sa6},//10
    	        {asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,asc,sa6},//11
    	        {sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6},//12
    	        {sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa8,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sas,asc,sa2,sa2},//13
    	        {sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa8,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sas,asc,sa2,sa2},//14
    	        {sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa2,sa8,sa2,sa2,sa2,sas,sa2,sa2,sa2,sa2,sa2,sa2,sas,asc,sa2,sa2},//15
    	        {sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sas,asc,sa6,sa6},//16
    	        {sa6,sa2,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sa6,sas,asc,sa6,sa6},//17
    	      };
    	    
        
      public LexicAnalyzer(Code f){
		buffer = new String();
		this.f=f;
		//INICIALIZO LISTA DE PALABRAS RESERVADAS
		lPR.add("if");
		lPR.add("then");
		lPR.add("else");
		lPR.add("endif");
		lPR.add("begin");
		lPR.add("end");
		lPR.add("out");
		lPR.add("switch");
		lPR.add("case");
		lPR.add("function");
		lPR.add("move");
		lPR.add("return");
		lPR.add("uinteger");
		lPR.add("double");
		reset = false;
	}
	
	//Setea el buffer
	public void setBuffer(String buffer){
		this.buffer=buffer;
	}
	
	//Devuelve el Buffer
	public String getBuffer(){
		return buffer;
	}
	
	//Devuelve la fuente
	public Code getCode(){
		return f;
	}
	
	//Devuelve lista Palabras Reservadas
	public ArrayList<String> getLPR(){
		return lPR;
	}
	
	//Devuelve la lista de errores
	public ArrayList<String> getErrors(){
		return errors;
	}
	
	//Devuelve la lista de Tokens
	public ArrayList<String> getTokens(){
		return tokens;
	}
	
	//Devuelve la tabla de simbolos
	public SymbolTable getTable(){
		return st;
	}
	
    public void setToken(String lexema, String use, RecordTS puntero ){
    	tokenActual = new Token(lexema,use,puntero);
    }
    
    public void addError(String error){
 	   errors.add(error);
    }
    
    //Resetea boolean
    public void reset(){
    	reset = true;
    }
    
	//Devuelve token
    public Token getToken(){
    	tokenActual = null;
        int estado = 0; //Estado inicial.
        while ((estado != F) && (!f.EOF())){ 
            char c = f.getCharacter();
            SemanticAction sA = mSA[estado][f.getID(c)]; //Acción semantica a realizar [Estado][Simbolo
            if (sA.ejecutar(c, this) == 0){ //0 consume.
            	f.increasePosition();
            }
            if (!reset){
  	            estado = mTE [estado][f.getID(c)]; //Dame el nuevo estado
	            if (estado == F){
	                return tokenActual;
	            }
            }
            else { //resetea buffer despues de haber reconocido un error
            	estado = 0;//lo resetea cuando escuentra un error y sigue compilando. NO LE CABE UNA
            	buffer = "";
            	reset = false;
            }
        }
        
        return null;    
    }
    
    public void updateUnique(String lexema, String use){
    	setToken(lexema, use, null);
    	tokens.add(String.format("%-20s%s",tokenActual.getId(),tokenActual.getUse()));
    }
    
    public void update(String lexema, String use, String type) {
    	setToken(lexema, use, getTable().add(lexema,use,type));
    	tokens.add(String.format("%-20s%s",tokenActual.getId(),tokenActual.getUse()));
    }
    
	public void showTokens(String nombre){
		try
		{
			FileWriter file = new FileWriter(new File(nombre+"_Tokens.txt"));
			PrintWriter pw = new PrintWriter(file);
			pw.println("TOKENS RECONOCIDOS POR EL ANALIZADOR LEXICO:");
			pw.println("--------------------------------------------");
			pw.println("");
			pw.printf("%-20s%s","Lexema:","Uso:");
			pw.println("");
			pw.println("");
			for(String e:tokens)
				pw.println(e);
			file.close();
		}catch(IOException ex){ex.printStackTrace();}
		
	}
}
