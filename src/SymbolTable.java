	import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.print.DocFlavor.STRING;

public class SymbolTable {
	
    private static Hashtable<String,RecordTS> st = new Hashtable<String,RecordTS>();
    
    public RecordTS add(String lexema, String use, String type){
    	if (st.containsKey(lexema))
    		st.get(lexema).increase();
		else st.put(lexema,new RecordTS(use,type));
    	return st.get(lexema);
    }
    
    public RecordTS get(String s){
		return st.get(s);
	}
    
    public RecordTS get(String s, String ambito){
    	if (st.get(s+ambito) != null) { return st.get(s+ambito);}
    	else if ((st.get(s+"@main") != null) ) { return st.get(s + "@main");}
		return null;
	}
    
    public boolean isLocal(String id, String ambito) {
    	return (st.get(id+ambito) != null);
    }
    
    public String getVariableScope (String id, String ambito) {
    	if (st.get(id+ambito) != null) return ambito;
    	else if  ((st.get(id+"@main") != null) ) return "@main";
    	return "";
    }
    
    public void decrease(String s){
		st.get(s).decrease();
		if (st.get(s).getReps()==0) {
			st.remove(s);
		}
	}
	
	public RecordTS deleteReg(String s){
		return(st.remove(s));
	}
	
	public boolean exists(String s){
		return(st.containsKey(s));
	}
	
	public void setType(String lexema, String s){
    	st.get(lexema).setType(s);
    }
	
	public boolean hasType(String lexema){
		return (st.get(lexema).getType()!=null);
	}
	
	public String getType(String lexema){
    	return get(lexema).getType();
    }
	
	public String getType(String lexema, String ambito){
    	RecordTS aux = get(lexema,ambito);
    	if (aux==null) return null;
    	else return aux.getType();
    }
	
	public void setUse(String lexema, String s){
    	st.get(lexema).setUse(s);
    }
	
	public boolean hasUse(String lexema){;
		return (st.get(lexema).getUse()!=null);
	}
	
	public String getUse(String lexema){
    	return st.get(lexema).getUse();
    }
	
	public String getUse(String lexema, String ambito){
    	return get(lexema,ambito).getUse();
    }
	
	
	public boolean isFunction(String lexema){
		return (st.get(lexema).getUse()=="funcion");
	}
	
	public void setNegativeDouble(Token t) {
		String id = t.getId();
		RecordTS r = st.get(id);
		decrease(id);
		st.put("-" + id, r);
		t.setPointer(r);
	}
	
	public void update() {
		Iterator<String> i = this.st.keySet().iterator();
		while (i.hasNext()) {
			String id = i.next();
			if (get(id).getType()==null) 
				i.remove();
		}
	}


	public Set<String> getKeys() {
		return st.keySet();
	}
	
	public void toString(String name){
		try
		{
			FileWriter file = new FileWriter(new File(name+"_tabla_simbolos.txt"));
			PrintWriter pw = new PrintWriter(file);
			pw.println("TABLA DE SIMBOLOS:");
			pw.println("------------------");
			pw.println("");
			pw.printf("%-37s %-21s%21s","Lexema:","Tipo:","Uso:");
			pw.println("");
			pw.println("");
			Enumeration<String> palabras = st.keys();
			while (palabras.hasMoreElements()){
				String key = palabras.nextElement();
				pw.printf("%-37s %-21s%21s",key,st.get(key).getType(),st.get(key).getUse());
				pw.println("");
			}
			file.close();
		}catch(IOException ex){ex.printStackTrace();}
	}

	public static void addAuxiliarVariable(String name, String use, String type) {
		st.put(name,new RecordTS(use,type));
	}
}
