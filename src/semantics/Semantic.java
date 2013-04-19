package semantics;
import types.*;
import ast.*;
import ast.Ellipsis;
import ast.Number;
import util.*;
import util.Error;
import java.util.List;
import java.util.ArrayList;

public class Semantic {

	private List<Error> errors = new ArrayList<Error>();
	private java.util.ArrayList<Var> callTable = new java.util.ArrayList<Var>();
	private Env env = null;
	private int inIteration = 0;
	private Type thisfunction = null;
	
	public Semantic() {
		env = new Env();
	}
	
	private void error(Position pos, String message) {
		errors.add(new Error(pos, message, true));
	}
	
	private void fatalError(Position pos, String message) {
		error(pos, message);
		printErrors();
		new Exception().printStackTrace();
		System.out.println("1");
		System.exit(1);
	}
	
	public void printErrors() {
		for (Error e : errors) {
			System.out.println("\t" + e);
		}
	}
	
	public boolean hasErrors() {
		return errors.size() > 0;
	}
	
	private Type toType(TypeSpecifier ty, int stars) {
		Type tmp = null;
		switch (ty.type) {
		case INT:
			tmp = Type.INT; break;
		case CHAR:
			tmp = Type.CHAR; break;
		case VOID:
			tmp = Type.VOID; break;
		case STRUCT:
			StructTypeSpecifier s = (StructTypeSpecifier) ty;
			if (s.pdl == null) {
				tmp = (Type) env.rEnv.get(s.name);
				if (tmp == null) {
					fatalError(s.pos, "Undefined struct name " + s.name.toString());
				}
			} else {
				if (s.name != null && !env.rEnv.checkNameUniquity(s.name)) {
					fatalError(s.pos, "Redeclaration of struct " + s.name.toString());
				}
				if (s.name != null) env.rEnv.put(s.name, new Name(s.name));
				tmp = checkStruct(s);
				if (s.name != null) ((Name) env.rEnv.get(s.name)).bind(tmp);
			}
			break;
		case UNION:
			UnionTypeSpecifier u = (UnionTypeSpecifier) ty;
			if (u.pdl == null) {
				tmp = (Type) env.rEnv.get(u.name);
				if (tmp == null) {
					fatalError(u.pos, "Undefined union name " + u.name.toString());
				}
			} else {
				if (u.name != null && !env.rEnv.checkNameUniquity(u.name)) {
					fatalError(u.pos, "Redeclaration of union " + u.name.toString());
				}
				if (u.name != null) env.rEnv.put(u.name, new Name(u.name));
				tmp = checkUnion(u);
				if (u.name != null) ((Name) env.rEnv.get(u.name)).bind(tmp);
			}
			break;
		case DEFINED:
			tmp = (Type) env.tEnv.get(((TypedefTypeSpecifier) ty).typedefname);
			if (tmp == null) {
				fatalError(ty.pos, "Undefined type name " + ((TypedefTypeSpecifier) ty).typedefname.toString());
			}
			break;
		}
		
		for (int i = 0; i < stars; ++i) {
			tmp = new Pointer(tmp);
		}
		return tmp;
	}
	
	private Type checkBrackets(Type t, DeclaratorWithBrackets d) {
		ConstantExpressionPackage clist = d.constantexpressionpackage;
		java.util.Stack<Integer> capStack = new java.util.Stack<Integer>();
		while(clist != null) {
			checkExpr(clist.head.op);
			if (!clist.head.isConstant()) {
				error(clist.head.pos, "Constant expression required in brackets");
				return null;
			}
			if (clist.head.op.value < 0) {
				error(clist.head.pos, "Array capacity must be positive");
				return null;
			}
			capStack.push(clist.head.op.value);
			clist = clist.tail;
		}
		while (!capStack.isEmpty()) t = new Array(t, capStack.pop());
		return t;
	}
	
	private Type checkParameters(Type rettype, DeclaratorWithParameters d) {
		Parameters paralist = d.parameters;
		Type type = new Function(null, rettype);
		java.util.Stack<Type> paraStack = new java.util.Stack<Type>();
		while (paralist != null) {
			if (paralist instanceof Ellipsis) {
				type = new types.Ellipsis(rettype);
				break;
			}
			TypeSpecifier ts = paralist.head.typespecifier;
			Declarator dec = paralist.head.dec;
			Type paratype = null;
			if (dec instanceof PlainDeclarator) {
				paratype = toType(ts, ((PlainDeclarator) dec).stars());
			} else if (dec instanceof DeclaratorWithBrackets) {
				paratype = toType(ts, dec.plaindeclarator.stars());
				paratype = checkBrackets(paratype, (DeclaratorWithBrackets) dec);
			} else if (dec instanceof DeclaratorWithParameters) {
				paratype = toType(ts, dec.plaindeclarator.stars());
				paratype = checkParameters(paratype, (DeclaratorWithParameters) dec);
			}
			paraStack.push(paratype);
			paralist = paralist.tail;
		}
		while (!paraStack.isEmpty()) type = new Function(paraStack.pop(), type);
		return type;
	}
	
	private Record checkStruct(StructTypeSpecifier s) {
		Record rec = new Record();
		PlainDeclarationList pdl = s.pdl;
		env.beginScope();
		while (pdl != null) {
			TypeSpecifier ts = pdl.typespecifier;
			DeclaratorList list = pdl.decl;
			Type plaintype = toType(ts, 0);
			while (list != null) {
				Declarator d = list.head;
				Type fieldtype = plaintype;
				Symbol fieldname = d.getSymbol();
				if (rec != null && rec.getField(fieldname) != null) {
					error(d.pos, "No two fields may have the same name");
					return null;
				}
				if (d instanceof PlainDeclarator) {
					for (int i = 0; i < ((PlainDeclarator) d).stars(); ++i) fieldtype = new Pointer(fieldtype);
				} else if (d instanceof DeclaratorWithBrackets) {
					for (int i = 0; i < d.plaindeclarator.stars(); ++i) fieldtype = new Pointer(fieldtype);
					fieldtype = checkBrackets(fieldtype, (DeclaratorWithBrackets) d);
				} else if (d instanceof DeclaratorWithParameters) {
					error(d.pos, "Field " + d.plaindeclarator.symbol.toString() + " declared as a function");
					return null;
				}
				rec.addField(fieldname, fieldtype);
				list = list.tail;
			}
			pdl = pdl.tail;
		}
		env.endScope();
		return rec;
	}
	
	private Record checkUnion(UnionTypeSpecifier s) {
		Record rec = new Record();
		PlainDeclarationList pdl = s.pdl;
		env.beginScope();
		while (pdl != null) {
			TypeSpecifier ts = pdl.typespecifier;
			DeclaratorList list = pdl.decl;
			Type plaintype = toType(ts, 0);
			while (list != null) {
				Declarator d = list.head;
				Type fieldtype = plaintype;
				Symbol fieldname = d.getSymbol();
				if (rec != null && rec.getField(fieldname) != null) {
					error(d.pos, "No two fields may have the same name");
					return null;
				}
				if (d instanceof PlainDeclarator) {
					for (int i = 0; i < ((PlainDeclarator) d).stars(); ++i) fieldtype = new Pointer(fieldtype);
				} else if (d instanceof DeclaratorWithBrackets) {
					for (int i = 0; i < d.plaindeclarator.stars(); ++i) fieldtype = new Pointer(fieldtype);
					fieldtype = checkBrackets(fieldtype, (DeclaratorWithBrackets) d);
				} else if (d instanceof DeclaratorWithParameters) {
					error(d.pos, "Field \"" + d.plaindeclarator.symbol.toString() + "\" declared as a function");
					return null;
				}
				rec.addField(fieldname, fieldtype);
				list = list.tail;
			}
			pdl = pdl.tail;
		}
		env.endScope();
		return rec;
	}
	
	public void checkProgram(Program prog) {
		while (prog != null) {
			if (prog.head instanceof Declaration) {
				checkDec((Declaration) prog.head);
			} else if (prog.head instanceof FunctionDefinition) {
				checkFunctionDef((FunctionDefinition) prog.head);
			}
			prog = prog.tail;
		}
		for (Var v : callTable) {
			FunEntry funinfo = (FunEntry) env.vEnv.get(v.symbol);
			if (!funinfo.implemented) error(v.pos, "Undefined reference to \"" + v.symbol.toString() + "\"");
		}
	}
	
	private void checkFunctionDef(FunctionDefinition funcdef) {
		Type type = toType(funcdef.typespecifier, funcdef.plaindeclarator.stars());
		thisfunction = type;
		type = new Function(null, type);
		Parameters paralist = funcdef.parameters;
		java.util.Dictionary<Symbol, Type> paraTable = new java.util.Hashtable<Symbol, Type>();
		java.util.Stack<Symbol> paraStack = new java.util.Stack<Symbol>();
		env.rEnv.beginScope();
		while (paralist != null) {
			if (paralist instanceof Ellipsis) {
				type = new types.Ellipsis(thisfunction);
				break;
			}
			TypeSpecifier ts = paralist.head.typespecifier;
			Declarator dec = paralist.head.dec;
			Type paratype = null;
			if (dec instanceof PlainDeclarator) {
				paratype = toType(ts, ((PlainDeclarator) dec).stars());
			} else if (dec instanceof DeclaratorWithBrackets) {
				paratype = toType(ts, dec.plaindeclarator.stars());
				paratype = checkBrackets(paratype, (DeclaratorWithBrackets) dec);
			} else if (dec instanceof DeclaratorWithParameters) {
				paratype = toType(ts, dec.plaindeclarator.stars());
				paratype = checkParameters(paratype, (DeclaratorWithParameters) dec);
			}
			if (paraTable.get(dec.getSymbol()) != null) {
				error(dec.pos, "Redeclaration of parameter " + dec.getSymbol().toString());
				return;
			} else paraTable.put(dec.getSymbol(), paratype);
			paraStack.add(dec.getSymbol());
			paralist = paralist.tail;
		}
		while (!paraStack.isEmpty()) type = new Function(paraTable.get(paraStack.pop()), type);
		if (!env.vEnv.checkNameUniquity(funcdef.plaindeclarator.getSymbol())) {
			VarEntry varinfo = (VarEntry) env.vEnv.get(funcdef.plaindeclarator.getSymbol());
			if (!(varinfo instanceof FunEntry) || (varinfo instanceof FunEntry && ((FunEntry) varinfo).implemented)) {
				error(funcdef.plaindeclarator.pos, "Redefinition of " + funcdef.plaindeclarator.getSymbol().toString());
				return;
			}
			if (!type.equalTo(((FunEntry) varinfo).type)) {
				error(funcdef.pos, "Function " + funcdef.plaindeclarator.getSymbol().toString() + " has conflicts with previous declaration");
				return;
			}
		}
		env.vEnv.put(funcdef.plaindeclarator.getSymbol(), new FunEntry((Function) type));
		((FunEntry) env.vEnv.get(funcdef.plaindeclarator.getSymbol())).Implement();
		
		paralist = funcdef.parameters;
		env.vEnv.beginScope();
		env.tEnv.beginScope();
		while (paralist != null) {
			if (paralist instanceof Ellipsis) break;
			Declarator dec = paralist.head.dec;
			env.vEnv.put(dec.getSymbol(), new VarEntry(paraTable.get(dec.getSymbol())));
			paralist = paralist.tail;
		}
		checkStmt((CompoundStatement) funcdef.compoundstatement);
		env.endScope();
	}
	
	private void checkStmt(Statement stmt) {
		if (stmt == null) return;
		if (stmt instanceof SelectionStatement) {
			checkStmt((SelectionStatement) stmt);
		} else if (stmt instanceof ExpressionStatement) {
			checkExpr(((ExpressionStatement) stmt).expression);
		} else if (stmt instanceof CompoundStatement) {
			env.beginScope();
			checkStmt((CompoundStatement) stmt);
			env.endScope();
		} else if (stmt instanceof IterationStatement) {
			inIteration++;
			checkStmt((IterationStatement) stmt);
			inIteration--;
		} else if (stmt instanceof JumpStatement) {
			checkStmt((JumpStatement) stmt);
		}
	}
	
	private void checkStmt(SelectionStatement stmt) {
		if (stmt instanceof IfStatement) {
			checkStmt((IfStatement) stmt);
		} else if (stmt instanceof IfElseStatement) {
			checkStmt((IfElseStatement) stmt);
		}
	}
	
	private void checkStmt(CompoundStatement stmt) {
		DeclarationList decl = stmt.decl;
		StatementList stmtl = stmt.stmtl;
		while (decl != null) {
			checkDec(decl.head);
			decl = decl.tail;
		}
		while (stmtl != null) {
			checkStmt(stmtl.head);
			stmtl = stmtl.tail;
		}
	}
	
	private void checkStmt(IterationStatement stmt) {
		if (stmt instanceof ForStatement) {
			checkStmt((ForStatement) stmt);
		} else if (stmt instanceof WhileStatement) {
			checkStmt((WhileStatement) stmt);
		}
	}
	
	private void checkStmt(JumpStatement stmt) {
		if (stmt instanceof ReturnStatement) {
			checkStmt((ReturnStatement) stmt);
		} else if (stmt instanceof BreakStatement) {
			if (inIteration == 0) {
				error(stmt.pos, "Break must be in a loop");
				return;
			}
		} else if (stmt instanceof ContinueStatement) {
			if (inIteration == 0) {
				error(stmt.pos, "Continue must be in a loop");
				return;
			}
		}
	}
	
	private void checkStmt(ForStatement stmt) {
		checkExpr(stmt.expr1);
		checkExpr(stmt.expr2);
		if (!stmt.expr2.type.equalTo(Type.INT)) {
			error(stmt.expr2.pos, "Invalid condition");
			return;
		}
		checkExpr(stmt.expr3);
		checkStmt(stmt.body);
	}
	
	private void checkStmt(WhileStatement stmt) {
		checkExpr(stmt.cond);
		if (!stmt.cond.type.equalTo(Type.INT)) {
			error(stmt.cond.pos, "Invalid condition");
			return;
		}
		checkStmt(stmt.body);
	}
	
	private void checkStmt(ReturnStatement stmt) {
		if (stmt.expression == null) return;
		checkExpr(stmt.expression);
		if (!stmt.expression.type.equalTo(thisfunction)) {
			error(stmt.pos, "Return type must match the return type of function");
			return;
		}
	}
	
	private void checkStmt(IfStatement stmt) {
		checkExpr(stmt.cond);
		if (!stmt.cond.type.equalTo(Type.INT)) {
			error(stmt.cond.pos, "Invalid condition");
			return;
		}
		checkStmt(stmt.body);
	}
	
	private void checkStmt(IfElseStatement stmt) {
		checkExpr(stmt.cond);
		if (!stmt.cond.type.equalTo(Type.INT)) {
			error(stmt.cond.pos, "Invalid condition");
			return;
		}
		checkStmt(stmt.body);
		checkStmt(stmt.elsebody);
	}
	
	private void checkDec(Declaration dec) {
		if (dec instanceof VarDeclaration) {
			checkDec((VarDeclaration) dec);
		} else if (dec instanceof TypeDeclaration) {
			checkDec((TypeDeclaration) dec);
		}
	}
	
	private void checkDec(VarDeclaration dec) {
		TypeSpecifier ts = dec.typespecifier;
		InitDeclaratorList list = dec.idecl;
		Type t = toType(ts, 0);
		while (list != null) {
			checkVarDec(list.head.pos, t, list.head);
			list = list.tail;
		}
	}
	
	private void checkVarDec(Position pos, Type ts, InitDeclarator id) {
		Declarator d = id.declarator;
		Initializer init = id.initializer;
		Type rtype = null;
		Type ltype = ts;
		if (!env.vEnv.checkNameUniquity(d.getSymbol())) {
			error(pos, "Redeclaration of variable");
			return;
		}
		if (init == null) rtype = null;
		else if (init instanceof BasicInitializer) {
			checkExpr(((BasicInitializer) init).expr);
			rtype = ((BasicInitializer) init).expr.type;
		} else if (init instanceof BracedInitializer) {
			/* to be accomplished */
		}
		if (d instanceof PlainDeclarator) {
			for (int i = 0; i < ((PlainDeclarator) d).stars(); ++i) ltype = new Pointer(ltype);
		} else if (d instanceof DeclaratorWithBrackets) {
			for (int i = 0; i < d.plaindeclarator.stars(); ++i) ltype = new Pointer(ltype);
			ltype = checkBrackets(ltype, (DeclaratorWithBrackets) d);
		} else if (d instanceof DeclaratorWithParameters) {
			if (init != null) {
				error(d.pos, "Function initiliazed like a variable");
				return;
			}
			for (int i = 0; i < d.plaindeclarator.stars(); ++i) ltype = new Pointer(ltype);
			ltype = checkParameters(ltype, (DeclaratorWithParameters) d);
			env.vEnv.put(d.getSymbol(), new FunEntry((Function) ltype));
			return;
		}
		if ((rtype != null) && !ltype.equalTo(rtype)) {
			error(pos, "Initializing with wrong type");
			return;
		}
		env.vEnv.put(d.getSymbol(), new VarEntry(ltype));
	}
	
	private void checkDec(TypeDeclaration dec) {
		TypeSpecifier ts = dec.typespecifier;
		DeclaratorList list = dec.decl;
		Type t = toType(ts, 0);
		while (list != null) {
			checkTypeDec(list.head.pos, t, list.head);
			list = list.tail;
		}
	}
	
	private void checkTypeDec(Position pos, Type ts, Declarator d) {
		if (!env.tEnv.checkNameUniquity(d.getSymbol())) {
			error(pos, "Redeclaration of type " + d.plaindeclarator.getSymbol());
			return;
		}
		Type type = ts;
		if (d instanceof DeclaratorWithParameters) {
			for (int i = 0; i < d.plaindeclarator.stars(); ++i) type = new Pointer(type);
			type = checkParameters(type, (DeclaratorWithParameters) d);
		} else if (d instanceof DeclaratorWithBrackets) {
			for (int i = 0; i < ((DeclaratorWithBrackets) d).plaindeclarator.stars(); ++i) type = new Pointer(type);
			type = checkBrackets(type, (DeclaratorWithBrackets) d);
		} else if (d instanceof PlainDeclarator) {
			for (int i = 0; i < ((PlainDeclarator) d).stars(); ++i) type = new Pointer(type);
		}
		
		env.tEnv.put(d.getSymbol(), type);
	}
	
	private void checkExpr(Expression expr) {
		if (expr == null) return;
		checkExpr((Op) expr);
	}
	
	private void checkExpr(Op op) {
		if (op instanceof CastExpression) {
			checkExpr((CastExpression) op);
		} else {
			checkExpr(op.left);
			checkExpr(op.right);
			switch (op.Optype) {
			case COMMA:
				op.type = op.right.type;
				op.isLvalue = op.right.isLvalue;
				op.isConstant = op.right.isConstant;
				if (op.isConstant) op.value = op.right.value;
				break;
			case ASSIGN:
			case MUL_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case ADD_ASSIGN: 
			case SUB_ASSIGN:
			case SHL_ASSIGN:
			case SHR_ASSIGN:
			case AND_ASSIGN:
			case XOR_ASSIGN: 
			case OR_ASSIGN:
				if (!op.left.isLvalue) fatalError(op.pos, "Invalid Lvalue");
				if (op.left.type instanceof Record || op.right.type instanceof Record)
					fatalError(op.pos, "Record type operands");
				if (!op.left.type.equalTo(op.right.type)) 
					if (!(op.left.type instanceof Pointer && op.right.isConstant && op.right.value == 0))
							fatalError(op.pos, "Assign or operate with incompatible types");
				op.type = op.left.type;
				op.isLvalue = false;
				op.isConstant = false;
				if (op.isConstant) op.value = calcConst(op.Optype, op.left.value, op.right.value);
				break;
			default:
				if (op.left.type instanceof Record || op.right.type instanceof Record)
					fatalError(op.pos, "Record type operands");
				if (!op.left.type.equalTo(op.right.type)) 
					fatalError(op.pos, "Assign or operate with incompatible types");
				if (!(op.left.type.equalTo(Type.INT) && op.right.type.equalTo(Type.INT))) fatalError(op.pos, "Operands must be INT or CHAR");
				op.isLvalue = false;
				op.type = op.left.type;
				op.isConstant = op.left.isConstant && op.right.isConstant;
				if (op.isConstant) op.value = calcConst(op.Optype, op.left.value, op.right.value);
			}
		}
	}
	
	private int calcConst(Op.OpType op, int l, int r) {
		switch (op) {		
		case OR:
			if (l == 0 && r == 0) return 0;
			else return 1;
		case AND: 
			if (l == 0 || r == 0) return 0;
			else return 1;
		case DIGIT_OR:
		case OR_ASSIGN:
			return l|r;
		case XOR:
		case XOR_ASSIGN:
			return l^r;
		case DIGIT_AND:
		case AND_ASSIGN:
			return l&r;
		case EQ:
			if (l == r) return 1;
			else return 0;
		case NE:
			if (l == r) return 0;
			else return 1;
		case LT:
			if (l < r) return 1;
			else return 0;
		case GT: 
			if (l > r) return 1;
			else return 0;
		case LE:
			if (l <= r) return 1;
			else return 0;
		case GE:
			if (l >= r) return 1;
			else return 0;
		case SHL:
		case SHL_ASSIGN:
			return l << r;
		case SHR:
		case SHR_ASSIGN:
			return l >> r;
		case PLUS:
		case ADD_ASSIGN:
			return l + r;
		case MINUS:
		case SUB_ASSIGN:
			return l - r;
		case TIMES:
		case MUL_ASSIGN:
			return l * r;
		case DIVIDE:
		case DIV_ASSIGN:
			return l / r;
		case MOD:
		case MOD_ASSIGN:
			return l % r;
		case COMMA:
		case ASSIGN:
			return r;
		}
		return 0;
	}
	
	private void checkExpr(CastExpression expr) {
		if (expr instanceof UnaryExpression) {
			checkExpr((UnaryExpression) expr);
		} else {
			checkExpr(expr.expr);
			Type casttype = toType(expr.casttype.typespecifier, expr.casttype.stars());
			if (!casttype.equalTo(expr.expr.type)) {
				fatalError(expr.pos, "Cannot cast the expression to target type");
			}
			expr.type = casttype;
			if (casttype.equalTo(Type.INT)) expr.isConstant = expr.expr.isConstant;
			else expr.isConstant = false;
			if (expr.isConstant) expr.value = expr.expr.value;
			expr.isLvalue = false;
		}
	}
	
	private void checkExpr(UnaryExpression expr) {
		if (expr instanceof PostfixExpression) {
			checkExpr((PostfixExpression) expr);
		} else if (expr instanceof SizeofTypenameExpression) {
			checkExpr((SizeofTypenameExpression) expr);
		} else if (expr instanceof UnaryCastExpression) {
			checkExpr((UnaryCastExpression) expr);
		} else {
			checkExpr(expr.uexpr);
			if (!(expr.uexpr.type.equalTo(Type.INT) || expr.uexpr.type instanceof Pointer)) fatalError(expr.pos, "Invalid operand type");
			if (!expr.uexpr.isLvalue) fatalError(expr.uexpr.pos, "Invalid Lvalue of prefix");
			expr.type = expr.uexpr.type;
			expr.isConstant = false;
			expr.isLvalue = false;
		}
	}
	
	private void checkExpr(PostfixExpression expr) {
		if (expr instanceof PrimaryExpression) {
			checkExpr((PrimaryExpression) expr);
		} else {
			checkExpr(expr.pexpr);
			checkPostfix(expr.postfix);
			if (expr.postfix instanceof PostfixWithBrackets) {
				if (!(expr.pexpr.type instanceof Pointer)) {
					fatalError(expr.pos, "Array type required");
				}
				expr.type = ((Pointer) expr.pexpr.type).elementType();
				expr.isLvalue = true;
				expr.isConstant = false;
			} else if (expr.postfix instanceof PostfixWithParens) {
				if (!(expr.pexpr.type instanceof Function)) {
					fatalError(expr.pexpr.pos, "Function name required");
				}
				Arguments args = ((PostfixWithParens) expr.postfix).arguments;
				Function rettype = (Function) expr.pexpr.type;
				while (args != null) {
					if (rettype.argumentType == null) {
						fatalError(args.head.pos, "Too many arguments");
					}
					if (!rettype.argumentType.equalTo(args.head.type)) {
						fatalError(args.head.pos, "Argument with wrong type");
					}
					args = args.tail;
					rettype = (Function) rettype.returnType;
				}
				if (rettype.argumentType != null && !(rettype.argumentType instanceof types.Ellipsis)) {
					fatalError(expr.pexpr.pos, "Too few arguments");
				}
				expr.type = rettype.returnType.actual();
				expr.isConstant = false;
				expr.isLvalue = false;
			} else if (expr.postfix instanceof PostfixWithPointer) {
				Type type = expr.pexpr.type;
				PostfixWithPointer pp = (PostfixWithPointer) expr.postfix;
				switch (pp.pointertype) {
				case POINT:
					if (!(type instanceof Record)) {
						fatalError(expr.pos, "Record type required");
					}
					Type t = ((Record) type).getField(pp.symbol);
					if (t == null) {
						fatalError(expr.pos, "Unknown field name \"" + pp.symbol.toString() + "\"");
					}
					expr.type = t;
					expr.isConstant = false;
					expr.isLvalue = true;
					break;
				case PTR:
					if (!(type instanceof Pointer)) {
						fatalError(expr.pexpr.pos, "Pointer type required");	
					} else if (!((((Pointer) type).elementType()) instanceof Record)) {
						fatalError(expr.pexpr.pos, "Record Pointer required");
					}
					Type pt = ((Record) ((Pointer) type).elementType()).getField(pp.symbol);
					if (pt == null) {
						fatalError(expr.pexpr.pos, "Unknown field name \"" + pp.symbol.toString() + "\"");
					}
					expr.type = pt;
					expr.isConstant = false;
					expr.isLvalue = true;
					break;
				}
			} else if (expr.postfix instanceof BasicPostfix) {
				if (!(expr.pexpr.type.equalTo(Type.INT) || expr.pexpr.type instanceof Pointer)) fatalError(expr.pos, "Invalid operand type");
				if (!expr.pexpr.isLvalue) fatalError(expr.pos, "Invalid Lvalue of postfix");
				expr.type = expr.pexpr.type;
				expr.isLvalue = false;
				expr.isConstant = false;
			}
		}
	}
	
	private void checkExpr(PrimaryExpression expr) {
		if (expr instanceof Var) {
			checkExpr((Var) expr);
		} else if (expr instanceof Constant) {
			checkExpr((Constant) expr);
		} else if (expr instanceof StringConstant) {
			checkExpr((StringConstant) expr);
		} else if (expr instanceof ParenedExpression) {
			Expression e = ((ParenedExpression) expr).expression;
			checkExpr(e);
			expr.type = e.type;
			expr.isConstant = e.isConstant;
			expr.isLvalue = e.isLvalue;
			if (expr.isConstant) expr.value = e.value;
		}
	}
	
	private void checkExpr(Var expr) {
		VarEntry varinfo = (VarEntry) env.vEnv.get(expr.symbol);
		if (varinfo == null) {
			error(expr.pos, "Undefined variable of function " + expr.symbol.toString());
			return;
		}
		expr.type = varinfo.type.actual();
		expr.isConstant = false;
		expr.isLvalue = true;
		if (varinfo instanceof FunEntry) callTable.add(expr);
	}
	
	private void checkExpr(Constant expr) {
		if (expr instanceof Number) {
			checkExpr((Number) expr);
		} else if (expr instanceof CharConstant) {
			checkExpr((CharConstant) expr);
		}
	}
	
	private void checkExpr(Number expr) {
		expr.type = Type.INT;
		expr.isConstant = true;
		expr.isLvalue = false;
		expr.value = expr.toInteger();
	}
	
	private void checkExpr(CharConstant expr) {
		expr.type = Type.CHAR;
		expr.isConstant = true;
		expr.isLvalue = false;
		expr.value = expr.toInteger();
	}
	
	private void checkExpr(StringConstant expr) {
		expr.type = new Array(Type.CHAR, expr.length());
		expr.isConstant = false;
		expr.isLvalue = false;
	}
	
	private void checkPostfix(Postfix p) {
		if (p instanceof PostfixWithBrackets) {
			PostfixWithBrackets pb = (PostfixWithBrackets) p;
			checkExpr(pb.expression);
			if (!pb.expression.type.equalTo(Type.INT)) {
				error(pb.expression.pos, "Integer index required");
				return;
			}
		} else if (p instanceof PostfixWithParens) {
			Arguments arg = ((PostfixWithParens) p).arguments;
			while (arg != null) {
				checkExpr(arg.head);
				arg = arg.tail;
			}
		}
	}
	
	private void checkExpr(SizeofTypenameExpression expr) {
		Type type = toType(expr.typename.typespecifier, expr.typename.stars());
		expr.type = Type.INT;
		expr.isConstant = true;
		expr.isLvalue = false;
		if (type instanceof INT) expr.value = 4;
		else if (type instanceof CHAR) expr.value = 1;
		else if (type instanceof VOID) expr.value = 1;
		else if (type instanceof Pointer) expr.value = 4;
		else expr.value = 0;
	}
	
	private void checkExpr(UnaryCastExpression expr) {
		CastExpression cexpr = expr.castexpression;
		checkExpr(cexpr);
		switch (expr.Unaryoperator) {
		case DIGIT_AND:
			expr.type = new Pointer(cexpr.type);
			expr.isConstant = false;
			expr.isLvalue = false;
			break;
		case TIMES:
			if (!(cexpr.type instanceof Pointer)) {
				error(expr.pos, "Pointer required");
				return;
			}
			expr.type = ((Pointer) cexpr.type).elementType();
			expr.isConstant = false;
			expr.isLvalue = true;
			break;
		case PLUS:
			if (!(cexpr.type.equalTo(Type.INT))) {
				error(expr.pos, "Wrong type argument to unary PLUS");
				return;
			}
			expr.type = cexpr.type;
			expr.isConstant = cexpr.isConstant;
			expr.isLvalue = false;
			if (expr.isConstant) expr.value = cexpr.value;
			break;
		case MINUS:
			if (!(cexpr.type.equalTo(Type.INT))) {
				error(expr.pos, "Wrong type argument to unary MINUS");
				return;
			}
			expr.type = cexpr.type;
			expr.isConstant = cexpr.isConstant;
			expr.isLvalue = false;
			if (expr.isConstant) expr.value = -cexpr.value;
			break;
		case DIGIT_NOT:
			if (!(cexpr.type.equalTo(Type.INT))) {
				error(expr.pos, "Wrong type argument to unary DIGIT_NOT");
				return;
			}
			expr.type = cexpr.type;
			expr.isConstant = cexpr.isConstant;
			expr.isLvalue = false;
			if (expr.isConstant) expr.value = ~cexpr.value;
			break;
		case  NOT:
			if (!(cexpr.type.equalTo(Type.INT))) {
				error(expr.pos, "Wrong type argument to unary NOT");
				return;
			}
			expr.type = cexpr.type;
			expr.isConstant = cexpr.isConstant;
			expr.isLvalue = false;
			if (expr.isConstant) {
				if (cexpr.value == 0) expr.value = 1;
				else expr.value = 0;
			}
			break;
		}
	}
	
	
}
	
