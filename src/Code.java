public class Code {
	
	private String entrada;
	private int posicion = 0;
	public int lineaActual = 1;
	
	public Code(String e){
		entrada = e + '\n';
	}
	
	public int getLine(){
		return lineaActual;
	}
	
	public boolean EOF(){
		return 	posicion >= entrada.length();
	}
	
	private boolean isUpper(char c){
		return (c >= 65 && c <= 90);
	}
	
	private boolean isLetter(char c){
		return (c >= 97 && c <= 122);
	
	}
	
	public char toLower(char c) {
		return Character.toLowerCase(c);
	}
	
    private boolean isDigit(char c) {
        return (c >= 48  && c <= 57);
    }
	
	public int getID(char c) {

		//regonized by the sintantic party
		if (c=='e') 
			return 13;
        if (isLetter(c))  
        	return 0;
		if (isDigit(c)) 
			return 1;
		switch (c) {
		case ',' : {return 2;}
              case '+' : {return 3;}
              case '-' : {return 4;}
              case '*' : {return 5;}
              case '/' : {return 6;}
              case '<' : {return 7;}
              case '>' : {return 8;}
              case '=' : {return 9;}
              case '"' : {return 10;}
              case '[' : {return 11;}
              case ']' : {return 12;}
              case '.' : {return 14;}
              case ':' : {return 15;}
              case '(' : {return 16;}
              case ')' : {return 17;}
              case '{' : {return 18;}
              case '}' : {return 19;}
              case '_' : {return 20;}
              case '\n' :{return 21;}
              case '\t' :{return 22;}
              case ' ' : {return 23;}
               
         }
          return 24; //Otros
    }
	
	public char getCharacter(){
		char c = entrada.charAt(posicion);
		if (isUpper(c)) {
			c = toLower(c);
		}
		return c;
	}
	
	public void increasePosition(){
		posicion++;
	}
}
