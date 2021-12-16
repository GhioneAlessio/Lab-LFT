import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
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

    public void prog() {
        switch (look.tag) {
            case Tag.ASSIGN:
                statlist();
                match(Tag.EOF);
                break;
            case Tag.PRINT:
                statlist();
                match(Tag.EOF);
                break;
            case Tag.READ:
                statlist();
                match(Tag.EOF);
                break;
            case Tag.WHILE:
                statlist();
                match(Tag.EOF);
                break;
            case Tag.IF:
                statlist();
                match(Tag.EOF);
                break;
            case '{':
                statlist();
                match(Tag.EOF);
                break;
            default:
                error("Error in prog() ");
                
        }
    }

    public void statlist() {
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
        switch (look.tag) {
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

    public void stat() {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist();
                match(')');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist();
                match(')');
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                stat();
                break;
            case Tag.IF:
                match(Tag.IF);
                match('(');
                bexpr();
                match(')');
                stat();
                distat();
                break;
            case '{':
                match('{');
                statlist();
                match('}');
                break;
            default:
                error("Error in stat() ");
        }
    }
    public void distat(){ //stato per la disambiguazione di stat()
        switch(look.tag){
            case Tag.END:
                match(Tag.END);
                break;
            case Tag.ELSE:
                match(Tag.ELSE);
                stat();
                match(Tag.END);
                break;
            default:
                error("Error in stat() ");
        }
    }
    public void idlist() {
        switch (look.tag) {
            case Tag.ID:
                match(Tag.ID);
                idlistp();
                break;
            default:
                error("Error in idlist() ");
        }
    }

    public void idlistp() {
        switch (look.tag) {
            case ',':
                match(',');
                match(Tag.ID);
                idlistp();
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

    public void bexpr() {
        switch (look.tag) {
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;
            default:
                error("Error in bexpr() ");
        }
    }

    public void expr() {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;
            case '-':
                match('-');
                expr();
                expr();
                break;
            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                break;
            case '/':
                match('/');
                expr();
                expr();
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;
            default:
                error("Error in bexpr() ");
        }
    }

    public void exprlist() {
        switch (look.tag) {
            case '+':
                expr();
                exprlistp();
                break;
            case '-':
                expr();
                exprlistp();
                break;
            case '*':
                expr();
                exprlistp();
                break;
            case '/':
                expr();
                exprlistp();
                break;
            case Tag.NUM:
                expr();
                exprlistp();
                break;
            case Tag.ID:
                expr();
                exprlistp();
                break;
            default:
                error("Error in bexpr() ");
        }
    }

    public void exprlistp() {
        switch (look.tag) {
            case ',':
                match(','); 
                expr();
                exprlistp();
                break;
            case ')':
                break;
            default:
                error("Error in bexpr() ");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/home/Alessio/Desktop/Uni/LFT/Lab/Parser/3.2/test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}