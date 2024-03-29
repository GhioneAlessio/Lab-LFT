import java.io.*;

public class Translator {
    private Lexer2_3 lex;
    private BufferedReader pbr;
    private Token look;
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer2_3 l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("Syntax error");
    }

    //GUIDA ( <prog> ::= <statlist> EOF ) = { assign, print, read, while, if, { } 
    public void prog() {
        int lnext_prog;
        switch (look.tag) {
            case Tag.ASSIGN:
                lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;
            case Tag.PRINT:
                lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;
            case Tag.READ:
                lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;
            case Tag.WHILE:
                lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;
            case Tag.IF:
                lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;
                
            case '{':
                lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;

            default:
                error("Error in prog()");
                break;
        }
    }
    //GUIDA ( <statlist> ::= <stat> <statlistp> ) = { assign, print, read, while, if, { }
    public void statlist(int l) { 
        switch (look.tag) {
            case Tag.ASSIGN:
                stat();
                statlistp();
                break;
            case Tag.PRINT:
                stat();
                statlistp();
                break;
            case Tag.READ:
                stat();
                statlistp();
                break;
            case Tag.WHILE:
                stat();
                statlistp();
                break;
            case Tag.IF:
                stat();
                statlistp();
                break;
            case '{':
                stat();
                statlistp();
                break;
            default:
                error("Error in statlist() ");
        }
    }
    //GUIDA ( <statlistp> ::= ; <stat> <statlistp> ) = { ; }
    public void statlistp() {
        switch (look.tag) {
            case ';':
                match(';');
                stat();
                statlistp();
                break;
    //GUIDA ( <statlistp> ::= ε ) = { EOF, } }
            case Tag.EOF:
                break;
            case '}':
                break;
            default:
                error("Error in statlistp() ");
        }
    }

    public void stat() {
        switch (look.tag) {
            //GUIDA ( <stat> ::= assign <expr> to <idlist> ) = { assign }
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                int v = expr();
                match(Tag.TO);
                idlist(0, v);
                break;
            //GUIDA ( <stat> ::= read ( <idlist> ) ) = { read }
            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist(1,-1);
                match(')');
                break;
            //GUIDA ( <stat> ::= print ( <exprlist> ) ) = { print }
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist(0);
                match(')');
                break;
            //GUIDA ( <stat> ::= while ( <bexpr> ) <stat> ) = { while }
            case Tag.WHILE: {
                int labelTrue = code.newLabel();
                int labelFalse = code.newLabel();
                int labelLoop = code.newLabel();

                code.emitLabel(labelLoop);// Stampo il label per il loop in caso di true (tipo: L1 if_icmpe) così da
                                          // tornare

                match(Tag.WHILE);
                match('(');

                bexpr(labelTrue);// gestisco condizione del while

                match(')');
                code.emit(OpCode.GOto, labelFalse); // stampo goto labelFalse per uscire dal loop
                code.emitLabel(labelTrue);// stampo il label true in caso di condizione verificata
                stat();
                code.emit(OpCode.GOto, labelLoop);// una volta finita la condizione verificata stampo goto "labelloop"
                                                  // per rientrare nel while
                code.emitLabel(labelFalse);// Stampo il labelFalse che sarà segito dal resto del codice
                break;
            }
            //GUIDA ( <stat> ::= if ( <bexpr> ) <stat> A ) = { if }
            case Tag.IF: {
                int labelTrue = code.newLabel();
                int labelFalse = code.newLabel();

                match(Tag.IF);
                match('(');

                bexpr(labelTrue);// gestisco la condizione dell'if

                match(')');
                code.emit(OpCode.GOto, labelFalse);// stampo goto labelFalse per uscire dal loop
                code.emitLabel(labelTrue);// stampo label true
                stat();
                distat(labelFalse);
                break;
            }
            //GUIDA ( <stat> ::= { <statlist> } ) = { { }	
            case '{':
                match('{');
                statlist(0);
                match('}');
                break;
            default:
                error("Error in stat() ");
        }
    }

    private void distat(int labelFalse) {
        switch (look.tag) {
            //GUIDA ( A ::= end ) = { end }
            case Tag.END:
                match(Tag.END);
                code.emitLabel(labelFalse);
                break;
            //GUIDA ( A ::= else <stat> end ) = { else }
            case Tag.ELSE:
                int skipElseLabel = code.newLabel();
                match(Tag.ELSE);
                code.emit(OpCode.GOto, skipElseLabel); // stampo per saltare else
                code.emitLabel(labelFalse); // in caso l'if fosse falso salto qua, ovvero entro nell'else
                stat();
                match(Tag.END);
                code.emitLabel(skipElseLabel); // segno la fine di else
                break;

            default:
                error("Error in statp()");
                break;
        }
    }

    
    private int idlist(int assign_read, int value) {
        int id_addr=0;
        switch (look.tag) {
            //GUIDA ( <idlist> ::= ID <idlistp> ) = { ID }
            case Tag.ID:
                id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                if (assign_read == 1) {
                    code.emit(OpCode.invokestatic, 0); // 0 è per prendere un valore da input
                }
                code.emit(OpCode.istore, id_addr);
                match(Tag.ID);
                idlistp(assign_read, value);
                break;
            default:
                error("Error in idlist()");
                break;
        }
        return id_addr;
    }

    
    public void idlistp(int assign_read, int value) {
        switch (look.tag) {
            //GUIDA ( <idlistp> ::= , ID <idlistp> ) = { , }
            case ',':
                match(',');
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                if (assign_read == 1) {
                    code.emit(OpCode.invokestatic, 0);
                }
                else if (assign_read == 0) { //se o istore devo ricaricare l valore per salvarlo in una seconda variabile
                    code.emit(OpCode.ldc, value);
				}
                code.emit(OpCode.istore, id_addr);
                match(Tag.ID);
                idlistp(assign_read, value);
                break;
            case ')':
                break;
            case ';':
                break;

            case Tag.END:
                break;
            case Tag.ELSE:
                break;

            case Tag.EOF:
                break;
            case '}':
                break;
            default:
                error("Error in idlistp() ");
        }
    }

    
    private int expr() {
        switch (look.tag) {
            //GUIDA ( <expr> ::= + ( <exprlist> ) ) = { + }
            case '+':
                match('+');
                match('(');
                exprlist(1);
                match(')');
                return -1;
            //GUIDA ( <expr> ::= * ( <exprlist> ) ) = { * }
            case '*':
                match('*');
                match('(');
                exprlist(2);
                match(')');
                return -1;
            //GUIDA ( <expr> ::= NUM ) = { NUM }
            case Tag.NUM:
                int n = ((NumberTok)look).lexeme;
                code.emit(OpCode.ldc, n);
                match(Tag.NUM);
                return n;
            //GUIDA ( <expr> ::= ID ) = { ID }
            case Tag.ID: // assegno ad id_addr il valore dell'indrizzo in cui è salvato l'ID
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                code.emit(OpCode.iload, id_addr); // stampo iload e l'indirizzo a cui è salvata la variabile
                match(Tag.ID);
                return -1;
            //GUIDA ( <expr> ::= - <expr> <expr> ) = { - }
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                return -1;
            //GUIDA ( <expr> ::= / <expr> <expr> ) = { / }
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                return -1;
            default:
                error("Error in expr() ");
                return -1;
        }
    }

    public void exprlist(int sum_mul) {
        //GUIDA ( <exprlist> ::= <expr> <exprlistp> ) = { +, -, *, /, NUM, ID }
        switch (look.tag) {
            case '+':
                expr();
                if (sum_mul == 0) {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(sum_mul);
                break;
            case '*':
                expr();
                if (sum_mul == 0) {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(sum_mul);
                break;

            case '-':
                expr();
                if (sum_mul == 0) {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(sum_mul);
                break;

            case '/':
                expr();
                if (sum_mul == 0) {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(sum_mul);
                break;

            case Tag.NUM:
                expr();
                if (sum_mul == 0) {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(sum_mul);
                break;
            case Tag.ID:
                expr();
                if (sum_mul == 0) {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(sum_mul);
                break;
            default:
                error("Error in bexpr() ");
                break;
        }
    }

    public void exprlistp(int sum_mul) {
        switch (look.tag) {
            //GUIDA ( <exprlistp> ::= , <expr> <exprlistp> ) = { , }
            case ',':
                match(',');
                expr();
                switch (sum_mul) {
                    case 1: // caso della somma
                        code.emit(OpCode.iadd);
                        break;
                    case 2: // caso della moltiplicazione
                        code.emit(OpCode.imul);
                        break;
                    case 0: // caso della print
                        code.emit(OpCode.invokestatic, 1);
                        break;
                }
                exprlistp(sum_mul);
                break;
            //GUIDA ( <exprlistp> ::= ε ) = { ) }
            case ')':
                break;
            default:
                error("Error in exprlistp() ");
        }
    }

    private void bexpr(int labelTrue) {
        switch (look.tag){
            //GUIDA ( <bexpr> ::= RELOP <expr> <expr> ) = { RELOP }
            case Tag.RELOP:
                String tmp = ((Word) look).lexeme;// != < > <= >= etc
                match(Tag.RELOP);
                expr();
                expr(); {

                if (tmp.equals(Word.eq.lexeme)) {// se relop è "==" stampo ifcmpeq
                    code.emit(OpCode.if_icmpeq, labelTrue);
                }
                if (tmp.equals(Word.ge.lexeme)) {
                    code.emit(OpCode.if_icmpge, labelTrue);
                }
                if (tmp.equals(Word.gt.lexeme)) {
                    code.emit(OpCode.if_icmpgt, labelTrue);
                }
                if (tmp.equals(Word.le.lexeme)) {
                    code.emit(OpCode.if_icmple, labelTrue);
                }
                if (tmp.equals(Word.lt.lexeme)) {
                    code.emit(OpCode.if_icmplt, labelTrue);
                }
                if (tmp.equals(Word.ne.lexeme)) {
                    code.emit(OpCode.if_icmpne, labelTrue);
                }

            }

                break;

            default:
                error("Error in bexpr()");
                break;
        }

    }

    public static void main(String[] args) {
        Lexer2_3 lex = new Lexer2_3();
        String path = "C:\\Users\\marco\\Desktop\\Unito\\Slide_LFT\\Esercizi\\Lab-LFT\\5_1\\test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}