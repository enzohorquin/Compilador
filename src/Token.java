public class Token{
   
    private String id;
    private String use;
	private RecordTS pointer;
    public Token(String id, String use, RecordTS pointer){
    	this.pointer = pointer;
    	this.id = id;
    	this.use=use;
    }
    
	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}
    
    public String getId() {
		return id;
	}
    
	public void setId(String id) {
		this.id = id;
	}
	
    public RecordTS getPointer() {
        return pointer;
    }
    
    public void setPointer(RecordTS pointer) {
        this.pointer=pointer;
    }
    
    public void setLexema(String lexema){
    	this.id = lexema;
    }
    
    public String getLexemaCorto(){
    	int indice=id.indexOf("@");
    	if (indice!=-1){
    		return id.substring(0,indice);
    	}
    	return id;
    }
}
