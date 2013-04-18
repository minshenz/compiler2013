package ast;

public class PostfixWithPointer extends Postfix {
	
	public enum PointerType{
		POINT, PTR
	}

	public PointerType pointertype;
	public Symbol symbol;
	
	public PostfixWithPointer(PointerType t, Symbol s) {
		pointertype = t;
		symbol = s;
	}
	
}
