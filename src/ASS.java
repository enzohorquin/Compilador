public class ASS extends SemanticAction {
	//Accion Semantica para las cadenas multilinea
	@Override
	int ejecutar(char c, LexicAnalyzer a) {
		if(c=='\n'){
			System.out.println("WARNING: SALTO DE LINEA CON ...");
			a.getCode().lineaActual++;
		}
		else {
		System.out.println(a.getBuffer());
		String buffer = a.getBuffer().substring(0,a.getBuffer().length()-2) + " ";
		a.setBuffer(buffer);
		System.out.println(a.getBuffer());
		}
		return 0;
	}

}
