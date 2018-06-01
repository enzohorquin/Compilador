import java.io.IOException;
import java.io.InputStream;

public class Main {
	public static void main(String[] args){
		ReadFile r = new ReadFile();
		Parser p = new Parser(r.getContenido());
		String filename = r.getFileName();
		p.run();
		p.showMessages(filename);
		Tree t = p.getArbol().createTree();
		t.print(filename);
		if (p.thereIsSomethingWrong())
			System.out.println("El codigo ta mal");
		else {
			AssemblerGenerator ag= new AssemblerGenerator(p);
			p.getLexicAnalyzer().showTokens(filename);
			ag.createASM(filename);
			try {
				generateExe(filename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		p.getST().toString(filename);
	}
	
	public static void generateExe(String fileName) throws IOException {

		String comc = "cmd /c .\\masm32\\bin\\ml /c /Zd /coff " + fileName + ".asm ";
		Process ptasm32 = Runtime.getRuntime().exec(comc);
		InputStream is = ptasm32.getInputStream();
	
		String coml = "cmd /c  \\masm32\\bin\\Link /SUBSYSTEM:CONSOLE " + fileName + ".obj";
		Process ptlink32 = Runtime.getRuntime().exec(coml);
		InputStream is2 = ptlink32.getInputStream();
	}
}

//cambie symboltable, checkvariablecorrectness, 