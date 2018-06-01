import java.util.logging.Level;
import java.util.logging.Logger;

public class AS2 extends SemanticAction{
	//Concatena el caracter de entrada con el buffer actual	
		public int ejecutar (char c,LexicAnalyzer a){
			try{
				a.setBuffer ( a.getBuffer() + c); //Agrego al buffer el char actual
			}
			catch (Exception ex) { //Try-Catch para salvar fuera de rango de String Java
				Logger.getLogger(LexicAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
			}
			return 0; //Devuelvo 0 para avanzar una posición y seguir reconociendo
		}
}
