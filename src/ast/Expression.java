package ast;
import types.Type;
import util.Position;

public abstract class Expression {
	
	public Type type;
	public Position pos = null;
	public boolean isConstant = false;
	public boolean isLvalue;
	public int value;
	
}
