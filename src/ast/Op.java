package ast;
import util.Position;

public class Op extends Expression {
	
	public static enum OpType {
		ASSIGN, MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN, ADD_ASSIGN, 
		SUB_ASSIGN, SHL_ASSIGN, SHR_ASSIGN, AND_ASSIGN, XOR_ASSIGN, OR_ASSIGN,
		OR, AND, DIGIT_OR, XOR, DIGIT_AND, EQ, NE, LT, GT, 
		LE, GE, SHL, SHR, PLUS, MINUS, TIMES, DIVIDE, MOD, COMMA
	}
	
	public OpType Optype;
	public Expression left, right;
	
	public Op(Position p, Expression l, OpType o, Expression r) {
		pos = p;
		left = l;
		Optype = o;
		right = r;
	}


}
