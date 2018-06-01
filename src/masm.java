import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class masm {
	public masm () throws IOException {
		File arch = new File("salida.asm");
		PrintWriter p = new PrintWriter(new FileWriter(arch));
		//Imprimir codigo assembler
	
		String comc = "cmd /c .\\masm32\\bin\\ml /c /Zd /coff salida.asm ";
		Process ptasm32 = Runtime.getRuntime().exec(comc);
		InputStream is = ptasm32.getInputStream();
	
		String coml = "cmd /c \\masm32\\bin\\Link /SUBSYSTEM:CONSOLE salida.obj ";
		Process ptlink32 = Runtime.getRuntime().exec(coml);
		InputStream is2 = ptlink32.getInputStream();
	}
}
