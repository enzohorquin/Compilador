	import java.util.Vector;

public class NodeHoja extends Arbol{	

	private String id;
	private String type;
	private String use;

	public NodeHoja(Token t , String ambito) {
		super(); 
		RecordTS rec = t.getPointer();
		id = t.getId() + ambito;
		type=rec.getType();
		use=rec.getUse();
	}
	 
	public NodeHoja(String id, String type, String use) {
		this.id=id;
		this.type=type;
		this.use=use;
		if ( use.equals("auxiliar")) SymbolTable.addAuxiliarVariable(id, use, type);
	}
	
	public String getId() {
		if (use.equals("variable")|| (use.equals("cte") && type.equals("uinteger")))
			return "_" + id;
		else 
			if(use.equals("cte") && type.equals("double"))
				return "_"+id.replace('.','_').replace('-', 'r').replace('+', 's');
			else
				return id;
			
	}

	public String getUse() {
		return use;
	}

	public boolean esHoja() {
		return true;
	}
	
	public String getType(){
		return type;
	}

	public Tree createTree() {
		return new Tree("'" + id + "'",new Vector<Tree>());
	}
	
	public String getAssembler() {
		return "";
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public NodeHoja getLeaf() {
		return this;
	}
}
