import java.util.logging.Level;
import java.util.logging.Logger;

public class AS4 extends SemanticAction {
	
	//Define entero y verifica rango _ui que va entre 0 y 2^16-1
	public int ejecutar(char c, LexicAnalyzer a){
		boolean error = false;
		try {
			String constante = a.getBuffer();
			int entero = Integer.parseInt(constante);
			if (( 0 <= entero) && (entero <=  65535)){
				a.update(constante,"cte", "uinteger");
            }
			else
				error = true;
		}
		catch (Exception ex) {
			error = true;
            Logger.getLogger(LexicAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
			}
		if (error){
			a.reset();
			a.addError("Linea: " + a.getCode().getLine() + ": Constante uinteger fuera de rango.");
		}
		return 1;	
	}
}
