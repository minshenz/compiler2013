package types;

public final class Function extends Type {
	
	public Type argumentType = null;
	public Type returnType;
	
	public Function(Type rt) {
		returnType = rt;
	}
	
	public Function(Type at, Type rt) {
		argumentType = at;
		returnType = rt;
	}
	
	public boolean equalTo(Type t) {
		if (!(t.actual() instanceof Function)) return false;
		Type arg = ((Function) t).argumentType;
		Type ret = ((Function) t).returnType;
		if ((argumentType == null && arg == null) || (argumentType != null && arg != null && argumentType.equalTo(arg)))
			return returnType.equalTo(ret);
		else return false;
	}

}
