public class AS8 extends SemanticAction{
	
	//Define cadena de caracteres
	public int ejecutar(char c, LexicAnalyzer a){
		a.setBuffer(a.getBuffer().substring(1));
		a.update(a.getBuffer(),"cadMulti", "string");
		return 0;
	}
}