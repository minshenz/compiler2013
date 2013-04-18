package semantics;
import java.io.*;
import ast.*;
import syntactic.*;

final class SemanticTest {
	
	private static final String path = "D:/Compiler/";
	
	private static void semantic(String filename) throws Exception {
		InputStream inp = new BufferedInputStream(new FileInputStream(filename));
		Parser parser = new Parser(inp);
		java_cup.runtime.Symbol parseTree = null;
		try {
			parseTree = parser.parse();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Error(e.toString());
		} finally {
			inp.close();
		}
		
		Program tree = (Program) parseTree.value;
		
		Semantic semantic = new Semantic();
		semantic.checkProgram(tree);
		if (semantic.hasErrors()) {
			semantic.printErrors();
			System.out.println("1");
			System.exit(1);
		} else {
			System.out.println("\tSemantic OK");
			System.out.println("0");
			System.exit(0);
		}
	}
	
	public static void main(String argv[]) throws Exception {
		semantic(path + "input.txt");
	}

}
