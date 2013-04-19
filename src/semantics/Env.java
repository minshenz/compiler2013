package semantics;
import Table.Table;
import types.*;
import ast.Symbol;

public final class Env {
	public Table tEnv = null;
	public Table vEnv = null;
	public Table rEnv = null;
	
	public Env() {
		initTEnv();
		initVEnv();
		initREnv();
	}
	
	private static Symbol symbol(String s) {
		return Symbol.symbol(s);
	}
	
	private void initREnv() {
		rEnv = new Table(); 
	}
	
	private void initTEnv() {
		tEnv = new Table();
		tEnv.put(symbol("int"), Type.INT);
		tEnv.put(symbol("void"), Type.VOID);
		tEnv.put(symbol("char"), Type.CHAR);
	}
	
	private void initVEnv() {
		vEnv = new Table();
		vEnv.put(symbol("NULL"), new VarEntry(new Pointer(Type.VOID)));
		vEnv.put(symbol("malloc"), new StdFunEntry(new Function(Type.INT, new Function(null, new Pointer(Type.VOID)))));
		vEnv.put(symbol("getchar"), new StdFunEntry(new Function(Type.INT)));
		vEnv.put(symbol("putchar"), new StdFunEntry(new Function(Type.INT, new Function(null, Type.INT))));
		vEnv.put(symbol("printf"), new StdFunEntry(new Function(new Pointer(Type.CHAR), new Ellipsis(Type.VOID))));
		vEnv.put(symbol("scanf"), new StdFunEntry(new Function(new Pointer(Type.CHAR), new Ellipsis(Type.INT))));
	}
	
	public void beginScope() {
		tEnv.beginScope();
		vEnv.beginScope();
		rEnv.beginScope();
	}
	
	public void endScope() {
		tEnv.endScope();
		vEnv.endScope();
		rEnv.endScope();
	}

}
