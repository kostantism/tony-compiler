package gr.hua.dit.compilers;

import java.io.*;

import gr.hua.dit.compilers.Lexer;
import gr.hua.dit.compilers.parser;
import gr.hua.dit.compilers.ast.Stmt;
import java_cup.runtime.Symbol;

public class Main {

    public static void main(String[] args) {
        try {
            Reader r;

            if (args.length > 0) {
                r = new FileReader(args[0]);
            } else {
                r = new InputStreamReader(System.in);
            }

            Lexer l = new Lexer(r);
            parser p = new parser(l);
            Stmt result = (Stmt) p.parse().value;
            result.print(0);

        } catch (Exception e) {
            System.err.println("Parsing failed: " + e.getMessage());
            System.exit(1);
        }
    }
}

