package gr.hua.dit.compilers;

import java_cup.runtime.*;

%%

%class Lexer
%unicode
%cup
%line
%column
%public

%state COMMENT

%{
  //create symbol without value
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  //create symbol with value
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }

  //counter for nested comments
  private int c = 0;
%}

%%

//keywords
"and" { return symbol(sym.AND); }
"bool" { return symbol(sym.BOOL); }
"char" { return symbol(sym.CHAR); }
"decl" { return symbol(sym.DECL); }
"def" { return symbol(sym.DEF); }
"else" { return symbol(sym.ELSE); }
"elsif" { return symbol(sym.ELSIF); }
"end" { return symbol(sym.END); }
"exit" { return symbol(sym.EXIT); }
"false" { return symbol(sym.FALSE); }
"for" { return symbol(sym.FOR); }
"head" { return symbol(sym.HEAD); }
"if" { return symbol(sym.IF); }
"int" { return symbol(sym.INT); }
"list" { return symbol(sym.LIST); }
"mod" { return symbol(sym.MOD); }
"new" { return symbol(sym.NEW); }
"nil" { return symbol(sym.NIL); }
"nil?" { return symbol(sym.NILQ); }
"not" { return symbol(sym.NOT); }
"or" { return symbol(sym.OR); }
"ref" { return symbol(sym.REF); }
"return" { return symbol(sym.RETURN); }
"skip" { return symbol(sym.SKIP); }
"tail" { return symbol(sym.TAIL); }
"true" { return symbol(sym.TRUE); } 

//operators
"+" { return symbol(sym.PLUS); }
"-" { return symbol(sym.MINUS); }
"*" { return symbol(sym.TIMES); }
"/" { return symbol(sym.DIV); }

//comparison operators
"=" { return symbol(sym.EQUAL); }
"<=" { return symbol(sym.LESS_OR_EQUAL); }
"<>" { return symbol(sym.NOT_EQUAL); }
"<" { return symbol(sym.LESS_THAN); }
">=" { return symbol(sym.GREATER_OR_EQUAL); }
">" { return symbol(sym.GREATER_THAN); }

//list construction operator
"#" { return symbol(sym.CONS); }

//delimiters
";" { return symbol(sym.SEMI); }
"(" { return symbol(sym.LPAREN); }
")" { return symbol(sym.RPAREN); }
"[" { return symbol(sym.LBRACKET); }
"]" { return symbol(sym.RBRACKET); }
"," { return symbol(sym.COMMA); }
":" { return symbol(sym.COLON); }
":=" { return symbol(sym.ASSIGN); }

//identifiers
[a-zA-Z][a-zA-Z0-9_?]* { return symbol(sym.ID, yytext()); }

//literals
[0-9]+ { return symbol(sym.INT_CONST, Integer.parseInt(yytext())); }
\'([^\'\"\\]|\\[ntr0\\\'\"]|\\x[0-9a-fA-F]{2})\' { return symbol(sym.CHAR_CONST, yytext().charAt(1)); }
\"([^\"\\\n]|\\[ntr0\\\'\"]|\\x[0-9a-fA-F]{2})*\" { return symbol(sym.STRING_LITERAL, yytext().substring(1, yytext().length()-1)); }

//ignore whitespace
[ \t\r\n]+ { /* ignore */ }

//single line comments
"%"[^\r\n]* { /* ignore */ }

//nested block comments
"<*" { c++; yybegin(COMMENT); }
<COMMENT>"<*" { c++; }
<COMMENT>"*>" { c--; if (c == 0) yybegin(YYINITIAL); }
<COMMENT>"*" { }
<COMMENT> [^*\n]+ { }
<COMMENT>\n { }
<COMMENT> <<EOF>> {throw new Error("Illegal character <"+yytext()+">");}

//eof handling
<<EOF>> { return symbol(sym.EOF); }

//error fallback
[^] { throw new Error("Illegal character <"+yytext()+">"); }