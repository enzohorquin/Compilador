public class AS3 extends SemanticAction{
	
	//Define identificador o palabra reservada y chequea rango identificador
	public int ejecutar(char c,LexicAnalyzer a){
		String cadena = a.getBuffer();
		if (a.getLPR().contains(a.getBuffer())){
			a.updateUnique(cadena,cadena);
		}
		else
		{
			if (cadena.length()>15){ 
				cadena = (cadena.substring(0, 14));
				a.addError("Linea " + a.getCode().getLine() + ": Identificador demasiado largo.");
			}
			a.update(cadena,"id",null); 
		} 
		return 1;
	}
}