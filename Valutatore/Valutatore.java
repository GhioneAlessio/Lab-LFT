package Valutatore;
import java.io.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + Lexer.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("syntax error");
    }

    public void start() {
        int expr_val;
        switch (look.tag) {
            case '(':
                expr_val = expr();
                match(Tag.EOF);
                System.out.println(expr_val);
                break;
            case Tag.NUM:
                expr_val = expr();
                match(Tag.EOF);
                System.out.println(expr_val);
                break;
            default:
                error("Errore in Start()");
                break;
        }
    }

    private int expr() {
        int term_val, exprp_val;
        switch (look.tag) {
            case '(':
                term_val = term();
                exprp_val = exprp(term_val);
                return exprp_val;
            case Tag.NUM:
                term_val = term();
                exprp_val = exprp(term_val);
                return exprp_val;
            default:
                error("Errore in Expr()");
                return 0;
        }
    }

    private int exprp(int exprp_i) {
        int term_val, exprp_val;
        switch (look.tag) {
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                return exprp_val;
            case '-':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                return exprp_val;
            case ')':
                return exprp_i;
            case Tag.EOF:
                return exprp_i;
            default:
                error("Errore in Exprp()");
                return 0;
        }
    }

    private int term() {
        int fact_val, term_val;
        switch (look.tag) {
            case '(':
                fact_val = fact();
                term_val = termp(fact_val);
                return term_val;
            case Tag.NUM:
                fact_val = fact();
                term_val = termp(fact_val);
                return term_val;
            default:
                error("Errore in Term()");
                return 0;
        }
    }

    private int termp(int termp_i) {
        int fact_val, termp_val;
        switch (look.tag) {
            case '*':
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                return termp_val;
            case '/':
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                return termp_val;
            case ')':
                return termp_i;
            case '+':
                return termp_i;
            case '-':
                return termp_i;
            case Tag.NUM:
                return termp_i;
            case Tag.EOF:
                return termp_i;
            default:
                error("Errore in Termp()");
                return 0;
        }
    }

    private int fact() {
        int expr_val, fact_val;
        switch (look.tag) {
            case '(':
                match('(');
                expr_val = expr();
                match(')');
                return expr_val;
            case Tag.NUM:
                fact_val = (((NumberTok) look).lexeme);
                match(Tag.NUM);
                return fact_val;
            default:
                error("Errore in Fact()");
                return 0;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "./test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
