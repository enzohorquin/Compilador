public class AS1 extends SemanticAction{
	
	//INCIALIZO EL BUFFER EN VACIO 
	public int ejecutar(char c,LexicAnalyzer a){
		a.setBuffer(""+c);
		return 0;
	}
}
