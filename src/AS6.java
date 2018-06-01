public class AS6 extends SemanticAction{
	
	//Define comparadores y especiales
	public int ejecutar (char c, LexicAnalyzer a){
		a.updateUnique(a.getBuffer(),a.getBuffer());
		return 1;
	}
}
