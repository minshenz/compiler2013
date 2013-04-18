package ast;

public class PostfixWithParens extends Postfix {
	
	public Arguments arguments;
	
	public PostfixWithParens() {
		arguments = null;
	}
	
	public PostfixWithParens(Arguments a) {
		arguments = a;
	}

}
