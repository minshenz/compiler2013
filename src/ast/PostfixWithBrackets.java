package ast;

public class PostfixWithBrackets extends Postfix {

	public Expression expression;

	public PostfixWithBrackets(Expression e) {
		expression = e;
	}
	
}
