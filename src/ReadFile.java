import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

public class ReadFile {
	private String filename ="";
	public String getContenido(){
		 
		String cadena = "";
		String aux="";
	    JFileChooser chooser_huff = new JFileChooser(Paths.get("").toAbsolutePath().toString());
		chooser_huff.setDialogTitle("Por favor seleccione el archivo que quiere compilar"); 
		int ret = chooser_huff.showOpenDialog(null);
		if (ret != JFileChooser.APPROVE_OPTION)
            return "";
		try {
			FileReader f =new FileReader(chooser_huff.getSelectedFile());
			filename = discardExtension(chooser_huff.getName(chooser_huff.getSelectedFile()));
			BufferedReader b = new BufferedReader(f);
	        try {
				while((aux = b.readLine())!=null) {
					
				    cadena+=aux+"\n";
				}
			
				  b.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cadena;
	}
	public String getFileName(){
		return filename;
	}
	
	private String discardExtension(String file){
		String name="";
		int i = file.lastIndexOf('.');
		if (i > 0) {
			name = file.substring(0,i);
		}
		else name = file;
		return name; 
	}
}
