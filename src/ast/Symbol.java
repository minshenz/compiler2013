package ast;

public class Symbol {
	
	private String name;
	
	private Symbol(String n) {
		name = n;
	}
	
	public String toString() {
		return name;
	}
	
	private static java.util.Dictionary<String, Symbol> dict = new java.util.Hashtable<String, Symbol>();
	
	public static Symbol symbol(String n) {
		String u = n.intern();
		Symbol s = dict.get(u);
		if (s == null) {
			s = new Symbol(u);
			dict.put(u, s);
		}
		return s;
	}

}
