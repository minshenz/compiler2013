package ast;

public class ConstantExpressionPackage {
	
	public ConstantExpression head;
	public ConstantExpressionPackage tail;
	
	public ConstantExpressionPackage(ConstantExpression h) {
		head = h;
		tail = null;
	}

	public ConstantExpressionPackage(ConstantExpression h, ConstantExpressionPackage t) {
		head = h;
		tail = t;
	}
	
}
