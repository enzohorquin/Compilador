public abstract class Arbol {
	
	public abstract boolean esHoja();

	public static void main(String[] args){
			
	}
	public abstract Tree createTree();
		
	public abstract String getAssembler();

	public abstract String toString(); 
	
	public abstract NodeHoja getLeaf();
	
		//EN DIVISION POR CERO TENGO QUE PREGUNTAR POR EL DIVISOR Y EJECUTAR EN EL ASSEMBLER UN MENSAJE QUE INDIQUE DIVISION POR CERO Y TERMINE.
		//OVERFLOW EN PRODUCTOS SI EL RESULTADO NO ENTRA EN 32 BITS. SE PONE EN 1 UN FLAG DE OVERFLOW (J0) , (JC) ES PARA DATOS ENTEROS SIN SIGNO. 
		//PARA FLOAT Y DOUBLE HAY QUE TRABAJAR CON EL COPROCESADOR MATEMATICO , ES UNA PILA DE 8 REGISTROS Y CADA UNO TIENE 80 BITS. 
		// OVERFLOW HAY QUE HACERLO DESPUES DE REALIZAR LA OPERACION, DIVISION POR CERO ANTES
		// Z = D1 + D2
		// DLD _D1
		// DADD _D2
		// DST _z 
		
}
