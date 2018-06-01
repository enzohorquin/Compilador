public class AS5 extends SemanticAction {

	// define operadores
	int ejecutar(char c, LexicAnalyzer a) {
		a.setBuffer(""+c);
		a.updateUnique(a.getBuffer(),a.getBuffer());
		return 0;
	}

}
