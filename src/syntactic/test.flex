package syntactic;
import	java_cup.runtime.*;
import	java.io.*;

%%

%class	Lexer
%unicode
%line
%column
%cup
%implements Symbols

%{
	private void err(String message) {
		System.out.println("Scanning error in line " + yyline + ", column " + yycolumn + ": " + message);
		System.exit(1);
	}
	
	private java_cup.runtime.Symbol tok(int kind) {
		return new java_cup.runtime.Symbol(kind, yyline, yycolumn);
	}

	private java_cup.runtime.Symbol tok(int kind, Object value) {
		return new java_cup.runtime.Symbol(kind, yyline, yycolumn, value);
	}
%}

%eofval{
	return tok(EOF, null);
%eofval}

LineTerminator	= \r|\n|\r\n
InputCharacter	= [^\r\n]
WhiteSpace	= {LineTerminator} | [ \t\f]

Preprocessor= "#"~{LineTerminator}
Comment		= {TraditionalComment} | {EndOfLineComment} 
TraditionalComment	= "/*"~"*/"
EndOfLineComment	= "//"{InputCharacter}*{LineTerminator}

Identifier	= [a-zA-Z_$][0-9a-zA-Z_$]*

DecNumber	= 0 | [1-9][0-9]*
OctNumber	= 0[0-7]*
HexNumber	= 0x([0-9]|[a-fA-F])*
Number		= {DecNumber} | {OctNumber} | {HexNumber}

Char	= "'"[^]"'"
String	= "\""([^\"]|(\\\"))*"\""

%%

{Comment}	{/* Skip */}
"typedef"	{ return tok(TYPEDEF); }
"void"		{ return tok(VOID); }
"char"		{ return tok(CHAR); }
"int"		{ return tok(INT); }
"struct"	{ return tok(STRUCT); }
"union"		{ return tok(UNION); }
"if"		{ return tok(IF); }
"else"		{ return tok(ELSE); }
"while"		{ return tok(WHILE); }
"for"		{ return tok(FOR); }
"continue"	{ return tok(CONTINUE); }
"break"		{ return tok(BREAK); }
"return"	{ return tok(RETURN); }
"sizeof"	{ return tok(SIZEOF); }

{Identifier} {
	if (ParserTest.isTypeId(yytext())) {
		return tok(TYPEDEFNAME, yytext());
	} else {
		return tok(ID, yytext());
	}
}
{Number}	{ return tok(NUMBER, yytext()); }
{Char}		{ return tok(CHARCONSTANT, yytext().substring(1,2)); }
{String}	{ return tok(STRINGCONSTANT, yytext()); }

"<<="		{ return tok(SHL_ASSIGN); }
">>="		{ return tok(SHR_ASSIGN); }
"..."		{ return tok(ELLIPSIS); }

"||"		{ return tok(OR); }
"&&"		{ return tok(AND); }
"=="		{ return tok(EQ); }
"!="		{ return tok(NE); }
"<="		{ return tok(LE); }
">="		{ return tok(GE); }
"<<"		{ return tok(SHL); }
">>"		{ return tok(SHR); }
"++"		{ return tok(INC); }
"--"		{ return tok(DEC); }
"->"		{ return tok(PTR); }
"*="		{ return tok(MUL_ASSIGN); }
"/="		{ return tok(DIV_ASSIGN); }
"%="		{ return tok(MOD_ASSIGN); }
"+="		{ return tok(ADD_ASSIGN); }
"-="		{ return tok(SUB_ASSIGN); }
"&="		{ return tok(AND_ASSIGN); }
"^="		{ return tok(XOR_ASSIGN); }
"|="		{ return tok(OR_ASSIGN); }

"("		{ return tok(LPAREN); }
")"		{ return tok(RPAREN); }
";"		{ return tok(SEMICOLON); }
","		{ return tok(COMMA); }
"="		{ return tok(ASSIGN); }
"{"		{ return tok(LBRACE); }
"}"		{ return tok(RBRACE); }
"["		{ return tok(LBRACKET); }
"]"		{ return tok(RBRACKET); }
"*"		{ return tok(TIMES); }
"|"		{ return tok(DIGIT_OR); }
"^"		{ return tok(XOR); }
"&"		{ return tok(DIGIT_AND); }
"<"		{ return tok(LT); }
">"		{ return tok(GT); }
"+"		{ return tok(PLUS); }
"-"		{ return tok(MINUS); }
"/"		{ return tok(DIVIDE); }
"%"		{ return tok(MOD); }
"~"		{ return tok(DIGIT_NOT); }
"!"		{ return tok(NOT); }
"."		{ return tok(POINT); }

{WhiteSpace}	{/* Skip */}
{Preprocessor}	{/* Skip */}

[^]		{ throw new RuntimeException("Illegal character " + yytext() + " in line " + (yyline + 1) + ", column " + (yycolumn + 1)); }