// non funziona, va fixato
import java.util.Scanner;

public class Es1_4 {

    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            char ch = s.charAt(i);
            switch (state) {
            case 0:
                if (ch == ' ')
                    state = 0;
                else if (Character.isDigit(ch)) {
                    if (Integer.parseInt(String.valueOf(ch)) % 2 == 0)
                        state = 1;
                    else
                        state = 2;
                } else
                    state = -1;
                break;
            case 1: //cifra pari
                if (Character.isDigit(ch)) {
                    if (Integer.parseInt(String.valueOf(ch)) % 2 == 0)
                        state = 1;
                    else
                        state = 2;
                } else if (Character.isLetter(ch)) {
                    Character.toLowerCase(ch);
                    if (97 <= ch && ch <= 107)
                        state = 3;
                    else
                        state = -1;
                } else if (ch == ' ')
                    state = 5;
                else
                    state = -1;
                break;
            case 2: //cifra dispari
                if (Character.isDigit(ch)) {
                    if (Integer.parseInt(String.valueOf(ch)) % 2 == 0)
                        state = 1;
                    else
                        state = 2;
                } else if (Character.isLetter(ch)) {
                    Character.toLowerCase(ch);
                    if (97 <= ch && ch <= 107)
                        state = -1;
                    else
                        state = 4;
                } else if (ch == ' ')
                    state = 6;
                else
                    state = -1;
                break;
            case 3:
                if (Character.isLetter(ch))
                    state = 3;
                else if (ch == ' ')
                    state = 5;
                else
                    state = -1;
                break;
            case 4:
                if (Character.isLetter(ch))
                    state = 4;
                else if (ch == ' ')
                    state = 6;
                else
                    state = -1;
                break;
            case 5:
                if (Character.isLetter(ch)) {
                    if (Character.isUpperCase(ch))
                        state = 3;
                    else
                        state = -1;
                } else if (ch == ' ')
                    state = 5;
                else
                    state = -1;
            case 6:
                if (Character.isLetter(ch)) {
                    if (Character.isUpperCase(ch))
                        state = 4;
                    else
                        state = -1;
                } else if (ch == ' ')
                    state = 6;
                else
                    state = -1;
            }
            i += 1;
        }
        return state == 3 || state == 4 || state == 5 || state == 6;
    }

    public static void main(String[] args) {
        String st;
        Scanner scanner = new Scanner(System.in);
        st = scanner.nextLine();
        scanner.close();
        System.out.println(scan(st) ? "OK" : "NOPE");
    }

}
