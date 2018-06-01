
public class Regs {
	private static Boolean[] registros = {false,false,false,false};
	private static Boolean[] aux = new Boolean[4];
	
	public static boolean estaOcupado (int n){
	    return registros[n];
	}
	
	public static void Ocupar (int n) {
	    registros[n]=false;
	}
	
	public static void Liberar(String n) {
	    if (n.equals("AX")) registros[0]=false;
	    if (n.equals("BX")) registros[1]=false;
	    if (n.equals("CX")) registros[2]=false;
	    if (n.equals("DX")) registros[3]=false;
	}
	
	public static String getLibre() {
		if (registros[0]) {registros[0] = true; return "AX";}
	    if (registros[1]) {registros[1] = true; return "BX";}
	    if (registros[2]) {registros[2] = true; return "CX";}
	    if (registros[3]) {registros[3] = true; return "DX";}
	    return "";
	}
	
	public static String getLibreNOAX () {
	    if (!registros[1]) {registros[1] = true; return "BX";}
	    if (!registros[2]) {registros[2] = true; return "CX";}
	    if (!registros[3]) {registros[3] = true; return "DX";}
	    return "";
	}
	
	public static String getLibreDX () {
	    if (!registros[3]) {registros[3] = true; return "DX";}
	    return "";
	}
	
	public static String getLibreBX () {
	    if (!registros[1]) {registros[1] = true; return "BX";}
	    return "";
	}
	public static String getLibreCX () {
	    if (!registros[2]) {registros[2] = true; return "CX";}
	    return "";
	}
	
	public static String getLibreAX () {
	    if (!registros[0]) {registros[0] = true; return "AX";}
	    return "";
	}
	
	public static void clear(){
	    registros[0] = false;
	    registros[1] = false;
	    registros[2] = false;
	    registros[3] = false;
	}
	
	public static void ocuparAll(){
	    registros[0] = true;
	    registros[1] = true;
	    registros[2] = true;
	    registros[3] = true;
	}
	
	public static void push(){
	    aux[0] = registros[0];
	    aux[1] = registros[1];
	    aux[2] = registros[2];
	    aux[3] = registros[3];
	}
	
	public static void pop(){
	    registros[0] = aux[0];
	    registros[1] = aux[1];
	    registros[2] = aux[2];
	    registros[3] = aux[3];
	}
	
	public static void mostrar(){
	    System.out.println("AX: " + registros[0] + ", BX: " + registros[1] + ", CX: " + registros[2] + ", DX: " + registros[3] + "\n");
	}
}
