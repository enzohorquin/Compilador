public class ASC extends SemanticAction{
	// Consume caracteres
	public int ejecutar(char c, LexicAnalyzer a){
		if (c == '\n')
			a.getCode().lineaActual++;
		return 0;
	}
}
