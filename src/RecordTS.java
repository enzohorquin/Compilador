public class RecordTS{
    	private int reps;
    	String type;
    	String use;
        public RecordTS(String use, String type){ 
        	this.type=type;
        	this.use=use;
        	this.reps=1;
        }
        public void increase(){
        	reps++;
        }
        public int getReps(){
        	return reps;
        }
        public void decrease(){
        	reps--;
        }
        public void setType(String t){
        	type = t;
        }
        
        public String getType(){
        	return type;
        }
        public void setUse(String use){
        	this.use=use;
        }
        
        public String getUse(){
        	return use;
        	
        }
        
        public String toString() {
        	return type + " " + use + " " + reps; 
        }
    }