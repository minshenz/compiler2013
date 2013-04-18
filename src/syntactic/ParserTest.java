package syntactic;
import java.io.*;
import com.google.gson.Gson;
import ast.*;
import Table.*;

final class ParserTest {
	
	private static Table types = new Table();
	
	public static void beginScope() {
		types.beginScope();
	}
	
	public static void endScope() {
		types.endScope();
	}
	
	public static void addTypeId(Symbol s) {	
		types.put(s, s.toString());
	}
	
	public static boolean isTypeId(String s) {
		return types.get(Symbol.symbol(s)) != null;
	}
	
	private static void parse(String filename) throws IOException {
		InputStream in = new FileInputStream(filename);
		Parser parser = new Parser(in);
		java_cup.runtime.Symbol parseTree = null;
		try {
			parseTree = parser.parse();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Error(e.toString());
		} finally {
			in.close();
		}
		Gson gson = new Gson();
		System.out.println(gson.toJson(parseTree.value));
	}

	public static void main(String argv[]) throws IOException {
		parse("D:/compiler/input.txt");
	}
}
