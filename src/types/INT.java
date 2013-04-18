package types;

public final class INT extends Type {
	public boolean equalTo(Type t) {
		return t.actual() instanceof INT || t.actual() instanceof CHAR;
	}

}
