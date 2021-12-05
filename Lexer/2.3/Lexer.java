import java.io.*;

//feature del 47 

public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n')
                line++;
            readch(br);
        }

        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                readch(br); 
                if(peek == '/'){
                    while(peek != '\n' && peek != (char) -1){
                        readch(br);
                    }
                    return lexical_scan(br);
                }else if(peek == '*'){
                    readch(br); 
                    while(peek != '\n' && peek != (char) -1){
                        if(peek == '*'){
                            readch(br);
                            if(peek == '/'){
                                readch(br);
                                return lexical_scan(br);
                            }
                        }
                        readch(br);
                    }
                }            
                else
                     return Token.div;
            case ';':
                peek = ' ';
                return Token.semicolon;
            case ',':
                peek = ' ';
                return Token.comma;

            // ... gestire i casi di ( ) { } + - * / ; , ... //

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : " + peek);
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : " + peek);
                    return null;
                }
            case '<':
                readch(br);
                if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else {
                    peek = ' ';
                    return Word.lt;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else {
                    peek = ' ';
                    return Word.lt;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after = : " + peek);
                    return null;
                }
                // ... gestire i casi di || < > <= >= == <> ... //

            case (char) -1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek)) {
                    String sr = "" + peek;
                    readch(br);
                    while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
                        sr = sr + peek;
                        readch(br);
                    }
                    switch (sr) {
                        case "assign":
                            return Word.assign;
                        case "to":
                            return Word.to;
                        case "if":
                            return Word.iftok;
                        case "else":
                            return Word.elsetok;
                        case "while":
                            return Word.whiletok;
                        case "begin":
                            return Word.begin;
                        case "end":
                            return Word.end;
                        case "print":
                            return Word.print;
                        case "read":
                            return Word.read;
                        default:
                            return new Word(Tag.ID, sr);
                    }
                }
                // ... gestire il caso degli identificatori e delle parole chiave //

                else if (Character.isDigit(peek)) {
                    String num = "" + peek;
                    readch(br);
                    while (peek != '\t' && peek != '\n' && peek != '\r') {
                        if (peek == ' ' || peek == (char) -1)
                            return new NumberTok(num);
                        else if (Character.isDigit(peek)) {
                            num = num + peek;
                            readch(br);
                        } else{
                            System.err.println("Identifier can't start with number");
                            return null;
                        }
                            
                    }
                    return new NumberTok(num);
                } else if (peek == '_') {
                    String sr = "" + peek;
                    readch(br);
                    while (peek == '_') {
                        sr = sr + peek;
                        readch(br);
                        if (peek == ' ' || peek == (char) -1) {
                            System.err.println("Erroneous character: "
                                    + sr);
                            return null;
                        }
                    }
                    if (Character.isDigit(peek) || Character.isLetter(peek)) {
                        while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
                            sr = sr + peek;
                            readch(br);
                        }
                        return new Word(Tag.ID, sr);
                    } else {
                        System.err.println("Erroneous character: "
                                + sr);
                        return null;
                    }
                }
                else {
                    System.err.println("Erroneous character: "
                            + peek);
                    return null;
                }
        }

    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "D:\\Unito\\Secondo Anno\\LFT\\Lab LFT\\Lexer\\2.3\\file.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
