package gr.hua.dit.compilers;

import java.io.*;

import gr.hua.dit.compilers.Lexer;
import gr.hua.dit.compilers.parser;
import java_cup.runtime.Symbol;
import gr.hua.dit.compilers.ast.*;
import gr.hua.dit.compilers.visitors.JavaASMGenerator;
import gr.hua.dit.compilers.visitors.ScopeChecker;
import gr.hua.dit.compilers.visitors.TypeChecker;
import gr.hua.dit.compilers.SemanticError;

public class Main {

    public static void main(String[] args) {
        try {
            Reader r;
            String fileName = "Program"; 

            //check if filename provided as argument
            if (args.length > 0) {
                r = new FileReader(args[0]);
                fileName = new File(args[0]).getName().replace(".tony", "");
            } else {
                r = new InputStreamReader(System.in);
            }

            Lexer l = new Lexer(r);
            parser p = new parser(l);

            //perform syntax analysis
            System.out.println("Syntax analysis");
            
            FuncDefNode root = (FuncDefNode) p.parse().value;
            
            System.out.println("Syntax analysis completed");
            System.out.println("\n");
            
            //print the ast
            System.out.println("Abstract Syntax Tree:");
            System.out.println("\n");
            root.print(0);
            System.out.println("\n");

            //perform scope checking
            System.out.println("Scope checking");
            ScopeChecker scopeCheck = new ScopeChecker();
            root.accept(scopeCheck);
            System.out.println("Scope checking completed");

            //perform type checking
            System.out.println("Type checking");
            TypeChecker typeCheck = new TypeChecker();
            root.accept(typeCheck);
            System.out.println("Type checking completed\n");

            //generate target code
            System.out.println("Code generation");
            JavaASMGenerator asmGen = new JavaASMGenerator(fileName);
            root.accept(asmGen);
            
            asmGen.writeClass(fileName + ".class");
            
            System.out.println("Code generation completed. \n" + fileName + ".class created successfully.");
        
        } catch (SemanticError e) {
            //handle semantic errors
            System.err.println("Semantic Error: " + e.getMessage());
            System.exit(1);

        } catch (Exception e) {
            //handle general errors
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}