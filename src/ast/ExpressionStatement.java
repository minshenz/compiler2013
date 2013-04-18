package ast;

public class ExpressionStatement extends Statement {
	
	public Expression expression;
	
	public ExpressionStatement() {
		expression = null;
	}

	public ExpressionStatement( Expression e) {
		expression = e;
	}

}
