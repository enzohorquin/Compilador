public class ECI extends SemanticAction{
	// ERROR CARACTER INVALIDO
	public int ejecutar(char c, LexicAnalyzer a){
		a.reset();
		a.addError("Linea " + a.getCode().getLine() +": Caracter inv�lido.");
		return 0;
	}


}