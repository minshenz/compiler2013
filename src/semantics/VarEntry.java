package semantics;
import types.*;

public class VarEntry implements Entry {
	public Type type;
	
	public VarEntry(Type t) {
		type = t;
	}
}
