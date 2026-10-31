package gr.hua.dit.compilers.visitors;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import gr.hua.dit.compilers.ast.*;
import gr.hua.dit.compilers.symbols.SymbolEntry;
import gr.hua.dit.compilers.symbols.SymbolTable;

public class JavaASMGenerator extends AbstractVisitor<Void>{
    
    private ClassWriter cw;
    private MethodVisitor mv; 
    private String className;
    private SymbolTable symbols;

    private int counter = 0;

    public JavaASMGenerator(String className) {
        this.className = className;
        this.symbols = new SymbolTable();
        
        //automatic frame and stack computation
        this.cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        
        //public class <className> extends java/lang/Object
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);
    
        //create default constructor
        MethodVisitor initMv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        initMv.visitCode();
        initMv.visitVarInsn(Opcodes.ALOAD, 0);
        initMv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        initMv.visitInsn(Opcodes.RETURN);
        initMv.visitMaxs(0, 0); 
        initMv.visitEnd();

    }

    public void writeClass(String file) throws IOException {
        //write bytecode to file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(cw.toByteArray());
        }
    }


    @Override
    public Void visit(AddNode n) throws Exception {
        //evaluate first expression
        n.e1.accept(this);
        
        //evaluate second expression
        n.e2.accept(this);
        
        //perform integer addition
        mv.visitInsn(Opcodes.IADD);
        
        return null;
    }

    @Override
    public Void visit(AndNode n) throws Exception {
        Label falseLabel = new Label();
        Label endLabel = new Label();

        //evaluate first expression
        n.e1.accept(this);

        //short-circuit if false
        mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);

        //evaluate second expression
        n.e2.accept(this);

        //short-circuit if false
        mv.visitJumpInsn(Opcodes.IFEQ, falseLabel);

        //both true result
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        //false result
        mv.visitLabel(falseLabel);
        mv.visitInsn(Opcodes.ICONST_0);

        //end of short-circuit block
        mv.visitLabel(endLabel);

        return null;
    }

    @Override
    public Void visit(ArrayAccessNode n) throws Exception {
        //load array reference
        n.name.accept(this);
        
        //load index
        n.i.accept(this);
        
        //resolve element type
        Type elementType = null;
        if (n.name instanceof VariableNode) {
            String varName = ((VariableNode) n.name).varName;
            SymbolEntry entry = symbols.getEntry(varName);
            if (entry.getType() instanceof Type.ArrayType) {
                elementType = ((Type.ArrayType) entry.getType()).elementType; 
            }
        }
        
        //select appropriate load instruction
        if (elementType instanceof Type.IntType) {
            mv.visitInsn(Opcodes.IALOAD);
        } else if (elementType instanceof Type.BoolType) {
            mv.visitInsn(Opcodes.BALOAD);
        } else if (elementType instanceof Type.CharType) {
            mv.visitInsn(Opcodes.CALOAD);
        } else {
            mv.visitInsn(Opcodes.AALOAD);
        }

        return null;
    }

    @Override
    public Void visit(AssignNode n) throws Exception {
        //case for array access assignment
        if (n.var instanceof ArrayAccessNode) {
            ArrayAccessNode arrayAccess = (ArrayAccessNode) n.var;
            
            //load array and index
            arrayAccess.name.accept(this);
            arrayAccess.i.accept(this);
            
            //load value
            n.e.accept(this);
            
            //resolve element type for store
            Type elementType = null;
            if (arrayAccess.name instanceof VariableNode) {
                String varName = ((VariableNode) arrayAccess.name).varName;
                SymbolEntry entry = symbols.getEntry(varName);
                if (entry.getType() instanceof Type.ArrayType) {
                    elementType = ((Type.ArrayType) entry.getType()).elementType;
                }
            }
            
            //select store instruction
            if (elementType instanceof Type.IntType) {
                mv.visitInsn(Opcodes.IASTORE);
            } else if (elementType instanceof Type.BoolType) {
                mv.visitInsn(Opcodes.BASTORE);
            } else if (elementType instanceof Type.CharType) {
                mv.visitInsn(Opcodes.CASTORE);
            } else {
                mv.visitInsn(Opcodes.AASTORE);
            }
        } 
        //case for variable assignment
        else if (n.var instanceof VariableNode) {
            String varName = ((VariableNode) n.var).varName;
            SymbolEntry entry = symbols.getEntry(varName);
            
            //evaluate expression
            n.e.accept(this);
            
            int reg = Integer.parseInt(entry.getAddress());
            
            //store in local variable
            if (entry.getType() instanceof Type.IntType || 
                entry.getType() instanceof Type.BoolType || 
                entry.getType() instanceof Type.CharType) {
                mv.visitVarInsn(Opcodes.ISTORE, reg);
            } else {
                mv.visitVarInsn(Opcodes.ASTORE, reg);
            }
        }

        return null;
    }

    @Override
    public Void visit(Block n) throws Exception {
        //enter block scope
        if (n.table != null) {
            symbols.enterScope(n.table);
        } else {
            symbols.enterScope();
        }

        //visit declarations
        if (n.decls != null) {
            for (Decl d : n.decls) {
                if (d != null) {
                    d.accept(this);
                }
            }
        }

        //visit statements
        if (n.stmts != null) {
            for (Stmt s : n.stmts) {
                if (s != null) {
                    s.accept(this);
                }
            }
        }

        //exit scope
        symbols.exitScope();

        return null;
    }

    @Override
    public Void visit(FormalNode n) throws Exception {
        //assign register to formal parameter
        for (String name : n.names) {
            SymbolEntry argEntry = symbols.getEntry(name);
            if (argEntry != null && argEntry.getAddress() == null) {
                argEntry.setAddress(String.valueOf(this.counter++));
            }
        }
        return null;
    }

    private String getTypeDescriptor(Type type) {
        if (type == null) return "V";
        if (type instanceof Type.IntType) return "I";
        if (type instanceof Type.BoolType) return "Z";
        if (type instanceof Type.CharType) return "C";
        
        if (type instanceof Type.ArrayType) {
            Type elem = ((Type.ArrayType) type).elementType;
            if (elem instanceof Type.CharType) {
                return "Ljava/lang/String;";
            }
            return "[" + getTypeDescriptor(elem);
        }
        
        if (type instanceof Type.ListType) {
            return "Lgr/hua/dit/compilers/runtime/TonyList;";
        }
        return "V";
    }

    @Override
    public Void visit(FuncDefNode n) throws Exception {
        MethodVisitor oldMv = this.mv;
        int oldCounter = this.counter;

        boolean isMain = symbols.getCurrentScopeLevel() == 0;

        String methodName;
        String methodDescriptor;

        if (isMain) {
            methodName = "main";
            methodDescriptor = "([Ljava/lang/String;)V";
            this.counter = 0; 
        } else {
            methodName = n.name;
            StringBuilder sb = new StringBuilder("(");
            if (n.arguments != null) {
                for (FormalNode f : n.arguments) {
                    for (String ignored : f.names) sb.append(getTypeDescriptor(f.type));
                }
            }
            sb.append(")").append(getTypeDescriptor(n.returnType));
            methodDescriptor = sb.toString();
            this.counter = 0;
        }

        this.mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, methodName, methodDescriptor, null, null);
        this.mv.visitCode();

        symbols.enterScope(n.table);

        //setup parameters
        if (n.arguments != null) {
            for (FormalNode formal : n.arguments) {
                for (String name : formal.names) {
                    SymbolEntry argEntry = symbols.getEntry(name);
                    if (argEntry != null) {
                        argEntry.setAddress(String.valueOf(this.counter++));
                    }
                }
            }
        }

        //process definitions
        if (n.defs != null) {
            for (Decl def : n.defs) {
                if (def != null) {
                    def.accept(this);
                }
            }
        }

        //process statements
        if (n.stmts != null) {
            for (Stmt stmt : n.stmts) {
                if (stmt != null) {
                    stmt.accept(this);
                }
            }
        }

        //default return for void methods
        if (n.returnType == null || isMain) {
            this.mv.visitInsn(Opcodes.RETURN);
        }

        this.mv.visitMaxs(100, 100);
        this.mv.visitEnd();

        //restore context
        this.mv = oldMv;
        this.counter = oldCounter;

        symbols.exitScope();

        return null;
    }


    @Override
    public Void visit(VarDefNode n) throws Exception {
        //assign address to local variables
        for (String varName : n.varNames) {
            SymbolEntry entry = symbols.getEntry(varName);
            
            if (entry != null) {
                entry.setAddress(String.valueOf(this.counter++));
            }
        }
        return null;
    }


    @Override
    public Void visit(CallNode n) throws Exception {
        //resolve function
        SymbolEntry funcEntry = symbols.getEntry(n.functionName);
        if (funcEntry == null) {
            throw new RuntimeException("Function " + n.functionName + " is undefined.");
        }

        //process arguments
        if (n.arguments != null) {
            for (Expr arg : n.arguments) {
                if (arg != null) {
                    arg.accept(this);
                }
            }
        }

        //construct method descriptor
        StringBuilder descriptorBuilder = new StringBuilder("(");
        if (funcEntry.getParameterTypes() != null) {
            for (Type pType : funcEntry.getParameterTypes()) {
                descriptorBuilder.append(getTypeDescriptor(pType));
            }
        }
        descriptorBuilder.append(")");
        descriptorBuilder.append(getTypeDescriptor(funcEntry.getType()));
        String methodDescriptor = descriptorBuilder.toString();

        //invoke static method
        String classPath = this.className;
        
        List<String> builtIns = List.of("puti", "putb", "putc", "puts", "geti", "getb", "getc", "gets", "abs", "ord", "chr", "strlen", "strcmp", "strcpy", "strcat");
        if (builtIns.contains(n.functionName)) {
            classPath = "gr/hua/dit/compilers/runtime/TonyRuntime";
        }

        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            classPath,
            n.functionName,
            methodDescriptor,
            false
        );

        return null;
    }

    @Override
    public Void visit(ExprStmtNode n) throws Exception {
        if (n.expr != null) {
            n.expr.accept(this);
            
            //pop result if unused
            if (n.expr instanceof CallNode) {
                CallNode call = (CallNode) n.expr;
                SymbolEntry funcEntry = symbols.getEntry(call.functionName);
                
                if (funcEntry != null && funcEntry.getType() != null) {
                    mv.visitInsn(Opcodes.POP);
                }
            }
        }
        return null;
    }

    @Override
    public Void visit(FuncDeclNode n) throws Exception {
        //forward declarations do not generate bytecode
        return null;
    }

    @Override
    public Void visit(HeaderNode n) throws Exception {
        //header nodes do not generate bytecode
        return null;
    }

    @Override
    public Void visit(NewNode n) throws Exception {
        //evaluate size
        if (n.size != null) {
            n.size.accept(this);
        }

        //array creation
        if (n.elementType instanceof Type.IntType) {
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
        } 
        else if (n.elementType instanceof Type.BoolType) {
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_BOOLEAN);
        } 
        else if (n.elementType instanceof Type.CharType) {
            mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_CHAR);
        } 
        else {
            String elementDescriptor = getTypeDescriptor(n.elementType);
            
            if (elementDescriptor.startsWith("L") && elementDescriptor.endsWith(";")) {
                elementDescriptor = elementDescriptor.substring(1, elementDescriptor.length() - 1);
            }
            
            mv.visitTypeInsn(Opcodes.ANEWARRAY, elementDescriptor);
        }

        return null;
    }

    @Override
    public Void visit(ReturnNode n) throws Exception {
        if (n.e != null) {
            n.e.accept(this);

            Type returnType = n.e.type; 
            
            //return typed value
            if (returnType instanceof Type.IntType || 
                returnType instanceof Type.BoolType || 
                returnType instanceof Type.CharType) {
                
                mv.visitInsn(Opcodes.IRETURN);
            } else {
                mv.visitInsn(Opcodes.ARETURN);
            }
        } else {
            //empty return
            mv.visitInsn(Opcodes.RETURN);
        }

        return null;
    }

    @Override
    public Void visit(VariableNode n) throws Exception {
        //resolve variable in symbol table
        SymbolEntry entry = symbols.getEntry(n.varName);
        if (entry == null) {
            throw new RuntimeException("Variable " + n.varName + " is undefined.");
        }

        //get jvm register index
        int reg = Integer.parseInt(entry.getAddress());

        //load variable based on type
        if (entry.getType() instanceof Type.IntType || 
            entry.getType() instanceof Type.BoolType || 
            entry.getType() instanceof Type.CharType) {
            
            mv.visitVarInsn(Opcodes.ILOAD, reg);
        } else {
            //load reference for arrays and lists
            mv.visitVarInsn(Opcodes.ALOAD, reg);
        }

        return null;
    }

    @Override
    public Void visit(BoolNode n) throws Exception {
        //push boolean value as int
        if (n.value) {
            mv.visitInsn(Opcodes.ICONST_1);
        } else {
            mv.visitInsn(Opcodes.ICONST_0);
        }
        return null;
    }

    @Override
    public Void visit(CharNode n) throws Exception {
        //get character ascii value
        int asciiValue = (int) n.value;
        
        //push char value
        if (asciiValue >= -128 && asciiValue <= 127) {
            mv.visitIntInsn(Opcodes.BIPUSH, asciiValue);
        } else {
            mv.visitIntInsn(Opcodes.SIPUSH, asciiValue);
        }
        
        return null;
    }

    @Override
    public Void visit(ConsNode n) throws Exception {
        //evaluate head
        n.e1.accept(this);
        
        //primitive types to Object
        if (n.e1.type instanceof Type.IntType) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
        } else if (n.e1.type instanceof Type.BoolType) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
        } else if (n.e1.type instanceof Type.CharType) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
        }
        
        //evaluate tail
        n.e2.accept(this);

        //invoke cons
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "gr/hua/dit/compilers/runtime/TonyList",
            "cons",
            "(Ljava/lang/Object;Lgr/hua/dit/compilers/runtime/TonyList;)Lgr/hua/dit/compilers/runtime/TonyList;",
            false
        );

        return null;
    }

    @Override
    public Void visit(DivNode n) throws Exception {
        //evaluate dividend
        if (n.e1 != null) {
            n.e1.accept(this);
        }
        
        //evaluate divisor
        if (n.e2 != null) {
            n.e2.accept(this);
        }
        
        //perform integer division
        mv.visitInsn(Opcodes.IDIV);
        
        return null;
    }

    @Override
    public Void visit(IfNode n) throws Exception {
        Label endLabel = new Label();
        Label elseLabel = new Label();
        
        //create labels for elsifs
        List<Label> elsifLabels = new ArrayList<>();
        for (int i = 0; i < n.elsifs.size(); i++) {
            elsifLabels.add(new Label());
        }

        //evaluate condition
        n.condition.accept(this);
        mv.visitJumpInsn(Opcodes.IFEQ, (n.elsifs.isEmpty() && n.elseStmts == null) ? endLabel : 
                           (!n.elsifs.isEmpty() ? elsifLabels.get(0) : elseLabel));

        //then block
        for (Stmt s : n.thenStmts) s.accept(this);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        //elsif blocks
        for (int i = 0; i < n.elsifs.size(); i++) {
            mv.visitLabel(elsifLabels.get(i));
            n.elsifs.get(i).condition.accept(this);
            
            Label nextLabel = (i + 1 < elsifLabels.size()) ? elsifLabels.get(i + 1) : 
                                (n.elseStmts != null ? elseLabel : endLabel);
            mv.visitJumpInsn(Opcodes.IFEQ, nextLabel);
            
            for (Stmt s : n.elsifs.get(i).stmts) s.accept(this);
            mv.visitJumpInsn(Opcodes.GOTO, endLabel);
        }

        //else block
        if (n.elseStmts != null) {
            mv.visitLabel(elseLabel);
            for (Stmt s : n.elseStmts) s.accept(this);
        }

        mv.visitLabel(endLabel);
        return null;
    }

    @Override
    public Void visit(ElsifNode n) throws Exception {
        //elsif handled within ifnode visitor
        n.condition.accept(this);
        for (Stmt s : n.stmts) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(EqNode n) throws Exception {
        //evaluate expressions
        n.e1.accept(this);
        n.e2.accept(this);

        Label trueLabel = new Label();
        Label endLabel = new Label();

        //check equality based on type
        if (n.e1.type instanceof Type.IntType || n.e1.type instanceof Type.BoolType || n.e1.type instanceof Type.CharType) {
            mv.visitJumpInsn(Opcodes.IF_ICMPEQ, trueLabel);
        } else {
            mv.visitJumpInsn(Opcodes.IF_ACMPEQ, trueLabel);
        }

        //false case
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        //true case
        mv.visitLabel(trueLabel);
        mv.visitInsn(Opcodes.ICONST_1);

        mv.visitLabel(endLabel);
        
        return null;
    }

    @Override
    public Void visit(ExitNode n) throws Exception {
        //push exit code
        mv.visitInsn(Opcodes.ICONST_0);
        
        //call system exit
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC, 
            "java/lang/System", 
            "exit", 
            "(I)V", 
            false
        );
        
        return null;
    }

    @Override
    public Void visit(ForNode n) throws Exception {
        Label startLabel = new Label();
        Label endLabel = new Label();

        //initialization
        for (Stmt s : n.init) s.accept(this);

        //condition check
        mv.visitLabel(startLabel);
        n.condition.accept(this);
        mv.visitJumpInsn(Opcodes.IFEQ, endLabel);

        //loop body
        for (Stmt s : n.body) s.accept(this);

        //step execution
        for (Stmt s : n.step) s.accept(this);
        
        //loop back
        mv.visitJumpInsn(Opcodes.GOTO, startLabel);

        mv.visitLabel(endLabel);
        
        return null;
    }

    @Override
    public Void visit(GeqNode n) throws Exception {
        //evaluate expressions
        n.e1.accept(this);
        n.e2.accept(this);

        Label trueLabel = new Label();
        Label endLabel = new Label();

        //greater or equal check
        mv.visitJumpInsn(Opcodes.IF_ICMPGE, trueLabel);

        //false case
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        //true case
        mv.visitLabel(trueLabel);
        mv.visitInsn(Opcodes.ICONST_1);

        mv.visitLabel(endLabel);
        
        return null;
    }

    @Override
    public Void visit(GtNode n) throws Exception {
        //evaluate expressions
        n.e1.accept(this);
        n.e2.accept(this);

        Label trueLabel = new Label();
        Label endLabel = new Label();

        //greater than check
        mv.visitJumpInsn(Opcodes.IF_ICMPGT, trueLabel);

        //false case
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        //true case
        mv.visitLabel(trueLabel);
        mv.visitInsn(Opcodes.ICONST_1);

        mv.visitLabel(endLabel);
        
        return null;
    }

    @Override
    public Void visit(HeadNode n) throws Exception {
        //evaluate list expression
        n.listName.accept(this);
        
        //get head field
        mv.visitFieldInsn(
            Opcodes.GETFIELD,
            "gr/hua/dit/compilers/runtime/TonyList",
            "head",
            "Ljava/lang/Object;"
        );

        //unbox primitive types
        if (n.type instanceof Type.IntType) {
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Integer");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
        } else if (n.type instanceof Type.BoolType) {
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Boolean");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
        } else if (n.type instanceof Type.CharType) {
            mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Character");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
        }

        return null;
    }

    @Override
    public Void visit(IntNode n) throws Exception {
        //load constant integer value
        mv.visitLdcInsn(n.value);
        
        return null;
    }

    @Override
    public Void visit(LeqNode n) throws Exception {
        //evaluate expressions
        n.e1.accept(this);
        n.e2.accept(this);

        Label trueLabel = new Label();
        Label endLabel = new Label();

        //less than or equal check
        mv.visitJumpInsn(Opcodes.IF_ICMPLE, trueLabel);

        //false case
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        //true case
        mv.visitLabel(trueLabel);
        mv.visitInsn(Opcodes.ICONST_1);

        mv.visitLabel(endLabel);
        
        return null;
    }

    @Override
    public Void visit(LtNode n) throws Exception {
        //evaluate expressions
        n.e1.accept(this);
        n.e2.accept(this);

        Label trueLabel = new Label();
        Label endLabel = new Label();

        //less than check
        mv.visitJumpInsn(Opcodes.IF_ICMPLT, trueLabel);

        //false case
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        //true case
        mv.visitLabel(trueLabel);
        mv.visitInsn(Opcodes.ICONST_1);

        mv.visitLabel(endLabel);
        
        return null;
    }

    @Override
    public Void visit(ModNode n) throws Exception {
        //evaluate dividend and divisor
        if (n.e1 != null) {
            n.e1.accept(this);
        }
        if (n.e2 != null) {
            n.e2.accept(this);
        }
        
        //perform modulo operation
        mv.visitInsn(Opcodes.IREM);
        
        return null;
    }

    @Override
    public Void visit(MultNode n) throws Exception {
        //evaluate factors
        if (n.e1 != null) {
            n.e1.accept(this);
        }
        if (n.e2 != null) {
            n.e2.accept(this);
        }
        
        //perform multiplication
        mv.visitInsn(Opcodes.IMUL);
        
        return null;
    }

    @Override
    public Void visit(NeqNode n) throws Exception {
        //evaluate expressions
        n.e1.accept(this);
        n.e2.accept(this);

        Label trueLabel = new Label();
        Label endLabel = new Label();

        //check for inequality
        mv.visitJumpInsn(Opcodes.IF_ICMPNE, trueLabel);

        //false case
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        //true case
        mv.visitLabel(trueLabel);
        mv.visitInsn(Opcodes.ICONST_1);

        mv.visitLabel(endLabel);
        
        return null;
    }

    @Override
    public Void visit(NilNode n) throws Exception {
        //push null reference
        mv.visitInsn(Opcodes.ACONST_NULL);
        
        return null;
    }

    @Override
    public Void visit(NilqNode n) throws Exception {
        //evaluate list expression
        n.listName.accept(this);

        Label trueLabel = new Label();
        Label endLabel = new Label();

        //check if null
        mv.visitJumpInsn(Opcodes.IFNULL, trueLabel);

        //false case
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        //true case
        mv.visitLabel(trueLabel);
        mv.visitInsn(Opcodes.ICONST_1);

        mv.visitLabel(endLabel);

        return null;
    }

    @Override
    public Void visit(NotNode n) throws Exception {
        //evaluate expression
        n.e.accept(this);
        
        //bitwise xor to flip boolean
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitInsn(Opcodes.IXOR);
        
        return null;
    }

    @Override
    public Void visit(OrNode n) throws Exception {
        Label trueLabel = new Label();
        Label endLabel = new Label();

        //evaluate expressions with short-circuit
        n.e1.accept(this);
        mv.visitJumpInsn(Opcodes.IFNE, trueLabel);

        n.e2.accept(this);
        mv.visitJumpInsn(Opcodes.IFNE, trueLabel);

        //false case
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, endLabel);

        //true case
        mv.visitLabel(trueLabel);
        mv.visitInsn(Opcodes.ICONST_1);

        mv.visitLabel(endLabel);

        return null;
    }

    @Override
    public Void visit(SkipNode n) throws Exception {
        //no-op instruction
        mv.visitInsn(Opcodes.NOP);
        
        return null;
    }

    @Override
    public Void visit(StringNode n) throws Exception {
        //load string constant
        mv.visitLdcInsn(n.value);
        
        return null;
    }

    @Override
    public Void visit(SubNode n) throws Exception {
        //evaluate operands
        if (n.e1 != null) {
            n.e1.accept(this);
        }
        if (n.e2 != null) {
            n.e2.accept(this);
        }
        
        //perform subtraction
        mv.visitInsn(Opcodes.ISUB);
        
        return null;
    }

    @Override
    public Void visit(TailNode n) throws Exception {
        //evaluate list
        n.listName.accept(this);
        
        //get tail field
        mv.visitFieldInsn(
            Opcodes.GETFIELD,
            "gr/hua/dit/compilers/runtime/TonyList",
            "tail",
            "Lgr/hua/dit/compilers/runtime/TonyList;"
        );

        return null;
    }

    @Override
    public Void visit(UminusNode n) throws Exception {
        //evaluate expression
        n.e.accept(this);
        
        //perform negation
        mv.visitInsn(Opcodes.INEG);
        
        return null;
    }

    @Override
    public Void visit(UplusNode n) throws Exception {
        //unary plus has no effect
        n.e.accept(this);
        
        return null;
    }

}
