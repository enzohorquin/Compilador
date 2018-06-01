import java.util.logging.Level;
import java.util.logging.Logger;

public class AS7 extends SemanticAction{
	//DEFINE DOUBLE 
	private static final double limite_inf = 2.2250738585072014E-308; 
	private static final double limite_sup = 1.7976931348623157E308;
	private static final double cero = 0.0;	
	public int ejecutar(char c, LexicAnalyzer a){	
		boolean error = false;
		try {
			String constante = a.getBuffer();
			if (constante.startsWith(",")) constante = "0" + constante;
			constante = constante.replace(',','.');
			double doble = Double.parseDouble(constante);
			if (((doble>limite_inf) && (doble < limite_sup))|| doble==cero) {
				a.update(constante,"cte", "double");
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
			a.addError("Linea: " + a.getCode().getLine() + ": Constante double fuera de rango.");
		}
		return 1;
	}

}
