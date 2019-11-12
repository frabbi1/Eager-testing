package testsmelldetectorutils;

public class OutputList {
	
	String smell, fn, mn, line, path;
	
	public OutputList() {
		smell = "";
		fn = "";
		mn = "";
		line = "";
		path = "";
		
	}
	
	public void setSmell(String smell) {
		this.smell = smell;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public void setFn(String fn) {
		this.fn = fn;
	}
	public void setMn(String mn) {
		this.mn = mn;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getSmell() {
		return smell;
	}
	public String getMn() {
		return mn;
	}
	public String getPath() {
		return path;
	}
	public String getLine() {
		return line;
	}
	public String getFn() {
		return fn;
	}
	
	

}
