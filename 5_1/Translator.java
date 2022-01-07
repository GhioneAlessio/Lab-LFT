/*CONTROLLARE SE POSSO RIMUOVERE CIO' CHE C'E' SU TAG.ID DA READ*/

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

    /* COMPLETE */
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

    public void statlist(int l) { // Riceve il label da prog e non ne "crea" uno nuovo
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

    public void statlistp() {
        switch (look.tag) { // non riceve e non incrementa il label.
            case ';':
                match(';');
                stat();
                statlistp();
                break;
            case Tag.EOF:
                break;
            case '}':
                break;
            default:
                error("Error in statlistp() ");
        }
    }

    /* CIAO QUA VA TUTTO BEN ^ */
    public void stat() {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist(0);
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist(1);
                match(')');
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist(0);
                match(')');
                break;
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
            case '{':
                match('{');
                statlist(0);
                match('}');
                break;
            default:
                error("Error in stat() ");
        }
    }

    // DID
    private void distat(int labelFalse) {
        switch (look.tag) {
            case Tag.END:
                match(Tag.END);
                code.emitLabel(labelFalse);
                break;
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

    // DID
    private void idlist(int assign_read) {
        switch (look.tag) {
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                if (assign_read == 1) {
                    code.emit(OpCode.invokestatic, 0); // 0 è per lettura
                }
                code.emit(OpCode.istore, id_addr);
                match(Tag.ID);
                idlistp(assign_read);
                break;
            default:
                error("Error in idlist()");
                break;
        }

    }

    // DID
    public void idlistp(int assign_read) {
        switch (look.tag) {
            case ',':
                match(',');
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                if (assign_read == 1) {
                    code.emit(OpCode.invokestatic, 0);
                }
                code.emit(OpCode.istore, id_addr);
                idlistp(assign_read);
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

    // DID
    private void expr() {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist(1);
                match(')');
                break;
            case '*':
                match('*');
                match('(');
                exprlist(2);
                match(')');
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc, (((NumberTok) look).lexeme));
                match(Tag.NUM);
                break;
            case Tag.ID: // assegno ad id_addr il valore dell'indrizzo in cui è salvato l'ID
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                /*  if (id_addr == -1) { // se non è mai stato salvato in un indirizzo avrà valore -1
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++); // assegno 0 al primo nuovo ID ed incremento di 1 ogni
                                                              // volta

                }
                */
                code.emit(OpCode.iload, id_addr); // stampo iload e l'indirizzo a cui è salvata la variabile
                match(Tag.ID);
                break;
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            default:
                error("Error in expr() ");
                break;
        }
    }

    // DID
    public void exprlist(int sum_mul) {
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

    // DID
    public void exprlistp(int sum_mul) {
        switch (look.tag) {
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
            case ')':
                break;
            default:
                error("Error in exprlistp() ");
        }
    }

    // DID
    private void bexpr(int labelTrue) {
        switch (look.tag) {
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