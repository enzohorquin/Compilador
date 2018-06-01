import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Set;
import java.util.Vector;

public class AssemblerGenerator {
    @SuppressWarnings("unused")
	private String data, asm, code, libraries;
    private Arbol a;
    Parser p;
    public static int start = 0;
    String codigo="";
    public static String functionCode = "";
    
    public AssemblerGenerator(Parser p){
        data = "";
        asm = "";
        code = "";
        libraries = ".386 \n.model flat, stdcall \noption casemap :none  \n" +
                "include \\masm32\\include\\windows.inc \ninclude \\masm32\\include\\kernel32.inc \ninclude \\masm32\\include\\masm32.inc  \n" +
                "includelib \\masm32\\lib\\kernel32.lib \nincludelib \\masm32\\lib\\masm32.lib\n" +
                "include \\masm32\\include\\user32.inc \n" +
                "includelib \\masm32\\lib\\user32.lib \n";
        this.p=p;
        a=p.getArbol();
        codigo = a.getAssembler();
    }
    
	private String genData() {
		String tabla = "\n.data\n";
		SymbolTable s = p.getST();
		for (String k : s.getKeys()) {
			RecordTS aux = s.get(k);
			String use = aux.getUse();
			if(use.equals("cte")){
				String aux2 = k.replace('.','_').replace('-', 'r').replace('+', 's');
				String prefix="_";
				if(aux.getType().equals("double"))
				data += prefix + aux2+ " dq "+k+"\n"; 	
				else
					data+=prefix + aux2+" dd "+k+"\n";
		}
			if (!use.equals("funcion") && !use.equals("cte")&& !use.equals("cadMulti") ) {				
				if (use.equals("variable") || use.equals("auxiliar")) {
					String prefix = "";
					if (use.equals("variable")) prefix="_";
					if (aux.getType().equals("uinteger"))
						data += prefix + k + " dd ?\n";
					else
						data += prefix + k + " dq ?\n";			
				}
			}
		
				
		}
		this.data += Node.ret1 + " dd ?\n" + Node.ret2 + " dq ?\n";
		this.data += "@aux_Flags" + " dw ?\n";	
		tabla += Node.data + this.data;
		tabla +=  "errorMsgMult db \"Error: Overflow.\", 0\n"
                + "errorMsgResta db \"Error: La resta dio como resultado un numero negativo.\", 0\n"
        		+ "errorMsgDivCero db \"Error: La division por cero no esta definida.\", 0\n";
		return tabla;
	}
    
    private String genCode(){
    	code = "\n.code\n"
                + "errorMult:\n"
                + "invoke MessageBox, NULL, addr errorMsgMult, addr errorMsgMult, MB_OK"  + "\n"
                + "invoke ExitProcess, 1\n"
                + "errorDivCero:\n"
                + "invoke MessageBox, NULL, addr errorMsgDivCero, addr errorMsgDivCero, MB_OK"  + "\n"
                + "invoke ExitProcess, 1\n"
                + "errorResta:\n"
                + "invoke MessageBox, NULL, addr errorMsgResta, addr errorMsgResta, MB_OK"  + "\n"
                + "invoke ExitProcess, 1\n"
                + functionCode;
        code += "start:\n";
        code+= codigo;

        code += "invoke ExitProcess, 0\nend start\n\n";
        return code;
    }
    
    public String getAsm(){
        return asm = this.libraries + this.genData() + this.genCode();
    }
    
	public void createASM(String name) {
		File file = new File(name + ".asm");
    	try {
			Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	PrintWriter pw;
		try {
			pw = new PrintWriter(file);
			pw.println(getAsm());
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
	}
}
