package types;

public class Pointer extends Type {
	
	public Type elementType;
	
	public Pointer(Type t) {
		elementType = t;
	}
	
	public boolean equalTo(Type t) {
		return (t.actual() instanceof Pointer);
	}
	
	public Type elementType() {
		return elementType.actual();
	}

}
