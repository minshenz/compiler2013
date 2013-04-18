package syntactic;
import java.io.*;

final class LexerTest {
	
	private static void lex(String filename) throws IOException {
		InputStream in = new FileInputStream(filename);
		Lexer lexer = new Lexer(in);
		while(true) lexer.next_token();
	}
	
	public static void main(String argv[]) throws IOException {
		lex("D:/Compiler/example1.c");
	}

}
