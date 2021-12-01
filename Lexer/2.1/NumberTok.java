public class NumberTok extends Token{
    public String lexeme;

    public NumberTok(String s) {
        super(256);
        lexeme = s;
    }

    public String toString() {
        return "<" + tag + ", " + lexeme + ">";
    }
}
