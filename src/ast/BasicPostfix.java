package ast;

public class BasicPostfix extends Postfix {
	
	public static enum PostfixType{
		INC, DEC
	}
	
	public PostfixType type;
	public BasicPostfix(PostfixType t) {
		type = t;
	}

}
