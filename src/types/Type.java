package types;

public abstract class Type {
	
	public static final Type INT = new INT();
	public static final Type VOID = new VOID();
	public static final Type CHAR = new CHAR();
	
	public Type actual() {
		return this;
	}
	
	public boolean equalTo(Type t) {
		return false;
	}

}
