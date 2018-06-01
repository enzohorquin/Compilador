public class EED extends SemanticAction{
	// ERROR SE ESPERA UN DIGITO
	public int ejecutar(char c, LexicAnalyzer a){
		a.reset();
		a.addError( "Linea " + a.getCode().getLine() + ": Double mal definido, se espera un dígito.");
		return 1;
	}
}