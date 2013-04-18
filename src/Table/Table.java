package Table;
import ast.*;

class Binder {
	Object value;
	Symbol prevtop;
	Binder tail;
	
	Binder(Object v, Symbol p, Binder t) {
		value = v;
		prevtop = p;
		tail = t;
	}
	
}

public class Table {
	
	private java.util.Dictionary<Symbol, Binder> dict = new java.util.Hashtable<Symbol, Binder>();
	private Symbol top = null;
	private Binder marks = null;
	
	public Object get(Symbol key) {
		Binder e = dict.get(key);
		if (e == null) {
			return null;
		}else{
			return e.value;
		}
	}
	
	public void put(Symbol key, Object value) {
		dict.put(key, new Binder(value, top, dict.get(key)));
		top = key;
	}
	
	public void beginScope() {
		marks = new Binder(null, top, marks);
		top = null;
	}
	
	public void endScope() {
		while (top != null) {
			Binder e = dict.get(top);
			if (e.tail != null) {
				dict.put(top, e.tail);
			}else{
				dict.remove(top);
			}
			top = e.prevtop;
		}
		top = marks.prevtop;
		marks = marks.tail;
	}
	
	public boolean checkNameUniquity(Symbol key) {
		Symbol tmp = top;
		while (tmp != null) {
			if (tmp.equals(key)) return false;
			Binder e = dict.get(tmp);
			tmp = e.prevtop;
		}
		return true;
	}
	
	public java.util.Enumeration<Symbol> keys() {
		return dict.keys();
	}

}
