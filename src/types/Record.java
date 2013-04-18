package types;
import ast.Symbol;
import java.util.*;

public final class Record extends Type {
	
	private static int nowindex = 0;
	private Dictionary<Symbol, Type> fieldList = null;
	public int index;
	
	public Record() {
		fieldList = new Hashtable<Symbol, Type>();
		index = nowindex++;
	}
	
	public boolean equalTo(Type t) {
		if (t == null) return false;
		if (!(t.actual() instanceof Record)) return false;
		return ((Record) t).index == index; 
	}
	
	public void addField(Symbol s, Type t) {
		fieldList.put(s, t);
	}
	
	public Type getField(Symbol field) {
		Type t = fieldList.get(field);
		if (t == null) return null;
		else return t.actual();
	}
	
}
